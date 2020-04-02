package com.codinground.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.codinground.Base.BaseClass;

public class Utility extends BaseClass{

	public void navigate() {
		driver.get("https://www.cleartrip.com/");
		driver.manage().window().maximize();
	}
	
	public void waitforelement(By by) {
		WebDriverWait driverWait=new WebDriverWait(driver,10);
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}
	
	public WebElement element(By by) {
		waitforelement(by);
		return driver.findElement(by);
	}
	
	public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
	
	public String getText(By by) {
		return element(by).getText();
	}
	
	public void set(By by,String value) {
		element(by).clear();
		element(by).sendKeys(value);	
	}
	
	public void waitFor(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void click(By by) {
		element(by).click();
	}
	
}
