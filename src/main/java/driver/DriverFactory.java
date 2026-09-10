package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	public static void initBrowser(String browser, String... arguments) {
		if (browser.equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();

			java.util.Map<String, Object> prefs = new java.util.HashMap<>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			prefs.put("profile.password_manager_leak_detection", false);

			options.setExperimentalOption("prefs", prefs);

			for (String argument : arguments) {
				options.addArguments(argument);
			}
			driver.set(new ChromeDriver(options));
			getDriver().manage().window().maximize();
			System.out.println(
					"Thread Name : " + Thread.currentThread().getName() + " | Thread ID : " + Thread.currentThread());

			// WebDriverManager.chromedriver().setup();

			// driver = new ChromeDriver(options);
			// driver.manage().window().maximize();
		} else {
			throw new IllegalArgumentException("Unsupported browser" + browser);
		}
	}

	public static WebDriver getDriver() {
		return driver.get();
	}

	public static void closeBrowser() {
		if (driver != null) {
			getDriver().close();
		}
	}

}
