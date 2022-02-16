package com.larn.alarm.base.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.larn.alarm.base.service.AuthCallBackService;
import com.larn.alarm.base.service.MessageService;

@RestController
public class BaseController {
	private static Logger logger = LoggerFactory.getLogger(BaseController.class);


	@Autowired
	AuthCallBackService authCallBackService;
	@Autowired
	MessageService messageService;

	 @GetMapping("/")
	    public String serviceStart(@RequestParam("code") String code) {
		 logger.info("============== first Service request ==============");
		 String accessToken = authCallBackService.getAuth(code);
		 return messageService.sendMessage(accessToken);
	 }


}
