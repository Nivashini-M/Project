import com.codinground.pages.HomePage;
import com.sun.javafx.PlatformUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class FlightBookingTest extends HomePage {
	@BeforeClass
	public void initClass() {
		getDriver();
		navigate();
	}

    @Test
    public void testThatResultsAppearForAOneWayJourney() {
    	click(oneWay);
        set(origin,"Bangalore");
        selectOrigin();
        set(destination,"Delhi");
        selectDestination();
        selectDate();
        search();
        waitFor(5000);
        
        //verify that result appears for the provided journey search
        Assert.assertTrue(isElementPresent(resultSummary));
    }
    
    
   // @Test
    public void shouldThrowAnErrorIfSignInDetailsAreMissing() {
	    click(yourTrips);
	    click(signIn);
	    switchToSignInFrame();
	    click(signInButton);
	    String error_message = getText(error);
	    Assert.assertTrue(error_message.contains("There were errors in your submission"));
	    
    }
    
    @AfterClass
    public void wrapUp() {
    	driver.close();
    }
}
