package com.larn.alarm.news;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.larn.alarm.news.dto.NewsInfoDto;
import com.larn.alarm.news.service.NewsService;

@SpringBootTest
class NewsServiceTest {

	@Autowired
	NewsService newsService;

	@Test
	public void 뉴스정보_받아오기() {
		//given
		List<String> expectTitleList = new ArrayList<>();

		expectTitleList.add("정치");
		expectTitleList.add("경제");
		expectTitleList.add("사회");
		expectTitleList.add("생활문화");
		expectTitleList.add("세계");
		expectTitleList.add("과학");

		//when
		List<NewsInfoDto> newsInfoList = newsService.getNewsInfo();

		//then
		int idx =  0;
		for(int i = 1 ; i <= newsInfoList.size(); i++) {
			if(i % 3 == 0) {
				assertEquals(newsInfoList.get(i-1).getTitle(), "분야 : " + expectTitleList.get(idx));
				idx++;
			}
		}
	}

}
