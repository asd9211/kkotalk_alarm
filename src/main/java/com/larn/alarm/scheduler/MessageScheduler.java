package com.larn.alarm.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.larn.alarm.base.service.AuthService;
import com.larn.alarm.exception.ServiceException;
import com.larn.alarm.food.service.FoodInfoService;
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

	@Autowired
	FoodInfoService foodInfoService;

	//@Scheduled(cron="0 00 07 * * ?")
	//@Scheduled(fixedRate = 5000)
    public void morningWeatherInfoScheduler() { // 아침 날씨 알람 스케쥴러
		String linkUrl = "https://weather.naver.com/today";
		String authToken = AuthService.getAuthToken();
		logger.info("==============WeatherSceduler start=============="); // 추후 AOP로 Service Start 로그 관리
		if(!StringUtils.isEmpty(authToken)) {
			WeatherInfoDto weatherInfoDto = weaterInfoService.getWeatherInfo();
			DefaultMessageDto msgDto = new DefaultMessageDto();

			int temp = Integer.parseInt(weatherInfoDto.getTemp());
			String yyyy = weatherInfoDto.getDay().substring(0, 4);
			String mm = weatherInfoDto.getDay().substring(4, 6);
			String dd = weatherInfoDto.getDay().substring(6, 8);
			String time = weatherInfoDto.getTime();
			time = time.substring(0,2) + ":" + time.substring(2,4);
			String weatherStatus = weatherInfoDto.getWeatherStatus();
			String recommandWear = weaterInfoService.getWearRecommandForWeather(temp);


			StringBuilder sb = new StringBuilder();
			sb.append(yyyy + "년 " + mm + "월 " + dd + "일 " + System.lineSeparator());
			sb.append("발표시간 : " + time + "시 기준  " + System.lineSeparator());
			sb.append("현재온도 : " + temp + System.lineSeparator());
			sb.append("현재기상 : " + weatherStatus + " 입니다. " + System.lineSeparator());
			sb.append("온도에 맞는 오늘의 추천의상은 : " + System.lineSeparator() + recommandWear + " 입니다. " + System.lineSeparator());

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

	@Scheduled(cron="0 00 14 * * ?")
	public void nutrientsAlarmScheduler() { // 영양제 알림 스케쥴러
		String linkUrl = "";
		String authToken = AuthService.getAuthToken();
		logger.info("==============nutrientsAlarmSceduler start=============="); // 추후 AOP로 Service Start 로그 관리
		if(!StringUtils.isEmpty(authToken)) {
			DefaultMessageDto msgDto = new DefaultMessageDto();

			msgDto.setObjType("text");
			msgDto.setText("영양제를 먹을 시간입니다.");
			msgDto.setBtnTitle("");
			msgDto.setWebUrl(linkUrl);
			msgDto.setMobileUrl(linkUrl);

			msgService.sendMessage(authToken, msgDto);
		}else {
			throw new ServiceException("토큰정보가 없습니다!");
		}
	}

	@Scheduled(fixedRate = 10000)
	public void testScheduler() {
		msgService.sendListMessage(foodInfoService.getFoodInfoForWeather("test"));
	}
}
