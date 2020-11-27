package com.pulu.buyer;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Optional;
import java.util.logging.Logger;

public class NeweggEngine {
    private static final Logger logger = Logger.getLogger("pooplog");

    public void start(String url) {
        try {
            logger.info("Trying to purchase - " + url);

            WebDriver driver = new ChromeDriver();

            driver.get(url);

            exists(driver, By.id("popup-close")).ifPresent(WebElement::click);

            boolean isPresent = false;
            while (!isPresent) {
                isPresent = exists(driver, By.cssSelector("#ProductBuy > div > div:nth-child(2) > button")).isPresent();
                if (isPresent) {

                    exists(driver, By.xpath("//*[text()='" + "Sign in / Register" + "']")).ifPresent(WebElement::click);

                    exists(driver, By.name("signEmail")).ifPresent(el -> el.sendKeys(//TODO: EMAIL""));

                    exists(driver, By.name("signIn")).ifPresent(WebElement::click);

                    exists(driver, By.name("password")).ifPresent(el -> el.sendKeys(//TODO: PASSWORD""));

                    exists(driver, By.name("signIn")).ifPresent(WebElement::click);

                    exists(driver, By.cssSelector("#ProductBuy > div > div:nth-child(2) > button")).ifPresent(WebElement::click);

                    exists(driver, By.xpath("//*[text()='" + "No, thanks" + "']")).ifPresent(WebElement::click);

                    exists(driver, By.xpath("//*[text()='" + "View Cart & Checkout" + "']")).ifPresent(WebElement::click);

                    exists(driver, By.xpath("//*[text()='" + " Secure Checkout " + "']")).ifPresent(WebElement::click);

                    exists(driver, By.cssSelector("#app > div > section > div > div > form > div.row-inner > div.row-body > div > div:nth-child(3) > div > div.checkout-step-body > div.checkout-step-done > div.card > div.retype-security-code > input")).ifPresent(el -> {
                        el.click();
                        el.sendKeys(//TODO: CARD CVV"");
                    });

                    exists(driver, By.xpath("//*[text()='" + "Place Order" + "']")).ifPresent(WebElement::click);

                    System.out.println("BOUGHT?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?!?");

                    isPresent = true;
                } else {
                    System.out.println("************* UNAVAILABLE *************");
                    driver.navigate().refresh();
                }
            }
            System.out.println("************* Finished process!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Optional<WebElement> exists(WebDriver driver, By by) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return Optional.of(driver.findElement(by));
        } catch (TimeoutException | NoSuchElementException e) {
            return Optional.empty();
        }
    }
}
