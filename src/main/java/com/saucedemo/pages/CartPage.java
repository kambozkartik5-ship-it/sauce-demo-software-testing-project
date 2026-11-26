package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for SauceDemo Shopping Cart Page.
 */
public class CartPage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector("span.title");
    private final By cartItems = By.className("cart_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By continueShoppingButton = By.id("continue-shopping");
    private final By checkoutButton = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getText(pageTitle);
    }

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle) && getTitle().equalsIgnoreCase("Your Cart");
    }

    public int getCartItemCount() {
        return findElements(cartItems).size();
    }

    public List<String> getCartItemNames() {
        List<WebElement> elements = findElements(itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement element : elements) {
            names.add(element.getText().trim());
        }
        return names;
    }

    public void removeItem(String productName) {
        String xpath = "//div[@class='cart_item'][.//div[text()='" + productName + "']]//button[contains(@id, 'remove')]";
        click(By.xpath(xpath));
    }

    public ProductsPage clickContinueShopping() {
        click(continueShoppingButton);
        return new ProductsPage(driver);
    }

    public CheckoutStepOnePage clickCheckout() {
        click(checkoutButton);
        return new CheckoutStepOnePage(driver);
    }

    public boolean isCartEmpty() {
        return findElements(cartItems).isEmpty();
    }
}
