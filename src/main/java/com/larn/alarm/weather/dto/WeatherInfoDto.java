package com.larn.alarm.weather.dto;

import lombok.Data;

@Data
public class WeatherInfoDto {

	private String day;
	private String time;
	private String temp;
	private String weatherCode;
	private String weatherStatus;


}
