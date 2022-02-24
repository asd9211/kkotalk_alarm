package com.larn.alarm.base.repository;

import org.springframework.stereotype.Repository;

@Repository
public class AuthRepository {
	private static String authToken;
	private static String refrashAuthToken;

	public boolean saveAuthToken(String authToken) {
		AuthRepository.authToken = authToken;
		return AuthRepository.authToken.equals(authToken);
	}

	public boolean saveRefrashAuthToken(String refrashToken) {
		AuthRepository.refrashAuthToken = refrashToken;
		return AuthRepository.refrashAuthToken.equals(refrashToken);
	}

	public String getAuthToken() {
		return authToken;
	}

	public String getRefrashAuthToken() {
		return refrashAuthToken;
	}

}
