package com.larn.alarm.base.dto;

import lombok.Data;

@Data
public class WeatherInfoDto {

	private String day;
	private String time;
	private String temp;
	private String weatherStatus;

}
