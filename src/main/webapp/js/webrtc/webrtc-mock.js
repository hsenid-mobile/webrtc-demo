(function (data) {

    var signalChannel;
    var refId = "120023232343434";
    var state_context = stateContext;
    var $root;

    var sampleIce = "a=candidate:1432344491 1 udp 2113937151 192.168.0.74 53838 typ host generation 0";
    var sampleSdp = "v=0\n" +
        "\" +\n" +
        "            \"o=- 8561504440366956394 2 IN IP4 127.0.0.1\\n\" +\n" +
        "            \"s=-\\n\" +\n" +
        "            \"t=0 0\\n\" +\n" +
        "            \"a=group:BUNDLE audio\\n\" +\n" +
        "            \"a=msid-semantic: WMS UfU1Nz6ybT9x3JCWfTwzRdFcA79YtydQ83lx\\n\" +\n" +
        "            \"m=audio 53838 RTP/SAVPF 111 103 104 0 8 106 105 13 126\\n\" +\n" +
        "            \"c=IN IP4 192.168.0.74\\n\" +\n" +
        "            \"a=rtcp:53838 IN IP4 192.168.0.74\\n\" +\n" +
        "            \"a=candidate:1432344491 1 udp 2113937151 192.168.0.74 53838 typ host generation 0\\n\" +\n" +
        "            \"a=ice-options:google-ice\\n\" +\n" +
        "            \"a=fingerprint:sha-256 20:0B:87:F5:6C:C1:18:8D:DA:30:FE:5B:89:23:E7:B9:2F:B7:8B:C4:7F:03:E0:A8:3F:B6" +
        "            \"a=ssrc:1882624536 mslabel:UfU1Nz6ybT9x3JCWfTwzRdFcA79YtydQ83lx\\n\" +\n" +
        "            \"a=ssrc:1882624536 label:UfU1Nz6ybT9x3JCWfTwzRdFcA79YtydQ83lxa0";


    var webrtc_adapter = function (handlers) {
        var connection = createSignalingChannel(onMessage);
        var adapter = Object.create(null);
        adapter.makeCall = function (internalRefId) {
            signalChannel = connection.conn;
            send_registration_details(refId);
        };

        adapter.hangup = function (event) {
            signalChannel.send(JSON.stringify({hang_up: "bye"}))
        };

        function send_registration_details(refId) {
            signalChannel.send(JSON.stringify({register: refId}))
        }

        function handleState(signal) {
            var current_state = signal.msg;
            if (current_state == "registered") {
                initWebRtcCall(signal);
            }
        }

        function handle_invite_state(signal) {
            var msg = signal.msg;
            console.log("Call State received - " + msg);
            if (msg == "RINGING") {
                handlers.ringing(msg);
            } else if (msg == "CONNECTED") {
                signalChannel.send(JSON.stringify({type: "ack"}));
                handlers.connected(msg);
            } else if (msg == "BYE_FROM_SIP") {
                signalChannel.send(JSON.stringify({type: "ack_call_end"}))
                handlers.call_ended(msg);
            } else if (msg == "SESSION_PROGRESS") {
                console.log("Remote stream received")
            } else if (msg == "OK_FOR_BYE") {
                handlers.call_ended(msg);
            }
        }

        /**
         * Need to create a separate object for signaling protocol implementation and need to maintain a state per session.
         */

        function initWebRtcCallMock(signal) {
            signalChannel.send(JSON.stringify({"sdp": sampleSdp}));
            signalChannel.send(JSON.stringify({"ice-candidate": sampleIce}));
            setTimeout(function (event) {
                signalChannel.send(JSON.stringify({"ice-gathering_state": "ice_gathering_completed"}));
            }, 3000);
        }


        function initWebRtcCall(signal) {
            handlers.dialing(signal.msg);
            initWebRtcCallMock(refId);
        }


        function onMessage(event) {
            var signal = event.data;
            var jsonSignal = JSON.parse(signal);

            console.log("Got message from the server" + signal);

            if (!jsonSignal.command) {
                return;
            }

            var command = jsonSignal.command;
            var msg = jsonSignal.msg;

            if (command == "registration_state") {
                console.log("registration_state: " + msg);
                handleState(jsonSignal);

            } else if (command == "selected_ice") {
                console.log("selected_ice_received: " + msg);

            } else if (command == "remote_sdp") {
                console.log("Remote-Sdp received- " + msg);
            } else if (command == "call_state") {
                handle_invite_state(jsonSignal);
            }
        }

        function createSignalingChannel(callBackMethod) {
            var ws;
            var call_back_func = callBackMethod;
            if ('WebSocket' in window) {
                ws = new WebSocket("ws://" + "192.168.0.74:9091" + "/WebRTC/call");
                ws.onopen = function (event) {
                    console.log("WS connection established");

                };

                ws.onmessage = function (event) {
                    console.log("Message received -> " + event.data);
                    callBackMethod(event);
                };

                ws.onclose = function (event) {
                    console.log("WebSocket closed");
                };
            } else {
                console.log("Not supported")
            }
            var connection = {
                send: function (msg, call_back) {
                    console.log("WS message sending : " + msg);
                    ws.send(msg);
                    call_back_func = call_back;
                }
            };
            return {
                conn: connection
            };
        }

        return adapter;

    };

    $(document).ready(function () {
        $root = $(".web_rtc_root");
        var st = stateElements("./img");
        var $idleStateElement = $(".state-idle", $root)
        if ($idleStateElement.length == 0) {
            $root.append(st.idle)
            $idleStateElement = $(".state-idle", $root)
        }

        var $dialingStateElement = $(".state-dialing", $root)
        if ($dialingStateElement.length == 0) {
            $root.append(st.dialing);
            $dialingStateElement = $(".state-dialing", $root)
        }

        var $ringStateElement = $(".state-ring", $root)
        if ($ringStateElement.length == 0) {
            $root.append(st.ringing)
            $ringStateElement = $(".state-ring", $root)
        }

        var $connectedStateElement = $(".state-connected", $root)
        if ($connectedStateElement.length == 0) {
            $root.append(st.connected)
            $connectedStateElement = $(".state-connected", $root)
        }

        var $disconnectedStateElement = $(".state-disconnected", $root)
        if ($disconnectedStateElement.length == 0) {
            $root.append(st.disconnected)
            $disconnectedStateElement = $(".state-disconnected", $root)
        }

        var handlers = {
            ringing: function (event) {
                stateContext.handleRtcEvent({type: "progress"});
            },
            connected: function (event) {
                stateContext.handleRtcEvent({type: "started"});
            },
            call_ended: function (event) {
                stateContext.handleRtcEvent({type: "ended"});
            },
            dialing: function (event) {
                stateContext.handleRtcEvent({type: "dialing"});
            }};
        state_context.change_state_element_func = changeElementsForState;
        state_context.adaptor = new webrtc_adapter(handlers);
        state_context.internalRefId = refId;
        state_context.change_state = changeElementsForState;
        state_context.disconnected_element = $disconnectedStateElement;
        state_context.dialing_element = $dialingStateElement;
        state_context.ringing_element = $ringStateElement;
        state_context.connected_element = $connectedStateElement;
        state_context.idle_element = $idleStateElement;
        state_context.$root = $root;
        state_context.setToIdleState();

        function changeElementsForState(state, callback, sound) {
            $(".phone-state", $root).remove();
            $root.append(state);
            if (callback == null) {
                state.click(function (event) {
                    state_context.handleUIEvent({type: "click"});
                })
            } else {
                state.click(callback)
            }
        }


    });

    var stateElements = function (path) {
        return {
            idle: '<div class="phone-state state-idle" style="background-image: url(\'' + path +
                '/icons/button_idle.png\');width: 150px;height: 35px; background-size: 107%; float:left;background-position: -4px -3px;border-radius: 16px;margin-left:25%;"></div>',
            dialing: '<div id="phone-dial" class="phone-state state-dialing" style="background-image: url(\'' + path +
                '/icons/button_dialing.gif\');width: 150px;height: 35px; background-size: 107%; float:left;background-position: -4px -3px;border-radius: 16px;margin-left:25%;"></div>',
            ringing: '<div id="phone-ring" class="phone-state state-ring" style="background-image: url(\'' + path +
                '/icons/button_ringing.gif\');width: 150px;height: 35px; background-size: 107%; float:left;background-position: -4px -3px;border-radius: 16px;margin-left:25%;"></div>',
            connected: '<div id="phone-connected" class="phone-state state-connected" style="background-image: url(\'' + path +
                '/icons/button_connected.gif\');width: 150px;height: 35px; background-size: 107%; float:left;background-position: -4px -3px;border-radius: 16px;margin-left:25%;"></div>',
            disconnected: '<div id="phone-disconnected" class="phone-state state-disconnected" style="background-image: url(\'' +
                path +
                '/icons/button_disconnect.png\');width: 150px;height: 35px; background-size: 107%; float:left;background-position: -4px -3px;border-radius: 16px;margin-left:25%;"></div>'
        }
    };

    /**
     * <p>
     *     Updates the label with relevant message.
     * </p>
     * @param msg
     */
    function updateLabel(msg) {
        $("#status_label").text(msg);
    }

})("null");
