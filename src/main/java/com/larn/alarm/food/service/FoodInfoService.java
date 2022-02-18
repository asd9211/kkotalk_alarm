package com.larn.alarm.food.service;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.larn.alarm.base.service.HttpCallService;
import com.larn.alarm.exception.ServiceException;

@Service
public class FoodInfoService extends HttpCallService{
	private static final String NAVER_SEARCH_URL = "https://openapi.naver.com/v1/search/local.json?";

	@Autowired MessageSource msgSource;

	public String getFoodInfoForWeather(String weatherCode) {
		String cliendId = msgSource.getMessage("naver.api.client_id", null, Locale.getDefault());
		String cliendSecret = msgSource.getMessage("naver.api.client_secret", null, Locale.getDefault());

		HttpHeaders header = new HttpHeaders();
		header.set("X-Naver-Client-Id", cliendId);
		header.set("X-Naver-Client-Secret", cliendSecret);
    	HttpEntity<?> searchRequestEntity = httpClientEntity(header, null);

    	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(NAVER_SEARCH_URL)
				.queryParam("sort", "comment")
				.queryParam("query", "종로역 설렁탕 맛집")
				.queryParam("display", "5");

		ResponseEntity<String> response = httpRequest(builder.build().toUriString(), HttpMethod.GET, searchRequestEntity);
		JSONObject jsonData = new JSONObject(response.getBody());
		JSONArray items = (JSONArray)jsonData.get("items");

		if (items.length() == 0)
			throw new ServiceException("날씨 API에서 정보를 받는데 실패했습니다.");

		for (Object item : items) {
			JSONObject itemObj = (JSONObject) item;
			String category = itemObj.get("category").toString();
			if (category.equals("TMP")) {


			}
		}
		System.out.println(jsonData.toString());
		return null;
	}
}
