package com.larn.alarm.base.service;

import com.larn.alarm.base.dto.AuthDto;

public interface AuthServiceInterface {
	public boolean saveAuthToken(String code) ;
	public boolean saveAuthRefash() ;
	public AuthDto getKakaoAuthToken(String code);
	public AuthDto getRefrashToken();

}
