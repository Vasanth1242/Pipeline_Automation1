package stepdefinitions;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import driver.DriverFactory;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.LoginPage;

public class LoginStep {

	private WebDriver driver;
	LoginPage loginPage;

	@Given("the user launches the browser")
	public void the_user_launches_the_browser() {
		driver = DriverFactory.getDriver();
		loginPage = new LoginPage(driver);
		Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo"), "Application URL does not contain 'saucedemo'");
		System.out.println("Application opened");
	}

	@When("the user enter username  {string}")
	public void the_user_enter_username(String username) {
		if (username != null && !username.trim().isEmpty()) {
			loginPage.enterUsername(username);
			System.out.println("Username entered: " + username);
		} else {
			System.out.println("Username is empty");
		}
	}

	@When("the user enter password {string}")
	public void the_user_enter_password(String password) {
		if (password != null && !password.trim().isEmpty()) {
			loginPage.enterPassword(password);
			System.out.println("Password entered: " + password);
		} else {
			System.out.println("Password is empty");
		}
	}

	@When("the user clicks the login button")
	public void the_user_clicks_the_login_button() {
		loginPage.clickLogin();
		System.out.println("poll scm run successfully");
	}

	@Then("login should be {string}")
	public void login_should_be(String result) {
		if (result.equalsIgnoreCase("Valid")) {
			Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "User is not on the product page");
			System.out.println("Login successfully");
			System.out.println("Allure report + email validation");
		} else if (result.equalsIgnoreCase("Invalid")) {
			Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
					"Expected error message, but error message was not displayed");
			System.out.println("Login failed as expected");
		}
	}

}
