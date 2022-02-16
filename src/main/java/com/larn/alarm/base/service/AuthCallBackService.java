package com.larn.alarm.base.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

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
import com.larn.alarm.utils.StringUtils;


@Service
public class AuthCallBackService extends HttpCallService {
	private static Logger logger = LoggerFactory.getLogger(AuthCallBackService.class);

	private static final String AUTH_URL = "https://kauth.kakao.com/oauth/token";

	@Autowired MessageSource msgSource;

	public String getAuth(String code) {
		Map<String, String> header = new HashMap<>();
		Map<String, String> parameters = new HashMap<>();
		String body = "";//a
		String authToken = "";
		String tokenFailMsg = msgSource.getMessage("token.issued.fail", null, Locale.getDefault());
		String tokenSuccessMsg = msgSource.getMessage("token.issued.success", null, Locale.getDefault());

		ResponseEntity<String> response;

		header.put("appType", "x-www-form-urlencoded;charset=UTF-8");

		parameters.put("code", code);
		parameters.put("grant_type", msgSource.getMessage("grant_type",null, Locale.getDefault()));
		parameters.put("client_id", msgSource.getMessage("client_id",null, Locale.getDefault()));
		parameters.put("redirect_url", msgSource.getMessage("redirect_url",null, Locale.getDefault()));
		parameters.put("client_secret", msgSource.getMessage("client_secret",null, Locale.getDefault()));

		for (Entry<String, String> entry : parameters.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			body += key + "=" + value;
			body += "&";
		}
		body = body.replaceFirst(".$", "");

		HttpEntity<?> requestEntity = httpClientEntity(header, body);

		try {
			logger.info("======================== Auth Request Start ========================");
			response = httpRequest(AUTH_URL, HttpMethod.POST, requestEntity);
        	JSONObject jsonData = new JSONObject(response.getBody());
        	authToken = jsonData.get("access_token").toString();
        	if(StringUtils.isEmpty(authToken)) {
        		throw new ServiceException(tokenFailMsg);
        	}
			logger.info(tokenSuccessMsg);
        	logger.info("======================== Auth Request End ========================");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return authToken;
	}

}
