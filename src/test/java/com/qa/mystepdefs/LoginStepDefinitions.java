package com.qa.mystepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.cucumber.java.en.Then;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import pages.LoginPage;

public class LoginStepDefinitions {

    private WebDriver driver;
    private LoginPage loginPage;

    @Before
    public void setup() throws IOException{
    	Properties prop = new Properties();
		File proFile = new File("src\\test\\java\\config\\config.properties");
		
		
			FileInputStream fis = new FileInputStream(proFile);
			prop.load(fis);
    			
    			String browserName = System.getProperty("browser")!=null ? System.getProperty("browser") :prop.getProperty("browser");
//    			String browserName=prop.getProperty("browser");
    			if (browserName.contains("chrome")) {
    				ChromeOptions options = new ChromeOptions();
    				WebDriverManager.chromedriver().setup();
    				if(browserName.contains("headless")){
    				options.addArguments("headless");
    				}		
    				driver = new ChromeDriver(options);
    				driver.manage().window().setSize(new Dimension(1440,900));}
    		
    			else if (browserName.equalsIgnoreCase("edge")) {
    			
    				System.setProperty("webdriver.edge.driver",
				"src\\test\\java\\config\\msedgedriver.exe");
    			
    				driver = new EdgeDriver();
    			}
//    				// Firefox
//    			} 
//    			if (browserName.equalsIgnoreCase("chrome")) {
////    				WebDriverManager.edgedriver().setup();
//    				System.setProperty("webdriver.gecko.driver",
//				"src\\test\\java\\config\\geckodriver.exe");
//    				driver = new FirefoxDriver();
//    				// Firefox
//    			} 
    				
    			}
    

    @After
    public void tearDown(){
        if(driver!=null){
            driver.quit();
        }
    }


    @Given("I am on the OpenCart login page")
    public void i_am_on_the_open_cart_login_page() {
        driver.get("https://naveenautomationlabs.com/opencart/index.php?route=account/login");
        loginPage = new LoginPage(driver);

    }

    @Given("I have entered a valid username and password")
    public void i_have_entered_a_valid_username_and_password() {
        loginPage.enterEmail("qatestertest@gmail.com");
        loginPage.enterPassword("Test@123");
    }

    @Given("I have entered invalid {string} and {string}")
    public void i_have_entered_invalid_and(String username, String password) {
        loginPage.enterEmail(username);
        loginPage.enterPassword(password);
    }

    @When("I click on the login button")
    public void i_click_on_the_login_button() {
        loginPage.clickLoginButton();
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
       Assert.assertEquals(loginPage.checkLogoutLink(), true);
    }



    @Then("I should see an error message indicating {string}")
    public void i_should_see_an_error_message_indicating(String errorMessage) {
        // Assert that an error message is displayed on the page matching the expected error message
        Assert.assertEquals( driver.findElement(By.cssSelector(".alert-danger")).isDisplayed(), true);
    }

    @When("I click on the \"Forgotten Password\" link")
    public void i_click_on_the_forgotten_password_link() {
        loginPage.clickForgottenPasswordLink();
    }

    @Then("I should be redirected to the password reset page")
    public void i_should_be_redirected_to_the_password_reset_page() {
        // Assert that the current URL contains the password reset page route
        Assert.assertTrue(loginPage.getForgotPwdPageUrl().contains("account/forgotten"));
    }
}

