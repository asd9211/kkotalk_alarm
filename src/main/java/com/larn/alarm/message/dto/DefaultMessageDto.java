package com.larn.alarm.message.dto;

import java.util.Map;

import lombok.Data;

@Data
public class DefaultMessageDto {
	private String objType;
	private String text;
	private String webUrl;
	private String mobileUrl;
	private String btnTitle;


}
