package com.pulu.buyer;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Application {
    private static final Random random = new Random();
    private static final int MIN_WAIT = 5;
    private static final int MAX_WAIT = 50;
    private static final int MAX_TIMEOUT = 5;

    public static void main(String[] args) throws InterruptedException {
        String url = "https://www.newegg.com/asus-geforce-rtx-3080-tuf-rtx3080-o10g-gaming/p/N82E16814126452?Description=rtx%203080&cm_re=rtx_3080-_-14-126-452-_-Product";
        Application app = new Application();
        app.start(url);
    }

    public void start(String url) {
        try {
            long begin = System.currentTimeMillis();

            ChromeOptions options = new ChromeOptions();
//            options.addArguments("start-maximized");
            System.setProperty("webdriver.chrome.args", "--disable-logging");
            options.setHeadless(false);
            WebDriver driver = new ChromeDriver(options);

//        driver.get("https://www.newegg.com/amd-ryzen-9-3900x/p/N82E16819113103?Description=ryzen%203900x&cm_re=ryzen_3900x-_-19-113-103-_-Product&quicklink=true");
//        driver.get("https://www.newegg.com/msi-radeon-rx-6800-xt-rx-6800-xt-16g/p/N82E16814137607?Description=radeon%206800%20XT&cm_re=radeon_6800%20XT-_-14-137-607-_-Product");
            driver.get(url);

            exists(driver, By.id("popup-close"), MAX_TIMEOUT).ifPresent(WebElement::click);

            boolean isPresent = false;
            while (!isPresent) {
                isPresent = exists(driver, By.cssSelector("#ProductBuy > div > div:nth-child(2) > button"), 1).isPresent();
                if (isPresent) {

                    exists(driver, By.xpath("//*[text()='" + "Sign in / Register" + "']"), 3).ifPresent(WebElement::click);
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    exists(driver, By.name("signEmail"), MAX_TIMEOUT).ifPresent(el -> el.sendKeys("puluceno@gmail.com"));
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    exists(driver, By.name("signIn"), MAX_TIMEOUT).ifPresent(WebElement::click);
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    exists(driver, By.name("password"), MAX_TIMEOUT).ifPresent(el -> el.sendKeys("909abcX*"));
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    exists(driver, By.name("signIn"), MAX_TIMEOUT).ifPresent(WebElement::click);
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    exists(driver, By.cssSelector("#ProductBuy > div > div:nth-child(2) > button"), 5).ifPresent(WebElement::click);
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    exists(driver, By.xpath("//*[text()='" + "No, thanks" + "']"), MAX_TIMEOUT).ifPresent(WebElement::click);
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    exists(driver, By.xpath("//*[text()='" + "View Cart & Checkout" + "']"), MAX_TIMEOUT).ifPresent(WebElement::click);
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    exists(driver, By.xpath("//*[text()='" + " Secure Checkout " + "']"), MAX_TIMEOUT).ifPresent(WebElement::click);
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    exists(driver, By.cssSelector("#app > div > section > div > div > form > div.row-inner > div.row-body > div > div:nth-child(3) > div > div.checkout-step-body > div.checkout-step-done > div.card > div.retype-security-code > input"), MAX_TIMEOUT).ifPresent(el -> {
                        el.click();
                        el.sendKeys("794");
                    });
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    exists(driver, By.xpath("//*[text()='" + "Place Order" + "']"), MAX_TIMEOUT).ifPresent(WebElement::click);
                    Thread.sleep(getRandom(MIN_WAIT, MAX_WAIT));

                    System.out.println("BOUGHT?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?");
                } else {
                    System.out.println("************* UNAVAILABLE *************");
                    driver.navigate().refresh();
                }
            }
            System.out.println("************* Elapsed time: " + (System.currentTimeMillis() - begin));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int getRandom(int min, int max) {
        return random.ints(min, max).findFirst().getAsInt();
    }

    private static Optional<WebElement> exists(WebDriver driver, By by, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return Optional.of(driver.findElement(by));
        } catch (TimeoutException | NoSuchElementException e) {
            return Optional.empty();
        }
    }
}
