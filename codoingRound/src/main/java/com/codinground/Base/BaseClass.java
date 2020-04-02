package com.codinground.Base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.sun.javafx.PlatformUtil;

public class BaseClass {
	
	public WebDriver driver=null;
	
	@BeforeSuite
	public void initSuite() {
		 if (PlatformUtil.isMac()) {
	            System.setProperty("webdriver.chrome.driver", "chromedriver");
	        }
	        if (PlatformUtil.isWindows()) {
	            System.setProperty("webdriver.chrome.driver", "C:\\Users\\Nivash\\Downloads\\chromedriver.exe");
	        }
	        if (PlatformUtil.isLinux()) {
	            System.setProperty("webdriver.chrome.driver", "chromedriver_linux");
	        }
	}
	
	public void getDriver() {
		ChromeOptions options=new ChromeOptions();
		options.addArguments("--disable-notifications");
		driver=new ChromeDriver(options);
	}
	
	@AfterSuite
	public void wrapSuite() {
		driver.quit();
	}
}
