package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class SeleniumTest {
    private final String BASE_URL ="https://demo.opencart.com/";
    private WebDriver driver;
    private final Long IMPLICITLY_WAIT_SECONDS = 10L;
    private final Long ONE_SECOND_DELAY = 1000L;
    private final double priceEuro = 111.55;
    private final double eps = 0.001;

    private void presentationSleep() {
        presentationSleep(1);
    }

    private void presentationSleep(int seconds) {
        try {
            Thread.sleep(seconds * ONE_SECOND_DELAY); // For Presentation ONLY
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void beforeSuite() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeClass
    public void beforeClass() {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        //
        //driver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_SECONDS, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICITLY_WAIT_SECONDS));
        driver.manage().window().maximize();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeMethod
    public void beforeMethod() {
        driver.get(BASE_URL);
        presentationSleep();
    }

    @Test
    public void checkPrice(){
        WebElement currency = driver.findElement(By.id("form-currency"));
        currency.click();
        presentationSleep();
        WebElement euro = driver.findElement(By.xpath("//*[@id=\"form-currency\"]/div/ul/li[1]/a"));
        euro.click();
        presentationSleep();
        WebElement desktop = driver.findElement(By.xpath("//*[@id=\"narbar-menu\"]/ul/li[1]/a"));
        desktop.click();
        presentationSleep();
        WebElement mac = driver.findElement(
                By.xpath("//*[@id=\"narbar-menu\"]/ul/li[1]/div/div/ul/li[2]/a")
        );
        mac.click();
        presentationSleep();
        List<WebElement> products = driver.findElements(By.className("product-thumb"));
        //System.out.println(products.size());
        boolean containsPrice = false;
        for(WebElement element:products){
            WebElement priceElement = element.findElement(By.className("price-new"));
            String price = priceElement.getText();
            //System.out.println(price);
            price = price.replaceAll("[^0-9?!\\.]","");
            //System.out.println(price);
            if(Double.parseDouble(price)-priceEuro < eps){
                containsPrice=true;
                break;
            }
        }
        Assert.assertTrue(containsPrice);
    }



}
