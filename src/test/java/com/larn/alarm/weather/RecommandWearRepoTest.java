package com.larn.alarm.weather;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.larn.alarm.weather.dto.RecommandWearDto;
import com.larn.alarm.weather.repository.RecommandWearRepository;

@SpringBootTest
public class RecommandWearRepoTest {

	@Autowired
	RecommandWearRepository recommandWearRepository;

	@Test
	public void 온도별_추천옷_불러오기() {
		//given
		int temp = 28;

		//when
		List<RecommandWearDto> recommandWear = recommandWearRepository.findBytempQuery(28);

		//then
		recommandWear.forEach(wear ->{
			System.out.println(wear);
		});
	}

}
