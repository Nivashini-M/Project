package com.codinground.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.codinground.Utils.*;

public class HomePage extends Utility{

	public By oneWay=By.id("OneWay");
	public By origin=By.id("FromTag");
	public By destination=By.id("ToTag");
	public By date=By.xpath("//*[@id='ui-datepicker-div']/div[1]/table/tbody/tr[3]/td[7]/a");
	public By search=By.id("SearchBtn");
	public By resultSummary=By.xpath("//div[contains(@class,'resultContainer')]");
	public By yourTrips=By.linkText("Your trips");
	public By signIn=By.id("SignIn");//signIn option of YourTrips
	public By signInButton=By.id("signInButton");
	public By error=By.id("errors1");
	public By hotelLink=By.linkText("Hotels");
	
	
	
	public void switchToSignInFrame() {
		int frameSize=driver.findElements(By.tagName("iframe")).size();
		if(frameSize > 0) {
		     driver.switchTo().frame("modal_window");
		     }
	}
	
	public void selectOrigin() {
		waitFor(5000);
		List<WebElement> originOptions = driver.findElement(By.id("ui-id-1")).findElements(By.tagName("li"));
		originOptions.get(0).click();
	}

	public void selectDestination() {
		waitFor(5000);
		List<WebElement> destinationOptions = driver.findElement(By.id("ui-id-2")).findElements(By.tagName("li"));
	    destinationOptions.get(0).click();
	}

	public void selectDate() {
		click(date);
	}
    
	public void search() {
		click(search);
	}
    
}
