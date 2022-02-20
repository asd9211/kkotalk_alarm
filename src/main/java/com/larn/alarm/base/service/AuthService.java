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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.larn.alarm.exception.ServiceException;
import com.larn.alarm.utils.StringUtils;


@Service
public class AuthService extends HttpCallService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String AUTH_URL = "https://kauth.kakao.com/oauth/token";
	private static String authToken;

	private static void setAuthToken(String authToken) {
		AuthService.authToken = authToken;
	}

	public static String getAuthToken() {
		return authToken;
	}

	@Autowired MessageSource msgSource;

	public boolean setAuth(String code) {
		HttpHeaders header = new HttpHeaders();
		Map<String, String> parameters = new HashMap<>();
		String body = "";
		String accessToken = "";
		String tokenFailMsg = msgSource.getMessage("token.issued.fail", null, Locale.getDefault());
		String tokenSuccessMsg = msgSource.getMessage("token.issued.success", null, Locale.getDefault());

		header.set("Content-Type", "application/" + APP_TYPE_URL_ENCODED);

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

		logger.info("======================== Auth Request Start ========================");
		ResponseEntity<String> response = httpRequest(AUTH_URL, HttpMethod.POST, requestEntity);
        JSONObject jsonData = new JSONObject(response.getBody());
        accessToken = jsonData.get("access_token").toString();
        System.out.println(accessToken);
        if(StringUtils.isEmpty(accessToken)) {
        	throw new ServiceException(tokenFailMsg);
        }
        setAuthToken(accessToken);
		logger.info(tokenSuccessMsg);
        logger.info("======================== Auth Request End ========================");

		return true;
	}

}
