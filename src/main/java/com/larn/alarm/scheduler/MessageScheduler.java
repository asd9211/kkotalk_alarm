package com.larn.alarm.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.larn.alarm.base.dto.DefaultMessageDto;
import com.larn.alarm.base.dto.WeatherInfoDto;
import com.larn.alarm.base.service.AuthService;
import com.larn.alarm.base.service.MessageService;
import com.larn.alarm.base.service.WeaterInfoService;
import com.larn.alarm.exception.ServiceException;
import com.larn.alarm.utils.StringUtils;

@Component
public class MessageScheduler {
	private static Logger logger = LoggerFactory.getLogger(MessageScheduler.class);

	@Autowired
	MessageService msgService;

	@Autowired
	WeaterInfoService weaterInfoService;

	@Scheduled(fixedRate = 5000)
    public void morningWeatherInfoScheduler() { // 아침 날씨 알람 스케쥴러
		String authToken = AuthService.getAuthToken();
		logger.info("==============WeatherSceduler start=============="); // 추후 AOP로 Service Start 로그 관리
		System.out.println(authToken);
		if(!StringUtils.isEmpty(authToken)) {
			WeatherInfoDto WeatherInfoDto = weaterInfoService.getWeaterInfo();
			DefaultMessageDto msgDto = new DefaultMessageDto();
			StringBuilder sb = new StringBuilder();
			sb.append(WeatherInfoDto.getDay() + "일 " + System.lineSeparator());
			sb.append("발표시간 : " + WeatherInfoDto.getTime() + " 기준  " + System.lineSeparator());
			sb.append("현재온도 : " + WeatherInfoDto.getTemp() + System.lineSeparator());
			sb.append("현재기상 : " + WeatherInfoDto.getWeatherStatus() + " 입니다. ");
			String msg = sb.toString();
			msgDto.setObjType("text");
			msgDto.setText(msg);
			msgDto.setBtnTitle("날씨정보 보기");

			msgService.sendMessage(authToken, msgDto);
		}else {
			throw new ServiceException("토큰정보가 없습니다!");
		}

	}
}
