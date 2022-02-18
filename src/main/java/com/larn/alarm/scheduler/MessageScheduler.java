package com.larn.alarm.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.larn.alarm.base.service.AuthService;
import com.larn.alarm.exception.ServiceException;
import com.larn.alarm.message.dto.DefaultMessageDto;
import com.larn.alarm.message.service.MessageService;
import com.larn.alarm.utils.StringUtils;
import com.larn.alarm.weather.dto.WeatherInfoDto;
import com.larn.alarm.weather.service.WeaterInfoService;

@Component
public class MessageScheduler {
	private static Logger logger = LoggerFactory.getLogger(MessageScheduler.class);

	@Autowired
	MessageService msgService;

	@Autowired
	WeaterInfoService weaterInfoService;

	@Scheduled(fixedRate = 5000)
    public void morningWeatherInfoScheduler() { // 아침 날씨 알람 스케쥴러
		String linkUrl = "https://weather.naver.com/today";
		String authToken = AuthService.getAuthToken();
		logger.info("==============WeatherSceduler start=============="); // 추후 AOP로 Service Start 로그 관리
		if(!StringUtils.isEmpty(authToken)) {
			WeatherInfoDto weatherInfoDto = weaterInfoService.getWeaterInfo();
			DefaultMessageDto msgDto = new DefaultMessageDto();
			StringBuilder sb = new StringBuilder();
			Map<String,String> linkMap = new HashMap<>();


			sb.append(weatherInfoDto.getDay() + "일 " + System.lineSeparator());
			sb.append("발표시간 : " + weatherInfoDto.getTime() + " 기준  " + System.lineSeparator());
			sb.append("현재온도 : " + weatherInfoDto.getTemp() + System.lineSeparator());
			sb.append("현재기상 : " + weatherInfoDto.getWeatherStatus() + " 입니다. ");

			String msg = sb.toString();
			msgDto.setObjType("text");
			msgDto.setText(msg);
			msgDto.setBtnTitle("날씨정보 보기");
			msgDto.setWebUrl(linkUrl);
			msgDto.setMobileUrl(linkUrl);

			msgService.sendMessage(authToken, msgDto);
		}else {
			throw new ServiceException("토큰정보가 없습니다!");
		}

	}
}
