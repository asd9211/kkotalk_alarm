package com.larn.alarm.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import com.larn.alarm.base.dto.AuthDto;
import com.larn.alarm.base.service.AuthService;

@SpringBootTest
class AuthServiceTest {

	public class AuthServiceMock extends AuthService {
		public AuthServiceMock() {
			super();
		}
		@Override
		public AuthDto getKakaoAuthToken(String code) {
			// TODO Auto-generated method stub
			AuthDto expectDto = new AuthDto();
			expectDto.setAccessToken("123");
			expectDto.setRefrashToken("123");
			return expectDto;
		}
	}

	@Test
	void 토큰_받아오기() {
		//given
		AuthServiceMock authServiceMock = new AuthServiceMock();
		String code = "test";
		AuthDto expectAuthDto = new AuthDto();
		expectAuthDto.setAccessToken("123");
		expectAuthDto.setRefrashToken("123");


		//when
		AuthDto authDto = authServiceMock.getKakaoAuthToken(code);

		//then
		assertEquals(authDto.getAccessToken(), expectAuthDto.getAccessToken());
	}
}
