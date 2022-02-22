package com.larn.alarm.base.controller;

import java.util.Locale;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.larn.alarm.base.service.AuthService;
import com.larn.alarm.message.service.MessageService;

/**
* Base Controller
* @author larn
* @version 1.0
* @see None
*/
@RestController
public class BaseController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AuthService authService;
	@Autowired
	MessageService messageService;
	@Autowired
	MessageSource msgSource;

	/**
	* 서비스 시작 method
	*
	* @ param String code kakao API에서 Return해주는 Code 정보
	* @ return String 토큰 발급 Y/N
	* @ exception 예외사항
	*/
	@GetMapping("/")
	public String serviceStart(@RequestParam("code") String code) {
		boolean tokenYN = authService.setAuth(code);
		String tokenSuccessMsg = msgSource.getMessage("token.issued.success", null, Locale.getDefault());
		String tokenFailMsg = msgSource.getMessage("token.issued.fail", null, Locale.getDefault());

		if (tokenYN)
			return tokenSuccessMsg;
		return tokenFailMsg;
	}
}
