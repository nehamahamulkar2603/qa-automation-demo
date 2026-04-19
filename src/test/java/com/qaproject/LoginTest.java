package com.qaproject;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class LoginTest {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
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
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
        Thread.sleep(2000);
        Assert.assertEquals(driver.getCurrentUrl(),
                "https://www.saucedemo.com/inventory.html",
                "Login failed for user: " + username);
    }

    @Test
    public void invalidLoginTest() {
        driver.get("https://www.saucedemo.com");
        driver.findElement(By.id("user-name")).sendKeys("wrong_user");
        driver.findElement(By.id("password")).sendKeys("wrong_password");
        driver.findElement(By.id("login-button")).click();
        String errorMessage = driver.findElement(
                By.cssSelector("[data-test='error']")).getText();
        Assert.assertEquals(errorMessage,
                "Epic sadface: Username and password do not match any user in this service",
                "Error message did not appear");
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}