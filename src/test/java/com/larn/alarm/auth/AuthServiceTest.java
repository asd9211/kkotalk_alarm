package com.larn.alarm.auth;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.larn.alarm.base.service.AuthService;

@SpringBootTest
class AuthServiceTest {

	@Autowired
	AuthService authService;

	@Test
	void 토큰_받아오기() {
		//AuthService클래스에 authRequest하는 메서드를 분기해서 테스트해야 할 것 같음.
	}

}
