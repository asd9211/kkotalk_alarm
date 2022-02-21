package com.larn.alarm.food.service;

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
import com.larn.alarm.message.dto.ListMessageDto;

@Service
public class FoodInfoService extends HttpCallService {
	private static final String NAVER_SEARCH_URL = "https://openapi.naver.com/v1/search/local.json?";

	@Autowired
	MessageSource msgSource;

	public ListMessageDto getFoodInfoForWeather(String weatherStatus) {
		String cliendId = msgSource.getMessage("naver.api.client_id", null, Locale.getDefault());
		String cliendSecret = msgSource.getMessage("naver.api.client_secret", null, Locale.getDefault());
		// 추후 JPA 구현시 DB에 모든 메뉴 입력 후 랜덤으로 추출
		List<String> dailyMenu = new ArrayList<>(
				Arrays.asList( "돈까스", "김치찌개", "초밥", "김밥", "햄버거", "짜장면", "보쌈", "제육볶음"));      
		List<String> rainyMenu = new ArrayList<>(
				Arrays.asList( "김치전", "칼국수", "부대찌개", "국수", "해물파전", "삼계탕", "설렁탕", "국물요리")); 
		
		ListMessageDto msgDto = new ListMessageDto();
		List<ListMessageDto> msgDtoItemList = new ArrayList<>();
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
				ListMessageDto msgDtoItem = new ListMessageDto();
				JSONObject itemObj = (JSONObject) item;
				String title = itemObj.getString("title");
				
				msgDtoItem.setTitle(title);
				msgDtoItem.setDescription(
						"주소 : " + itemObj.getString("address") + " 전화번호 : " + itemObj.getString("telephone"));
				msgDtoItem.setImageUrl("https://freesvg.org/img/bentolunch.png?w=150&h=150&fit=fill");
				msgDtoItem.setImageWidth("50");
				msgDtoItem.setImageHeight("50");
				msgDtoItem.setWebUrl("https://map.naver.com/v5/search/" + title);	// 클릭시 naver 지도로 연결
				msgDtoItem.setMobileUrl("https://map.naver.com/v5/search/" + title);
				msgDtoItemList.add(msgDtoItem);
			}
		}
		msgDto.setDtoList(msgDtoItemList);

		return msgDto;
	}
}
