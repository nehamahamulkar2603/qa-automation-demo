package com.qaproject;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import com.qaproject.pages.LoginPage;
import org.openqa.selenium.chrome.ChromeOptions;

public class LoginTest {

    WebDriver driver;
    LoginPage loginPage;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        loginPage = new LoginPage(driver);
    }

    @DataProvider(name = "validUsers")
    public Object[][] getValidUsers() {
        return new Object[][] {
                {"standard_user", "secret_sauce"},
                {"problem_user", "secret_sauce"},
                {"performance_glitch_user", "secret_sauce"}
        };
    }

    @Test(dataProvider = "validUsers")
    public void validLoginTest(String username, String password)
            throws InterruptedException {
        driver.get("https://www.saucedemo.com");
        loginPage.login(username, password);
        Thread.sleep(2000);
        Assert.assertEquals(driver.getCurrentUrl(),
                "https://www.saucedemo.com/inventory.html",
                "Login failed for user: " + username);
    }

    @Test
    public void invalidLoginTest() {
        driver.get("https://www.saucedemo.com");
        loginPage.login("wrong_user", "wrong_password");
        Assert.assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service",
                "Error message did not appear");
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}