package ru.geekbrains.market;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class TestSeleniumCatalog {
    private WebDriver driver;

    @BeforeSuite
    public void init() {
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();
    }

    @Test
    public void testCatalog() {
        driver.get("http://localhost:8181");
    }

    @AfterSuite
    public void shutdown() {
        this.driver.quit();
    }
}
