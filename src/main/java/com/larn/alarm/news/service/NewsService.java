package com.larn.alarm.news.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class NewsService {

	public void getNewsInfo() {

		Path path = Paths.get("C:\\drivers\\chromedriver.exe"); 

		System.setProperty("webdriver.chrome.driver", path.toString());

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized"); 
		options.addArguments("--disable-popup-blocking"); 
		options.addArguments("--disable-default-apps"); 

		ChromeDriver driver = new ChromeDriver(options);

		int sessionId = 100;
		List<String> aList = new ArrayList<>();
		aList.add("정치");
		aList.add("경제");
		aList.add("사회");
		aList.add("생활문화");
		aList.add("세계");
		aList.add("과학");
		
		for (int i = 0; i < 6; i++) {
		
			driver.get("https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1="
					+ sessionId);
			sessionId++;
			List<WebElement> page1_title = driver.findElementsByClassName("cluster_text");
			System.out.println("분야별 == > "+ aList.get(i)+ "   " + sessionId);
			
			int len = page1_title.size();
			for (int j = 0; j < len; j++) {
				System.out.println(page1_title.get(j).getText());
			}
		}

		driver.close();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			driver.quit();
		}
	}
	
	public static void main(String[] args) {
		NewsService ns = new NewsService();
		ns.getNewsInfo();
	}
}
