package com.larn.alarm.weather.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.larn.alarm.base.service.HttpCallService;
import com.larn.alarm.exception.ServiceException;
import com.larn.alarm.weather.dto.WeatherInfoDto;

@Service
public class WeaterInfoService extends HttpCallService {
	private static final String WHEATER_INFO_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?";

	@Autowired MessageSource msgSource;

	public WeatherInfoDto getWeaterInfo() {
		LocalDate now = LocalDate.now();
		String todayDate = now.toString().replaceAll("-", "");
		String apiKey= msgSource.getMessage("weather.api.key", null, Locale.getDefault());
		String rows = "10";
		String pageNo = "1";
		String baseTime = "0500";
		String dataType = "json";

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(WHEATER_INFO_API_URL)
		        .queryParam("serviceKey", apiKey)
		        .queryParam("numOfRows", rows)
		        .queryParam("pageNo", pageNo)
		        .queryParam("base_date", todayDate)
		        .queryParam("base_time", baseTime)
		        .queryParam("nx", "55")
		        .queryParam("ny", "127")
		        .queryParam("dataType", dataType);

    	WeatherInfoDto weaterInfoDto = new WeatherInfoDto();
        ResponseEntity<String> response = httpRequest(builder.build(true).toUri(), HttpMethod.GET, null);
        JSONObject jsonData = new JSONObject(response.getBody());

        JSONArray items = jsonData.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
        if(items.length() == 0)
        	throw new ServiceException("날씨 API에서 정보를 받는데 실패했습니다.");

        for(Object item : items) {
        	JSONObject itemObj = (JSONObject)item;
        	String category = itemObj.get("category").toString();
        	if(category.equals("TMP")) {
        		weaterInfoDto.setTemp(itemObj.get("fcstValue").toString());
        		weaterInfoDto.setDay(itemObj.get("baseDate").toString());
        		weaterInfoDto.setTime(itemObj.get("fcstTime").toString());
        	}else if(category.equals("PTY")) {
        		String weatherCode = itemObj.get("fcstValue").toString();
        		String weatherStatus = "";
        		switch (weatherCode) {
					case "0": weatherStatus = "이상 없음";
						break;
					case "1": weatherStatus = "비";
						break;
					case "2": weatherStatus = "눈/비";
						break;
					case "3": weatherStatus = "눈";
						break;
					case "4": weatherStatus = "소나기";
						break;
					default:
						break;
				}
        		weaterInfoDto.setWeatherStatus(weatherStatus);
        	}
        }


        return weaterInfoDto;
	}
}
