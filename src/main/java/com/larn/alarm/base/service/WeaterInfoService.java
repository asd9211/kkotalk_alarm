package com.larn.alarm.base.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.larn.alarm.base.dto.WeaterInfoDto;

@Service
public class WeaterInfoService extends HttpCallService {
	private static final String WHEATER_INFO_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?";

	@Autowired MessageSource msgSource;

	public WeaterInfoDto getWeaterInfo() {
		LocalDate now = LocalDate.now();
		Map<String,String> paramsMap = new HashMap<>();
		String url = "";
		String parameters = "";
		String todayDate = now.toString().replaceAll("-", "");
		String apiKey= msgSource.getMessage("weather.api.key", null, Locale.getDefault());
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(WHEATER_INFO_API_URL)
		        .queryParam("serviceKey", apiKey)
		        .queryParam("numOfRows", "10")
		        .queryParam("pageNo", "1")
		        .queryParam("base_date", todayDate)
		        .queryParam("base_time", "0500")
		        .queryParam("nx", "55")
		        .queryParam("ny", "127")
		        .queryParam("dataType", "json");

		System.out.println(builder.build(true).toUri());
    	WeaterInfoDto weaterInfoDto = new WeaterInfoDto();
        ResponseEntity<String> response = httpRequest(builder.build(true).toUri(), HttpMethod.GET, null);
        JSONObject jsonData = new JSONObject(response.getBody());
        return weaterInfoDto;
	}
}
