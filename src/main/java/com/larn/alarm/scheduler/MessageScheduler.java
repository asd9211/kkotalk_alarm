package com.larn.alarm.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.larn.alarm.base.service.AuthService;
import com.larn.alarm.exception.ServiceException;
import com.larn.alarm.food.dto.RestaurantInfoDto;
import com.larn.alarm.food.service.RestaurantInfoService;
import com.larn.alarm.message.dto.DefaultMessageDto;
import com.larn.alarm.message.dto.ListMessageDto;
import com.larn.alarm.message.service.MessageService;
import com.larn.alarm.utils.StringUtils;
import com.larn.alarm.weather.dto.WeatherInfoDto;
import com.larn.alarm.weather.service.WeaterInfoService;

@Component
public class MessageScheduler {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MessageService msgService;

	@Autowired MessageSource msgSource;

	@Autowired
	WeaterInfoService weaterInfoService;

	@Autowired
	RestaurantInfoService restaurantInfoService;

	@Autowired
	AuthService authService;

	//@Scheduled(cron="0 00 07 * * ?")
	//@Scheduled(fixedRate = 10000)
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
			throw new ServiceException(msgSource.getMessage("token.info.notfound", null, Locale.getDefault()));
		}

	}

	//@Scheduled(cron="0 00 14 * * ?")
	//@Scheduled(fixedRate = 10000)
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
			throw new ServiceException(msgSource.getMessage("token.info.notfound", null, Locale.getDefault()));
		}
	}

	@Scheduled(fixedRate = 10000)
	public void testScheduler() {
		String naverMapUrl = "https://map.naver.com/v5/search/";

		String authToken = AuthService.getAuthToken();
		WeatherInfoDto weatherInfoDto = weaterInfoService.getWeatherInfo();
		String weatherStatus = weatherInfoDto.getWeatherStatus();
		List<ListMessageDto> msgDtoItemList = new ArrayList<>();
		ListMessageDto msgDto = new ListMessageDto();
		List<RestaurantInfoDto> restaurantsInfo = restaurantInfoService.getRestaurantInfoForWeather(weatherStatus);

		msgDto.setHeaderTitle("오늘의 맛집 추천");
		msgDto.setWebUrl(naverMapUrl);
		msgDto.setMobileUrl(naverMapUrl);

		for (RestaurantInfoDto restaurantInfo : restaurantsInfo) {
			ListMessageDto msgDtoItem = new ListMessageDto();
			String title = restaurantInfo.getTitle();

			msgDtoItem.setTitle(title);
			msgDtoItem.setDescription(restaurantInfo.getDescription());
			msgDtoItem.setImageUrl(restaurantInfo.getImageUrl());
			msgDtoItem.setImageWidth("50");
			msgDtoItem.setImageHeight("50");
			msgDtoItem.setWebUrl(naverMapUrl + title);	// 클릭시 naver 지도로 연결
			msgDtoItem.setMobileUrl(naverMapUrl + title);
			msgDtoItemList.add(msgDtoItem);
		}
		msgDto.setDtoList(msgDtoItemList);

		msgService.sendListMessage(authToken, msgDto);
	}


	//6시간마다 토큰 리프레시
	//@Scheduled(fixedRate = 5000)
	public void tokenRefrashSchduler() {
		authService.setAuthRefash();
	}
}
