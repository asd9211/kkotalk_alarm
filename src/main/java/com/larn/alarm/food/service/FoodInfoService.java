package com.larn.alarm.food.service;

import java.util.ArrayList;
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

	public ListMessageDto getFoodInfoForWeather(String weatherCode) {
		String cliendId = msgSource.getMessage("naver.api.client_id", null, Locale.getDefault());
		String cliendSecret = msgSource.getMessage("naver.api.client_secret", null, Locale.getDefault());
		String[] rainyMenu = { "김치전", "칼국수", "부대찌개", "국수", "해물파전", "삼계탕", "설렁탕", "국물요리" }; // 나중에 DB에서 관리
		int maxItemLength = 3 ;
		
		ListMessageDto msgDto = new ListMessageDto();
		List<ListMessageDto> msgDtoItemList = new ArrayList<>();
		
		for (int i = 0; i < maxItemLength; i++) {
			int idx = new Random().nextInt(rainyMenu.length);

			HttpHeaders header = new HttpHeaders();
			header.set("X-Naver-Client-Id", cliendId);
			header.set("X-Naver-Client-Secret", cliendSecret);
			HttpEntity<?> searchRequestEntity = httpClientEntity(header, null);

			UriComponentsBuilder builder = UriComponentsBuilder
					.fromHttpUrl(NAVER_SEARCH_URL)
					.queryParam("sort", "comment")
					.queryParam("query", rainyMenu[idx])
					.queryParam("display", "1");

			ResponseEntity<String> response = httpRequest(builder.build().toUriString(), HttpMethod.GET, searchRequestEntity);
			JSONObject jsonData = new JSONObject(response.getBody());
			JSONArray items = (JSONArray) jsonData.get("items");
			
			for (Object item : items) {
				ListMessageDto msgDtoItem = new ListMessageDto();
				JSONObject itemObj = (JSONObject) item;
				msgDtoItem.setTitle(itemObj.getString("title").toString());
				msgDtoItem.setDescription(
						"주소 : " + itemObj.getString("address") + " 전화번호 : " + itemObj.getString("telephone"));
				msgDtoItem.setImageUrl("https://freesvg.org/img/bentolunch.png?w=150&h=150&fit=fill");
				msgDtoItem.setImageWidth("50");
				msgDtoItem.setImageHeight("50");
				msgDtoItemList.add(msgDtoItem);
			}
		}
		msgDto.setDtoList(msgDtoItemList);

		return msgDto;
	}
}
