package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for SauceDemo Checkout Complete / Order Confirmation Page.
 */
public class CheckoutCompletePage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector("span.title");
    private final By completeHeader = By.className("complete-header");
    private final By completeText = By.className("complete-text");
    private final By ponyExpressImage = By.className("pony_express");
    private final By backHomeButton = By.id("back-to-products");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getText(pageTitle);
    }

    public String getCompleteHeader() {
        return getText(completeHeader);
    }

    public String getCompleteText() {
        return getText(completeText);
    }

    public boolean isPonyExpressImageDisplayed() {
        return isDisplayed(ponyExpressImage);
    }

    public boolean isOrderCompletedSuccessfully() {
        return getCompleteHeader().equalsIgnoreCase("Thank you for your order!") && isPonyExpressImageDisplayed();
    }

    public ProductsPage clickBackHome() {
        click(backHomeButton);
        return new ProductsPage(driver);
    }
}
