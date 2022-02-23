package com.larn.alarm.news.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.larn.alarm.news.dto.NewsInfoDto;

@Service
public class NewsService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public List<NewsInfoDto> getNewsInfo() {

		String naverNewsUrl = "https://news.naver.com/main/main.naver?";
		Path path = Paths.get("src","main","resources","utils","chromedriver.exe");

		System.setProperty("webdriver.chrome.driver", path.toString());

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-default-apps");

		ChromeDriver driver = new ChromeDriver(options);

		List<String> category = new ArrayList<>(
				Arrays.asList( "정치", "경제", "사회", "생활문화", "세계", "과학"));
		List<NewsInfoDto> newsInfoList = new ArrayList<>();
		int sessionId = 100;

		for (int i = 0; i <= 5; i++) {
			String param = "mode=LSD&mid=shm&sid1=" + sessionId;
			driver.get( naverNewsUrl + param);

			for(int k=1; k <= 3; k++) {
				NewsInfoDto newsInfo = new NewsInfoDto();
				try {
					WebElement element = driver.findElementByXPath("//*[@id=\"main_content\"]/div/div[2]/div[1]/div["+k+"]/div["+((i == 0) ? "1" : "2") +"]/ul/li[1]/div[2]/a");
					newsInfo.setTitle("분야  : " +  category.get(i));
					newsInfo.setDescription(element.getText());
					newsInfo.setUrl(element.getAttribute("href"));
					newsInfoList.add(newsInfo);
				}catch (Exception e) {
					newsInfo.setTitle("뉴스 크롤링 error! 분야 : " + category.get(i));
					newsInfo.setDescription(e.getMessage());
					newsInfo.setUrl("");
					newsInfoList.add(newsInfo);
				}

			}
			sessionId++;
		}
		driver.close();
		driver.quit();
		return newsInfoList;
	}
}
