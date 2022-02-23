package com.larn.alarm.weather.service;

import java.time.LocalDate;
import java.util.Locale;

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
import com.larn.alarm.weather.dto.WearRecommandDto;
import com.larn.alarm.weather.dto.WeatherInfoDto;

@Service
public class WeaterInfoService extends HttpCallService {
	private static final String WHEATER_INFO_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?";

	@Autowired
	MessageSource msgSource;

	public WeatherInfoDto getWeatherInfo() {
		LocalDate now = LocalDate.now();
		String todayDate = now.toString().replaceAll("-", "");
		String apiKey = msgSource.getMessage("weather.api.key", null, Locale.getDefault());
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

		JSONArray items = jsonData.getJSONObject("response").getJSONObject("body").getJSONObject("items")
				.getJSONArray("item");
		if (items.isEmpty())
			throw new ServiceException("날씨 API에서 정보를 받는데 실패했습니다.");

		for (Object item : items) {
			JSONObject itemObj = (JSONObject) item;
			String category = itemObj.get("category").toString();
			if (category.equals("TMP")) {
				weaterInfoDto.setTemp(itemObj.get("fcstValue").toString());
				weaterInfoDto.setDay(itemObj.get("baseDate").toString());
				weaterInfoDto.setTime(itemObj.get("fcstTime").toString());
			} else if (category.equals("PTY")) {
				String weatherCode = itemObj.get("fcstValue").toString();
				String weatherStatus = "";
				switch (weatherCode) {
				case "0":
					weatherStatus = "이상 없음";
					break;
				case "1":
					weatherStatus = "비";
					break;
				case "2":
					weatherStatus = "눈/비";
					break;
				case "3":
					weatherStatus = "눈";
					break;
				case "4":
					weatherStatus = "소나기";
					break;
				default:
					break;
				}
				weaterInfoDto.setWeatherStatus(weatherStatus);
			}
		}

		return weaterInfoDto;
	}

	// 추후 DB에 minTemp,maxTemp별로 옷차림 넣고 where temp 로 가져오게 변경
	public String getWearRecommandForWeather(int temp) {
		String recommandWear = "";
		// WearRecommandDto wearRecommandDto = new WearRecommandDto();
		// 27 ~ : 나시티, 반바지, 민소매, 원피스
		// 23 ~ 26 : 반팔, 얇은셔츠 ,얇은 긴팔, 반바지, 면바지
		// 20~22 : 긴팔티, 가디건, 후드티, 면바지, 슬랙스, 청바지
		// 17~19 : 니트, 가디건, 후드티, 맨투맨, 청바지, 면바지, 슬랙스, 원피스
		// 12~18 : 자켓, 셔츠, 가디건, 간절기 야상
		// 10~11 : 트렌치코트, 간절기 야상, 여러겹 껴입기
		// 6~9 : 코트, 가죽자켓
		// ~5 : 겨울옷 ( 야상, 패딩, 목도리 등등 )
		if(temp > 27) { // 임시로 if 처리
			recommandWear = "나시티, 반바지, 민소매, 원피스";
		}else if( 26 > temp && temp > 23) {
			recommandWear = "반팔, 얇은셔츠 ,얇은 긴팔, 반바지, 면바지";
		}else if( 26 > temp && temp > 23) {
			recommandWear = "반팔, 얇은셔츠 ,얇은 긴팔, 반바지, 면바지";
		}else if( 22 > temp && temp > 20) {
			recommandWear = "긴팔티, 가디건, 후드티, 면바지, 슬랙스, 청바지";
		}else if( 19 > temp && temp > 17) {
			recommandWear = "니트, 가디건, 후드티, 맨투맨, 청바지, 면바지, 슬랙스, 원피스";
		}else if( 11 > temp && temp > 10) {
			recommandWear = "트렌치코트, 간절기 야상, 여러겹 껴입기";
		}else if( 9 > temp && temp > 6) {
			recommandWear = "코트, 가죽자켓";
		}else if( 5 > temp) {
			recommandWear = "겨울옷 ( 야상, 패딩, 목도리 등등 )";
		}

		return recommandWear;
	}
}
