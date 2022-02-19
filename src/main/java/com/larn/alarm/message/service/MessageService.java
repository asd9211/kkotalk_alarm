package com.larn.alarm.message.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.larn.alarm.base.service.AuthService;
import com.larn.alarm.base.service.HttpCallService;
import com.larn.alarm.exception.ServiceException;
import com.larn.alarm.message.dto.DefaultMessageDto;
import com.larn.alarm.message.dto.ListMessageDto;

@Service
public class MessageService extends HttpCallService{ //확장포인트에 따라 인터페이스로 구현 고민
	private static Logger logger = LoggerFactory.getLogger(MessageService.class);
	private static final String MSG_SEND_SERVICE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
	private static final String SUCCESS_CODE = "0";

	@Autowired MessageSource msgSource;

	public String sendMessage(String accessToken, DefaultMessageDto msgDto) {
		String successMsg = msgSource.getMessage("msg.send.success", null, Locale.getDefault());
		String failMsg = msgSource.getMessage("msg.send.fail", null, Locale.getDefault());

		HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", "application/" + APP_TYPE_URL_ENCODED);
		header.set("Authorization", "Bearer " + accessToken);


    	JSONObject linkObj = new JSONObject();
    	linkObj.put("web_url", msgDto.getWebUrl());
    	linkObj.put("mobile_web_url", msgDto.getMobileUrl());

    	JSONObject templateObj = new JSONObject();
    	templateObj.put("object_type", msgDto.getObjType());
    	templateObj.put("text", msgDto.getText());
    	templateObj.put("link", linkObj);
    	templateObj.put("button_title", msgDto.getBtnTitle());

    	MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    	parameters.add("template_object", templateObj.toString());
    	
    	HttpEntity<?> messageRequestEntity = httpClientEntity(header, parameters);

        String resultCode = "";
        ResponseEntity<String> response = httpRequest(MSG_SEND_SERVICE_URL, HttpMethod.POST, messageRequestEntity);
        logger.info("SendMessageResponse======>{}", response.getBody());
        JSONObject jsonData = new JSONObject(response.getBody()); // 만료시간마다 리프레시하는 로직 추가 필요
        resultCode = jsonData.get("result_code").toString();
        if(resultCode.equals(SUCCESS_CODE)) {
        	return successMsg;
        }else {
        	throw new ServiceException(failMsg);
        }
	}
	
	public boolean sendListMessage(ListMessageDto msgDto) {
		
	 	JSONObject linkObj = new JSONObject();
    	linkObj.put("web_url", "");
    	linkObj.put("mobile_web_url", "");

    	JSONArray contentsArray = new JSONArray();

    	for(ListMessageDto dto : msgDto.getDtoList()) {
        	JSONObject contentsObj = new JSONObject();
	    	contentsObj.put("title", dto.getTitle());
	    	contentsObj.put("description", dto.getDescription());
	    	contentsObj.put("image_url", dto.getImageUrl());
	    	contentsObj.put("image_width", dto.getImageWidth());
	    	contentsObj.put("image_height", dto.getImageHeight());
	    	contentsObj.put("link", linkObj);
	    	contentsArray.put(contentsObj);
	    }

    	JSONObject templateObj = new JSONObject();
    	templateObj.put("object_type", "list");
    	templateObj.put("header_title", "맛집 추천");
    	templateObj.put("header_link", linkObj);
    	templateObj.put("contents", contentsArray);

    	MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    	parameters.add("template_object", templateObj.toString());
    	
    	// 서비스 따로 태워야 함. 임시.
    	HttpHeaders header = new HttpHeaders();
    	header.set("Content-Type", "application/" + APP_TYPE_URL_ENCODED);
 		header.set("Authorization", "Bearer " + AuthService.getAuthToken());

 		HttpEntity<?> messageRequestEntity = httpClientEntity(header, parameters);
 		System.out.println(messageRequestEntity.getBody());
        ResponseEntity<String> res = httpRequest(MSG_SEND_SERVICE_URL, HttpMethod.POST, messageRequestEntity);
        
        System.out.println(res.getBody().toString());
        
        return true;

	}
}
