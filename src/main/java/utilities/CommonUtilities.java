package utilities;

import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.Scenario;

public class CommonUtilities {

	private WebDriver driver;
	private WebDriverWait wait;

	public CommonUtilities(WebDriver driver) {
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	// Click
	public void click(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
		element.click();
	}

	// SendKey
	public void type(WebElement element, String text) {
		wait.until(ExpectedConditions.visibilityOf(element));
		element.clear();
		element.sendKeys(text);
	}

	// Clear
	public void clear(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
		element.clear();
	}

	// Screenshot Method
	public static void takeScreenshot(WebDriver driver, Scenario scenario, String stepName) {

		try {
			byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			String screenshotName = "Step Passed -" + stepName;
			scenario.attach(screenshot, "image/png", screenshotName);
			// Allure.addAttachment(screenshotName, stepName, new
			// ByteArrayInputStream(screenshot), screenshotName);
			System.out.println("Screenshot attached to Cucumber report: " + screenshotName);
		} catch (Exception e) {
			System.out.println("Screenshot failed: " + e.getMessage());
		}
	}

}
