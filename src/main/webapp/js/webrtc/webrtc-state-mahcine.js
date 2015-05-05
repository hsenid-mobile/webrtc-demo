/**
 * WebRtc State Machine
 *
 * States - idle , dialing, ringing, connected, disconnected
 */


var AbstractState = {
    handleCallEndEvent: function (event, stateContext) {
        this.handleDisconnectingRtc(stateContext);
        this.handleCallEndStateTransit(event, stateContext)
    },
    handleDisconnectingRtc: function (stateContext) {

    },
    handleCallEndStateTransit: function (event, stateContext) {
        stateContext.change_state_element_func(stateContext.disconnected_element, null);
        stateContext.currentState = stateContext.disconnectedState;
        setTimeout(stateContext.setToIdleState, 2000)
    }
};

var idleState = (function () {
    var ret = Object.create(AbstractState);
    ret.handleRtcEvent = function (event, stateContext) {
        var type = event.type;
        console.log("Invalid call state to handle the event : " + type);
    };
    ret.handleUIEvent = function (event, stateContext) {
        var type = event.type;
        switch (type) {
            case "click" :
                handleCallEvent(event, stateContext);
                break;
            default :
                console.log("Undefined UI event : " + type);
        }

        function handleCallEvent(event, stateContext) {
            var parameterMap = stateContext;
            var internalRefId = parameterMap.internalRefId;
            stateContext.adaptor.makeCall(internalRefId);
            parameterMap.change_state_element_func(parameterMap.dialing_element, null);
            stateContext.currentState = stateContext.dialState;
        }
    };
    return ret;
})();

var dialState = (function () {
    var ret = Object.create(AbstractState);
    var handleProgressEvent = function (stateContext) {
        var parameterMap = stateContext;
        parameterMap.change_state_element_func(parameterMap.ringing_element, null);
        stateContext.currentState = stateContext.ringState;
    };
    ret.handleRtcEvent = function (event, stateContext) {
        var type = event.type;
        switch (type) {
            case "started" :
                console.log("Invalid rtc event for state Dialing : " + type);
                break;
            case "failed" :
            case "ended"  :
                this.handleCallEndStateTransit(event, stateContext);
                break;
            case "progress" :
                handleProgressEvent(stateContext);
                break;
            default :
                console.log("Undefined rtc event : " + type);
                break;
        }
        console.log("RTC EVENTS : " + type);
    };
    ret.handleUIEvent = function (event, stateContext) {
        var type = event.type;
        switch (type) {
            case "click" :
                this.handleCallEndEvent(event, stateContext);
                break;
            default :
                console.log("Undefined UI event : " + type);
        }
    };
    ret.handleDisconnectingRtc = function (stateContext) {
        console.log("Disconnecting call .....");
        stateContext.adaptor.hangup();
    };
    return ret;
})();

var ringState = (function () {
    var ret = Object.create(AbstractState);
    ret.handleRtcEvent = function (event, stateContext) {
        var type = event.type;
        switch (type) {
            case "started" :
                handleStartEvent(event, stateContext);
                break;
            case "failed" :
            case "ended"  :
                this.handleCallEndStateTransit(event, stateContext);
                break;
            case "progress" :
                console.log("Invalid rtc event for state Ringing : " + type);
                break;
            default :
                console.log("Undefined rtc event : " + type);
                break;
        }
        console.log("RTC EVENTS : " + type);
    };
    ret.handleUIEvent = function (event, stateContext) {
        var type = event.type;
        switch (type) {
            case "click" :
                this.handleCallEndEvent(event, stateContext);
                break;
            default :
                console.log("Undefined UI event : " + type);
        }
    };
    ret.handleDisconnectingRtc = function (stateContext) {
        console.log("Disconnecting call .....");
        stateContext.adaptor.hangup()
    };
    function handleStartEvent(event, stateContext) {
        var parameterMap = stateContext;
        parameterMap.change_state_element_func(parameterMap.connected_element, null);
        stateContext.currentState = stateContext.connectedState;
    }

    return ret;
})();

var connectedState = (function () {
    var ret = Object.create(AbstractState);
    ret.handleRtcEvent = function (event, stateContext) {
        var type = event.type;
        switch (type) {
            case "started" :
                console.log("Invalid rtc event for state Connected : " + type);
            case "failed" :
            case "ended"  :
                this.handleCallEndStateTransit(event, stateContext);
                break;
            case "progress" :
                console.log("Invalid rtc event for state Connected : " + type);
                break;
            default :
                console.log("Undefined rtc event : " + type);
                break;
        }
        console.log("RTC EVENTS : " + type);
    };
    ret.handleUIEvent = function (event, stateContext) {
        var type = event.type;
        switch (type) {
            case "click" :
                this.handleCallEndEvent(event, stateContext);
                break;
            default :
                console.log("Undefined UI event : " + type);
        }
    };
    ret.handleDisconnectingRtc = function (stateContext) {
        console.log("Disconnecting call .....");
        stateContext.adaptor.hangup();
    };
    return ret;
})();

var disconnectedState = (function () {
    var ret = Object.create(AbstractState);
    ret.handleRtcEvent = function (event, stateContext) {
        var type = event.type;
//        this.handleCallEndEvent()
        console.log("Invalid rtc event for state Disconnected : " + type);
    };
    ret.handleUIEvent = function (event, stateContext) {
        var type = event.type;
        console.log("Undefined UI event : " + type);
    };
    return ret;
})()

var stateContext = {
    adaptor: {},
    currentState: idleState,
    $root: null,
    idleState: idleState,
    dialState: dialState,
    ringState: ringState,
    connectedState: connectedState,
    disconnectedState: disconnectedState,
    setToIdleState: function () {
        stateContext.$root.empty();
        stateContext.$root.append(stateContext.idle_element);
        stateContext.change_state_element_func(stateContext.idle_element);
        stateContext.currentState = idleState;
    },
    handleRtcEvent: function (event) {
        this.currentState.handleRtcEvent(event, this)
    },
    handleUIEvent: function (event) {
        this.currentState.handleUIEvent(event, this)
    }
};
;