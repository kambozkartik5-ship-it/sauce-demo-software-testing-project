package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
/**
 * Page Object for SauceDemo Checkout Step One: Your Information.
 */
public class CheckoutStepOnePage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector("span.title");
    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By cancelButton = By.id("cancel");
    private final By errorMessageContainer = By.cssSelector("h3[data-test='error']");

    public CheckoutStepOnePage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getText(pageTitle);
    }

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle) && getTitle().contains("Your Information");
    }

    public void enterFirstName(String firstName) {
        type(firstNameField, firstName);
    }

    public void enterLastName(String lastName) {
        type(lastNameField, lastName);
    }

    public void enterPostalCode(String postalCode) {
        type(postalCodeField, postalCode);
    }

    public void fillCustomerInfo(String firstName, String lastName, String postalCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPostalCode(postalCode);
    }

    public CheckoutStepTwoPage clickContinue() {
        List<WebElement> existingErrors = findElements(errorMessageContainer);
        String previousError = existingErrors.isEmpty() ? "" : existingErrors.get(0).getText().trim();
        click(continueButton);
        wait.until(currentDriver -> {
            if (currentDriver.getCurrentUrl().contains("checkout-step-two.html")) {
                return true;
            }

            List<WebElement> errors = currentDriver.findElements(errorMessageContainer);
            return !errors.isEmpty() && !errors.get(0).getText().trim().equals(previousError);
        });
        return new CheckoutStepTwoPage(driver);
    }

    public CartPage clickCancel() {
        click(cancelButton);
        return new CartPage(driver);
    }

    public String getErrorMessage() {
        return getText(errorMessageContainer);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessageContainer);
    }
}
