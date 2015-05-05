/*
*  (C) Copyright 2010-2013 hSenid Mobile Solutions (Pvt) Limited.
*  All Rights Reserved.
*
*  These materials are unpublished, proprietary, confidential source code of
*  hSenid Mobile Solutions (Pvt) Limited and constitute a TRADE SECRET
*  of hSenid Mobile Solutions (Pvt) Limited.
*
*  hSenid Mobile Solutions (Pvt) Limited retains all title to and intellectual
*  property rights in these materials.
*/
package hms.webrtc.demo.service;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class RestTemplateFixer {
    private RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void init() {
        for (HttpMessageConverter converter : restTemplate.getMessageConverters()) {
            if (converter instanceof MappingJacksonHttpMessageConverter) {
                ((MappingJacksonHttpMessageConverter) converter)
                        .setSupportedMediaTypes(new ArrayList<MediaType>(){{
                    add(MediaType.APPLICATION_JSON);
                }});
            }
        }
    }
}
