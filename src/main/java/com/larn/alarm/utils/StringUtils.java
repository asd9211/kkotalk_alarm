package com.larn.alarm.utils;

import org.springframework.lang.Nullable;

public class StringUtils {

	public static boolean isEmpty(@Nullable Object str) {
		return (str == null || "".equals(str));
	}
}
