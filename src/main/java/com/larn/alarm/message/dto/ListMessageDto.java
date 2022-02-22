package com.larn.alarm.message.dto;

import java.util.List;

import lombok.Data;

@Data
public class ListMessageDto {

	private String headerTitle;
	private String title;
	private String description;
	private String imageUrl;
	private String imageWidth;
	private String imageHeight;
	private String webUrl;
	private String mobileUrl;
	private List<ListMessageDto> dtoList;

}

