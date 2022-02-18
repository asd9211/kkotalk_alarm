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

@RestController
public class BaseController {
	private static Logger logger = LoggerFactory.getLogger(BaseController.class);

	@Autowired
	AuthService authService;
	@Autowired
	MessageService messageService;
	@Autowired
	MessageSource msgSource;

	@GetMapping("/")
	public String serviceStart(@RequestParam("code") String code) {
		logger.info("============== first Service request ==============");
		boolean tokenYN = authService.setAuth(code);
		String tokenSuccessMsg = msgSource.getMessage("token.issued.success", null, Locale.getDefault());
		String tokenFailMsg = msgSource.getMessage("token.issued.fail", null, Locale.getDefault());

		if (tokenYN)
			return tokenSuccessMsg;
		return tokenFailMsg;
	}


}
