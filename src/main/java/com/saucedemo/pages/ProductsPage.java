package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for SauceDemo Products/Inventory Page.
 */
public class ProductsPage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector("span.title");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By sortDropdown = By.className("product_sort_container");
    private final By shoppingCartLink = By.className("shopping_cart_link");
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private final By burgerMenuButton = By.id("react-burger-menu-btn");
    private final By logoutSidebarLink = By.id("logout_sidebar_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getText(pageTitle);
    }

    public boolean isPageLoaded() {
        return isDisplayed(pageTitle) && getTitle().equalsIgnoreCase("Products");
    }

    public int getProductCount() {
        return findElements(inventoryItems).size();
    }

    public List<String> getAllProductNames() {
        List<WebElement> elements = findElements(itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement element : elements) {
            names.add(element.getText().trim());
        }
        return names;
    }

    public List<Double> getAllProductPrices() {
        List<WebElement> elements = findElements(itemPrices);
        List<Double> prices = new ArrayList<>();
        for (WebElement element : elements) {
            String priceText = element.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
        }
        return prices;
    }

    public void selectSortOptionByValue(String value) {
        selectDropdownByValue(sortDropdown, value);
    }

    public void selectSortOptionByVisibleText(String text) {
        selectDropdownByVisibleText(sortDropdown, text);
    }

    public void addProductToCart(String productName) {
        // Find product container that contains the specific product name and click its add to cart button
        String xpath = "//div[@class='inventory_item'][.//div[text()='" + productName + "']]//button[contains(@id, 'add-to-cart')]";
        click(By.xpath(xpath));
    }

    public void removeProductFromCart(String productName) {
        String xpath = "//div[@class='inventory_item'][.//div[text()='" + productName + "']]//button[contains(@id, 'remove')]";
        click(By.xpath(xpath));
    }

    public int getCartBadgeCount() {
        if (findElements(shoppingCartBadge).isEmpty()) {
            return 0;
        }
        String countText = getText(shoppingCartBadge);
        return Integer.parseInt(countText);
    }

    public CartPage clickCartIcon() {
        click(shoppingCartLink);
        return new CartPage(driver);
    }

    public LoginPage logout() {
        click(burgerMenuButton);
        wait.until(driver -> {
            WebElement logoutLink = driver.findElement(logoutSidebarLink);
            return logoutLink.isDisplayed() && logoutLink.getRect().getX() >= 20;
        });
        WebElement logoutLink = waitForClickable(logoutSidebarLink);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutLink);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        return new LoginPage(driver);
    }
}
