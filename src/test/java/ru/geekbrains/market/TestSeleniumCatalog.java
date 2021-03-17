package ru.geekbrains.market;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;

@RunWith(SpringRunner.class)
public class TestSeleniumCatalog {
    private WebDriver driver;

    @BeforeSuite
    public void init() {
        this.driver = new ChromeDriver();
        this.driver.manage().window().maximize();

    }

    @Test
    public void testCatalog() throws InterruptedException {
        driver.get("http://localhost:8181");
        WebElement products = driver.findElement(By.xpath("//*[@id=\"navbarNav\"]/ul/li[1]/a"));
        products.click();
        WebElement tableRow = driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]/td[3]"));
        Assert.assertEquals("20\" Телевизор Samsung UE20NU7170U",tableRow.getText());
    }

    @AfterSuite
    public void shutdown() {
        this.driver.quit();
    }
}
