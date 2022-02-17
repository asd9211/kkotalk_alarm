package com.larn.alarm.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.larn.alarm.base.dto.DefaultMessageDto;
import com.larn.alarm.base.service.AuthService;
import com.larn.alarm.base.service.MessageService;
import com.larn.alarm.base.service.WeaterInfoService;
import com.larn.alarm.utils.StringUtils;

@Component
public class MessageScheduler {

	@Autowired
	MessageService msgService;

	@Autowired
	WeaterInfoService weaterInfoService;

	@Scheduled(fixedRate = 5000) //
    public void morningWheaterScheduler() { // 아침 날씨 알람 스케쥴러
		weaterInfoService.getWeaterInfo();
		String authToken = AuthService.getAuthToken();
		if(!StringUtils.isEmpty(authToken)) {
		}

	}
}
