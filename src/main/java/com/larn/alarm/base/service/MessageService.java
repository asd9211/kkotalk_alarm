package com.larn.alarm.base.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.larn.alarm.exception.ServiceException;

@Service
public class MessageService extends HttpCallService{ //확장포인트에 따라 인터페이스로 구현 고민
	private static Logger logger = LoggerFactory.getLogger(MessageService.class);
	private static final String MSG_SEND_SERVICE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
	private static final String SUCCESS_CODE = "0";

	@Autowired MessageSource msgSource;

	public String sendMessage(String accessToken) {
		ResponseEntity<String> response;
		String successMsg = msgSource.getMessage("msg.send.success", null, Locale.getDefault());
		String failMsg = msgSource.getMessage("msg.send.fail", null, Locale.getDefault());
		String appType = "x-www-form-urlencoded;charset=UTF-8";
		String linkUrl = "https://developers.kakao.com";

		Map<String, String> header = new HashMap<>();
		header.put("appType", appType);
		header.put("token", accessToken);

    	JSONObject linkObj = new JSONObject();
    	linkObj.put("web_url", linkUrl);
    	linkObj.put("mobile_web_url", linkUrl);

    	JSONObject templateObj = new JSONObject();
    	templateObj.put("object_type", "text");
    	templateObj.put("text", "하이하이");
    	templateObj.put("link", linkObj);
    	templateObj.put("button_title", "버튼입니다.");

    	Map<String,String> parameters = new HashMap<>();
    	parameters.put("template_object", "");
    	String body = "";

    	for(String key : parameters.keySet()) {
        	body += key + "=" + templateObj.toString();
        }

    	logger.info("send parameter ====> {}", body);
    	HttpEntity<?> messageRequestEntity = httpClientEntity(header, body);
        try {
        	String resultCode = "";
        	response = httpRequest(MSG_SEND_SERVICE_URL, HttpMethod.POST, messageRequestEntity);
        	logger.info("SendMessageResponse======>{}", response.getBody());
        	JSONObject jsonData = new JSONObject(response.getBody());
        	resultCode = jsonData.get("result_code").toString();
        	if(resultCode.equals(SUCCESS_CODE)) {
            	return successMsg;
        	}else {
        		throw new ServiceException(failMsg);
        	}
        }catch (Exception e) {
        	e.getStackTrace();
		}
        return failMsg;
	}
}
