package com.larn.alarm.food.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

import com.larn.alarm.base.service.AuthService;
import com.larn.alarm.base.service.HttpCallService;
import com.larn.alarm.exception.ServiceException;

@Service
public class FoodInfoService extends HttpCallService{
	private static final String NAVER_SEARCH_URL = "https://openapi.naver.com/v1/search/local.json?";
	private static final String MSG_SEND_SERVICE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

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

	 	JSONObject linkObj = new JSONObject();
    	linkObj.put("web_url", "");
    	linkObj.put("mobile_web_url", "");

    	JSONArray contentsArray = new JSONArray();

    	for(int i = 0 ; i < 5 ; i++) {
        	JSONObject contentsObj = new JSONObject();
	    	contentsObj.put("title", "곰탕집");
	    	contentsObj.put("description", "주소");
	    	contentsObj.put("image_url", "곰탕집");
	    	contentsObj.put("image_width", "50");
	    	contentsObj.put("image_height", "50");
	    	contentsObj.put("link", linkObj);
	    	contentsArray.put(contentsObj);
	    }

    	JSONObject templateObj = new JSONObject();
    	templateObj.put("object_type", "list");
    	templateObj.put("header_title", "맛집 추천");
    	templateObj.put("header_link", linkObj);
    	templateObj.put("contents", contentsArray);

    	Map<String,String> parameters = new HashMap<>();
    	parameters.put("template_object", "");
    	String body = "";

    	for(String key : parameters.keySet()) {
        	body += key + "=" + templateObj.toString();
        }

    	// 서비스 따로 태워야 함. 임시.
    	header = new HttpHeaders();
    	header.set("Content-Type", "application/" + APP_TYPE_URL_ENCODED);
 		header.set("Authorization", "Bearer " + AuthService.getAuthToken());

 		HttpEntity<?> messageRequestEntity = httpClientEntity(header, body);
        ResponseEntity<String> res = httpRequest(MSG_SEND_SERVICE_URL, HttpMethod.POST, messageRequestEntity);
        System.out.println(res.getBody().toString());

		if (items.length() == 0)
			throw new ServiceException("네이버 검색 API에서 정보를 받는데 실패했습니다.");

		for (Object item : items) {
			JSONObject itemObj = (JSONObject) item;
			String title = itemObj.get("title").toString();
			String address = itemObj.get("roadAddress").toString();
			String category = itemObj.get("category").toString();

		}
		System.out.println(jsonData.toString());
		return null;
	}
}
