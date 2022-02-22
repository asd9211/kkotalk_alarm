package com.larn.alarm.base.service;

import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.larn.alarm.utils.StringUtils;


/**
* RestTemplate를 통해 HttpService를 담당하는 Class
* @author larn
* @version 1.0
* @see None
*/
public class HttpCallService {
	protected static final String APP_TYPE_URL_ENCODED = "application/x-www-form-urlencoded;charset=UTF-8";
	protected static final String APP_TYPE_JSON = "application/json;charset=UTF-8";

	/**
	* Http 요청 클라이언트 객체 생성 method
	*
	* @ param Map<String,String> header HttpHeader 정보
	* @ param Object params HttpBody 정보
	* @ return HttpEntity 생성된 HttpClient객체 정보 반환
	* @ exception 예외사항
	*/
    public HttpEntity<?> httpClientEntity(HttpHeaders header, Object params) {
    	HttpHeaders requestHeaders = header;

        if (params == null || "".equals(params))
            return new HttpEntity<Object>(requestHeaders);
        else
            return new HttpEntity<Object>(params, requestHeaders);
    }

	/**
	* Http 요청 method
	*
	* @ param String url 요청 URL 정보
	* @ param HttpMethod method 요청 Method 정보
	* @ param  HttpEntity<?> entity 요청 EntityClient 객체 정보
	* @ return HttpEntity 생성된 HttpClient객체 정보 반환
	*/
    public ResponseEntity<String> httpRequest(String url, HttpMethod method, HttpEntity<?> entity){
		RestTemplate restTemplate = new RestTemplate();
    	return restTemplate.exchange(url, method, entity,String.class);
    }

    /**
	* Http 요청 method
	*
	* @ param URI url 요청 URL 정보
	* @ param HttpMethod method 요청 Method 정보
	* @ param  HttpEntity<?> entity 요청 EntityClient 객체 정보
	* @ return HttpEntity 생성된 HttpClient객체 정보 반환
	*/
    public ResponseEntity<String> httpRequest(URI url, HttpMethod method, HttpEntity<?> entity){
		RestTemplate restTemplate = new RestTemplate();
    	return restTemplate.exchange(url, method, entity,String.class);
    }
}
