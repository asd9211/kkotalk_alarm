package com.larn.alarm.weather.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name="Recommandwear")
public class RecommandWearDto {


	private long minTemp;
	private long maxTemp;
	@Id
	private String wear;
}
