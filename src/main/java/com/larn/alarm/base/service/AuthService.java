package com.larn.alarm.base.service;

import java.util.Locale;

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

import com.larn.alarm.base.dto.AuthDto;
import com.larn.alarm.base.repository.AuthRepository;
import com.larn.alarm.exception.ServiceException;
import com.larn.alarm.utils.StringUtils;


/**
* kakao Api Auth를 담당하는 Class
* @author larn
* @version 1.0
* @see None
*/
@Service
public class AuthService extends HttpCallService implements AuthServiceInterface{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String AUTH_URL = "https://kauth.kakao.com/oauth/token";

	@Autowired
	MessageSource msgSource;

	@Autowired
	AuthRepository authRepository;

	/**
	* kakao API 토큰 발급 method
	*
	* @ param String code kakao API에서 Return해주는 Code 정보
	* @ return boolean 토큰 발급 Y/N
	* @ exception 예외사항
	*/
	public boolean saveAuthToken(String code) {
		AuthDto authDto = getKakaoAuthToken(code);
		return authRepository.saveAuthToken(authDto.getAccessToken()) && authRepository.saveRefrashAuthToken(authDto.getRefrashToken());
	}

	/**
	* kakao API 토큰 Refrash method
	*
	* @ param
	* @ return boolean 토큰 Refrash Y/N
	* @ exception 예외사항
	*/
	public boolean saveAuthRefash() {
		AuthDto authDto = getRefrashToken();
		return authRepository.saveAuthToken(authDto.getAccessToken());
	}

	public AuthDto getKakaoAuthToken(String code) {
		HttpHeaders header = new HttpHeaders();
		String accessToken = "";
		String refrashToken = "";
		String tokenFailMsg = msgSource.getMessage("token.issued.fail", null, Locale.getDefault());
		String tokenSuccessMsg = msgSource.getMessage("token.issued.success", null, Locale.getDefault());
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

		header.set("Content-Type", APP_TYPE_URL_ENCODED);

		parameters.add("code", code);
		parameters.add("grant_type", msgSource.getMessage("kakao.auth.grant_type",null, Locale.getDefault()));
		parameters.add("client_id", msgSource.getMessage("kakao.auth.client_id",null, Locale.getDefault()));
		parameters.add("redirect_url", msgSource.getMessage("kakao.auth.redirect_url",null, Locale.getDefault()));
		parameters.add("client_secret", msgSource.getMessage("kakao.auth.client_secret",null, Locale.getDefault()));

		HttpEntity<?> requestEntity = httpClientEntity(header, parameters);

		logger.info("======================== Auth Request Start ========================");
		ResponseEntity<String> response = httpRequest(AUTH_URL, HttpMethod.POST, requestEntity);
        JSONObject jsonData = new JSONObject(response.getBody());
        accessToken = jsonData.get("access_token").toString();
        refrashToken = jsonData.get("refresh_token").toString();
        if(StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refrashToken)) {
        	throw new ServiceException(tokenFailMsg);
        }

        AuthDto authDto = new AuthDto();
        authDto.setAccessToken(accessToken);
        authDto.setRefrashToken(refrashToken);

		logger.info(tokenSuccessMsg);
        logger.info("======================== Auth Request End ========================");

		return authDto;
	}

	public AuthDto getRefrashToken() {
		String accessToken = "";
		String tokenFailMsg = msgSource.getMessage("token.issued.fail", null, Locale.getDefault());
		String tokenSuccessMsg = msgSource.getMessage("token.issued.success", null, Locale.getDefault());
		HttpHeaders header = new HttpHeaders();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();

		header.set("Content-Type", APP_TYPE_URL_ENCODED);

		parameters.add("grant_type", msgSource.getMessage("kakao.auth.refrash_grant_type",null, Locale.getDefault()));
		parameters.add("client_id", msgSource.getMessage("kakao.auth.client_id",null, Locale.getDefault()));
		parameters.add("refresh_token", authRepository.getRefrashAuthToken());
		parameters.add("client_secret", msgSource.getMessage("kakao.auth.client_secret",null, Locale.getDefault()));

		HttpEntity<?> requestEntity = httpClientEntity(header, parameters);

		logger.info("======================== AuthRefrash Request Start ========================");
		ResponseEntity<String> response = httpRequest(AUTH_URL, HttpMethod.POST, requestEntity);
        JSONObject jsonData = new JSONObject(response.getBody());
        accessToken = jsonData.get("access_token").toString();
        if(StringUtils.isEmpty(accessToken)) {
        	throw new ServiceException(tokenFailMsg);
        }
        AuthDto authDto = new AuthDto();
        authDto.setAccessToken(accessToken);

        logger.info(tokenSuccessMsg);
        logger.info("======================== AuthRefrash Request End ========================");

		return authDto;
	}

}
