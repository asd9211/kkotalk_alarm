package com.larn.alarm.weather;

import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.larn.alarm.weather.service.WeatherInfoService;

@SpringBootTest
public class weatherInfoServiceTest {


	@Autowired
	WeatherInfoService weatherInfoService;


	@Test
	public void 온도별_추천의상_가져오기() {
		//given
		int temp = 11;

		//when
		String clothes = weatherInfoService.getWearRecommandForWeather(temp);

		//then
		System.out.println(clothes);
		assertNotNull(clothes);

	}

}
