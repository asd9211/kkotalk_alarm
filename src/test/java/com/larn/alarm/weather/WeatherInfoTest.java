package com.larn.alarm.weather;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.larn.alarm.weather.dto.WeatherInfoDto;
import com.larn.alarm.weather.service.WeatherInfoService;

@SpringBootTest
class WeatherInfoTest {

	@Autowired
	WeatherInfoService weatherInfoService;

	@Test
	public void 날씨정보_받아오기() {
		//API MOCK Test로 추후 변경
		//given
		String expectTemp = "-10";
		String expectWeatherCode = "0";

		//when
		WeatherInfoDto weatherInfo = weatherInfoService.getWeatherInfo();

		//then
		assertEquals(weatherInfo.getTemp(), expectTemp);
		assertEquals(weatherInfo.getWeatherCode(), expectWeatherCode);
	}

	@Test
	public void 의상_추천받기() {
		//given
		int todayTemp = 30;
		String expectWear = "나시티, 반바지, 민소매, 원피스";

		//when
		String recommandWear = weatherInfoService.getWearRecommandForWeather(todayTemp);

		//then
		assertEquals(recommandWear, expectWear);
	}




}
