package com.larn.alarm.base.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.larn.alarm.utils.StringUtils;

public class HttpCallService {
    public HttpEntity<?> httpClientEntity(Map<String,String> header, String params) {
    	HttpHeaders requestHeaders = new HttpHeaders();

        if(!StringUtils.isEmpty(header.get("appType"))) {
	        requestHeaders.set("Content-Type", "application/" + header.get("appType"));
        }

        if(!StringUtils.isEmpty(header.get("token"))) {
	        requestHeaders.set("Authorization", "Bearer " + header.get("token"));
        }

        if ( "".equals(params) )
            return new HttpEntity<Object>(requestHeaders);
        else
            return new HttpEntity<Object>(params, requestHeaders);
    }

    public ResponseEntity<String> httpRequest(String url, HttpMethod method, HttpEntity<?> entity){
		RestTemplate restTemplate = new RestTemplate();
    	return restTemplate.exchange(url, method, entity,String.class);
    }
}
