package com.larn.alarm.message.service;

import java.util.Locale;

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

import com.larn.alarm.base.service.HttpCallService;
import com.larn.alarm.exception.ServiceException;
import com.larn.alarm.message.dto.DefaultMessageDto;
import com.larn.alarm.message.dto.ListMessageDto;

@Service
public class MessageService extends HttpCallService{ //확장포인트에 따라 인터페이스로 구현 고민
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String MSG_SEND_SERVICE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
	private static final String SUCCESS_CODE = "0"; //kakao api에서 return해주는 success code 값

	@Autowired MessageSource msgSource;

	public boolean sendMessage(String accessToken, DefaultMessageDto msgDto) {
		JSONObject linkObj = new JSONObject();
    	linkObj.put("web_url", msgDto.getWebUrl());
    	linkObj.put("mobile_web_url", msgDto.getMobileUrl());

    	JSONObject templateObj = new JSONObject();
    	templateObj.put("object_type", msgDto.getObjType());
    	templateObj.put("text", msgDto.getText());
    	templateObj.put("link", linkObj);
    	templateObj.put("button_title", msgDto.getBtnTitle());


    	HttpHeaders header = new HttpHeaders();
		header.set("Content-Type", APP_TYPE_URL_ENCODED);
		header.set("Authorization", "Bearer " + accessToken);

		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    	parameters.add("template_object", templateObj.toString());

    	HttpEntity<?> messageRequestEntity = httpClientEntity(header, parameters);

        String resultCode = "";
        ResponseEntity<String> response = httpRequest(MSG_SEND_SERVICE_URL, HttpMethod.POST, messageRequestEntity);
        JSONObject jsonData = new JSONObject(response.getBody());
        resultCode = jsonData.get("result_code").toString();

        return successCheck(resultCode);
	}

	public boolean sendListMessage(String accessToken, ListMessageDto msgDto) {
		JSONObject headerLinkObj = new JSONObject();
		headerLinkObj.put("web_url", msgDto.getWebUrl());
		headerLinkObj.put("mobile_web_url", msgDto.getMobileUrl());

    	JSONArray contentsArray = new JSONArray();

    	for(ListMessageDto dto : msgDto.getDtoList()) {
    	 	JSONObject linkObj = new JSONObject();
        	linkObj.put("web_url", dto.getWebUrl());
        	linkObj.put("mobile_web_url", dto.getMobileUrl());

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
    	templateObj.put("header_title", msgDto.getHeaderTitle());
    	templateObj.put("header_link", headerLinkObj);
    	templateObj.put("contents", contentsArray);

    	HttpHeaders header = new HttpHeaders();
    	header.set("Content-Type", APP_TYPE_URL_ENCODED);
 		header.set("Authorization", "Bearer " + accessToken);

 		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    	parameters.add("template_object", templateObj.toString());

 		HttpEntity<?> messageRequestEntity = httpClientEntity(header, parameters);

 		ResponseEntity<String> response = httpRequest(MSG_SEND_SERVICE_URL, HttpMethod.POST, messageRequestEntity);
        logger.info("SendMessageResponse======>{}", response.getBody());
        JSONObject jsonData = new JSONObject(response.getBody());
        String resultCode = jsonData.get("result_code").toString();

        return successCheck(resultCode);
	}

	public boolean successCheck(String resultCode) {
		String failMsg = msgSource.getMessage("msg.send.fail", null, Locale.getDefault());
		String successMsg = msgSource.getMessage("msg.send.success", null, Locale.getDefault());

		if(resultCode.equals(SUCCESS_CODE)) {
			logger.info(successMsg);
			return true;
		}else {
			throw new ServiceException(failMsg);
		}

	}
}
