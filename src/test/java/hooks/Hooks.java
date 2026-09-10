package hooks;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilities.CommonUtilities;
import utilities.ConfigReader;

public class Hooks {

	private WebDriver driver;
	String browser;
	String url;

	@Before(order = 1)
	public void setUp() {
		url = ConfigReader.getProperty("url");
		browser = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter("browser");
		if (browser == null || browser.isBlank()) {
			browser = System.getProperty("browser", "chrome");
		}

		System.out.println("Browser: " + browser);
		System.out.println("Url: " + url);

		DriverFactory.initBrowser(browser, "--disable-notifications", "--disable-popup-blocking",
				"--disable-save-password-bubble");
		driver = DriverFactory.getDriver();
		driver.get(url);
		System.out.println("Browser opened");
	}

	@AfterStep
	public void afterSteps(Scenario scenario) {
		if (!scenario.isFailed()) {
			CommonUtilities.takeScreenshot(driver, scenario, "Step Passed");
		}
	}

	@After(order = 0)
	public void tearDown(Scenario scenario) {

		if (scenario.isFailed()) {
			CommonUtilities.takeScreenshot(driver, scenario, "FAILED");
		}
		DriverFactory.closeBrowser();
		System.out.println("Browser Closed");
	}

}
