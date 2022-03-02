package com.larn.alarm.weather.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name="Recommandwear")
public class RecommandWearDto {

	@Id
	private long minTemp;
	private long maxTemp;
	private String wear;
}
