package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for SauceDemo Login Page.
 */
public class LoginPage extends BasePage {

    // Locators
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessageContainer = By.cssSelector("h3[data-test='error']");
    private final By loginLogo = By.className("login_logo");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void enterUsername(String username) {
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public ProductsPage clickLogin() {
        click(loginButton);
        return new ProductsPage(driver);
    }

    public ProductsPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    public String getErrorMessage() {
        return getText(errorMessageContainer);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessageContainer);
    }

    public boolean isLoginPageLoaded() {
        return isDisplayed(loginButton) && isDisplayed(loginLogo);
    }
}
