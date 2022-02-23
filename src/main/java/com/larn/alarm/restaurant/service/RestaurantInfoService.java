package com.larn.alarm.restaurant.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
import com.larn.alarm.restaurant.dto.RestaurantInfoDto;

@Service
public class RestaurantInfoService extends HttpCallService {
	private static final String NAVER_SEARCH_URL = "https://openapi.naver.com/v1/search/local.json?";

	@Autowired
	MessageSource msgSource;

	public List<RestaurantInfoDto> getRestaurantInfoForWeather(String weatherStatus) {
		String cliendId = msgSource.getMessage("naver.api.client_id", null, Locale.getDefault());
		String cliendSecret = msgSource.getMessage("naver.api.client_secret", null, Locale.getDefault());

		List<RestaurantInfoDto> restaurantInfoDtoList = new ArrayList<>();
		// 추후 JPA 구현시 DB에 모든 메뉴 입력 후 랜덤으로 추출
		List<String> dailyMenu = new ArrayList<>(
				Arrays.asList( "돈까스", "김치찌개", "초밥", "김밥", "햄버거", "짜장면", "보쌈", "제육볶음"));
		List<String> rainyMenu = new ArrayList<>(
				Arrays.asList( "김치전", "칼국수", "부대찌개", "국수", "해물파전", "삼계탕", "설렁탕", "국물요리"));


		List<String> todayMenu;
		int maxItemLength = 3 ;

		if(weatherStatus.equals("0")) { // 이상없음
			todayMenu = dailyMenu;
		}else {						  // 비 or 눈 or 소나기
			todayMenu = rainyMenu;
		}

		for (int i = 0; i < maxItemLength; i++) {
			int idx = new Random().nextInt(todayMenu.size());

			HttpHeaders header = new HttpHeaders();
			header.set("X-Naver-Client-Id", cliendId);
			header.set("X-Naver-Client-Secret", cliendSecret);
			HttpEntity<?> searchRequestEntity = httpClientEntity(header, null);

			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(NAVER_SEARCH_URL)
					.queryParam("sort", "comment")
					.queryParam("query", todayMenu.get(idx))
					.queryParam("display", "1");

			ResponseEntity<String> response = httpRequest(builder.build().toUriString(), HttpMethod.GET, searchRequestEntity);
			JSONObject jsonData = new JSONObject(response.getBody());
			JSONArray items = (JSONArray) jsonData.get("items");

			for (Object item : items) {
				RestaurantInfoDto restaurntInfoDto = new RestaurantInfoDto();
				JSONObject itemObj = (JSONObject) item;

				restaurntInfoDto.setTitle(itemObj.getString("title"));
				restaurntInfoDto.setAddress(itemObj.getString("address"));
				restaurntInfoDto.setDescription("주소 : " + itemObj.getString("address") + " 전화번호 : " + itemObj.getString("telephone"));
				restaurntInfoDto.setImageUrl("https://freesvg.org/img/bentolunch.png?w=150&h=150&fit=fill");

				restaurantInfoDtoList.add(restaurntInfoDto);
			}
		}

		return restaurantInfoDtoList;
	}
}
