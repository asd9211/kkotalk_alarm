package com.larn.alarm.weather.service;

import java.time.LocalDate;
import java.util.List;
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
import com.larn.alarm.weather.dto.RecommandWearDto;
import com.larn.alarm.weather.dto.WeatherInfoDto;
import com.larn.alarm.weather.repository.RecommandWearRepository;

@Service
public class WeatherInfoService extends HttpCallService {
	private static final String WHEATER_INFO_API_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?";

	@Autowired
	MessageSource msgSource;

	@Autowired
	RecommandWearRepository recommandWearRepository;
	public WeatherInfoDto getWeatherInfo() {
		LocalDate now = LocalDate.now();
		String todayDate = now.toString().replaceAll("-", "");
		String apiKey = msgSource.getMessage("weather.api.key", null, Locale.getDefault());
		String rows = "10";
		String pageNo = "1";
		String baseTime = "0500";
		String dataType = "json";
		WeatherInfoDto weaterInfoDto = new WeatherInfoDto();

		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(WHEATER_INFO_API_URL)
				.queryParam("serviceKey", apiKey)
				.queryParam("numOfRows", rows)
				.queryParam("pageNo", pageNo)
				.queryParam("base_date", todayDate)
				.queryParam("base_time", baseTime)
				.queryParam("nx", "55")
				.queryParam("ny", "127")
				.queryParam("dataType", dataType);

		ResponseEntity<String> response = httpRequest(builder.build(true).toUri(), HttpMethod.GET, null);
		JSONObject jsonData = new JSONObject(response.getBody());

		JSONArray items = jsonData.getJSONObject("response").getJSONObject("body").getJSONObject("items")
				.getJSONArray("item");
		if (items.isEmpty())
			throw new ServiceException("?????? API?????? ????????? ????????? ??????????????????.");

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
					weatherStatus = "?????? ??????";
					break;
				case "1":
					weatherStatus = "???";
					break;
				case "2":
					weatherStatus = "???/???";
					break;
				case "3":
					weatherStatus = "???";
					break;
				case "4":
					weatherStatus = "?????????";
					break;
				default:
					break;
				}
				weaterInfoDto.setWeatherCode(weatherCode);
				weaterInfoDto.setWeatherStatus(weatherStatus);
			}
		}

		return weaterInfoDto;
	}


	public String getWearRecommandForWeather(int temp) {
		StringBuilder sb = new StringBuilder();
		String separator = ", ";
		List<RecommandWearDto> recommandWearList = recommandWearRepository.findBytempQuery(temp);
		for(RecommandWearDto recommandWearDto : recommandWearList) {
			sb.append(recommandWearDto.getWear());
			sb.append(separator);
		}
		String clothes = sb.toString();
		clothes = clothes.replaceAll(separator + "$", "");
		return clothes;
	}
}
