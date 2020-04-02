import com.sun.javafx.PlatformUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SignInTest {

    @Test
    public void shouldThrowAnErrorIfSignInDetailsAreMissing() {

    	ChromeOptions options = new ChromeOptions();
    	options.addArguments("--disable-notifications");
        setDriverPath();
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.cleartrip.com/");
        driver.manage().window().maximize();
        waitFor(5000);
        //Actions actions=new Actions(driver);
        //actions.sendKeys(Keys.ESCAPE).build().perform();
        
        driver.findElement(By.linkText("Your trips")).click();
        driver.findElement(By.id("SignIn")).click();
        int size=driver.findElements(By.tagName("iframe")).size();
        System.out.println("size="+size);
        if(size > 0) {
        driver.switchTo().frame("modal_window");
        }
        waitFor(5000);
        driver.findElement(By.id("signInButton")).click();

        String errors1 = driver.findElement(By.id("errors1")).getText();
        Assert.assertTrue(errors1.contains("There were errors in your submission"));
        driver.quit();
    }

    private void waitFor(int durationInMilliSeconds) {
        try {
            Thread.sleep(durationInMilliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void setDriverPath() {
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


}
