package com.larn.alarm.weather.dto;

import lombok.Data;

@Data
public class WearRecommandDto {

	private int minTemp;
	private int maxTemp;
	private String wear;
}
