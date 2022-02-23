package com.larn.alarm.news.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		int maxNewsCount = 3;
		String naverNewsUrl = "https://news.naver.com/main/main.naver?";
		Path path = Paths.get("src","main","resources","utils","chromedriver.exe");

		System.setProperty("webdriver.chrome.driver", path.toString());

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-default-apps");

		ChromeDriver driver = new ChromeDriver(options);

		List<NewsInfoDto> newsInfoList = new ArrayList<>();
		Map<String,String> category = new HashMap<>();
		category.put("100", "정치");
		category.put("101", "경제");
		category.put("102", "사회");
		category.put("103", "생활문화");
		category.put("104", "세계");
		category.put("105", "과학");

		for(Entry<String, String> entry : category.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			String param = "mode=LSD&mid=shm&sid1=" + key;

			driver.get(naverNewsUrl + param);

			for(int k=1; k <= maxNewsCount; k++) {
				NewsInfoDto newsInfo = new NewsInfoDto();
				try {
					WebElement element = driver.findElementByXPath("//*[@id=\"main_content\"]/div/div[2]/div[1]/div["+k+"]/div["+((key.equals("100")) ? "1" : "2") +"]/ul/li[1]/div[2]/a");
					newsInfo.setTitle("분야 : " +  value);
					newsInfo.setDescription(element.getText());
					newsInfo.setUrl(element.getAttribute("href"));
					newsInfoList.add(newsInfo);
				}catch (Exception e) {
					newsInfo.setTitle("뉴스 크롤링 error! 분야 : " + value);
					newsInfo.setDescription(e.getMessage());
					newsInfo.setUrl("");
					newsInfoList.add(newsInfo);
				}

			}
		}

		driver.close();
		driver.quit();
		return newsInfoList;
	}
}
