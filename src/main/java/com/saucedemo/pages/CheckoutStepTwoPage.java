package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for SauceDemo Checkout Step Two: Overview.
 */
public class CheckoutStepTwoPage extends BasePage {

    // Locators
    private final By pageTitle = By.cssSelector("span.title");
    private final By itemNames = By.className("inventory_item_name");
    private final By itemPrices = By.className("inventory_item_price");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");

    public CheckoutStepTwoPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return getText(pageTitle);
    }

    public boolean isPageLoaded() {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(pageTitle, "Overview"));
    }

    public List<String> getItemNames() {
        List<WebElement> elements = findElements(itemNames);
        List<String> names = new ArrayList<>();
        for (WebElement element : elements) {
            names.add(element.getText().trim());
        }
        return names;
    }

    public double getItemTotal() {
        String text = getText(subtotalLabel); // Format: "Item total: $29.99"
        String price = text.replace("Item total: $", "").trim();
        return Double.parseDouble(price);
    }

    public double getTax() {
        String text = getText(taxLabel); // Format: "Tax: $2.40"
        String price = text.replace("Tax: $", "").trim();
        return Double.parseDouble(price);
    }

    public double getTotal() {
        String text = getText(totalLabel); // Format: "Total: $32.39"
        String price = text.replace("Total: $", "").trim();
        return Double.parseDouble(price);
    }

    public boolean isPriceCalculationAccurate() {
        double expectedTotal = getItemTotal() + getTax();
        double actualTotal = getTotal();
        return Math.abs(expectedTotal - actualTotal) < 0.01;
    }

    public CheckoutCompletePage clickFinish() {
        click(finishButton);
        return new CheckoutCompletePage(driver);
    }

    public ProductsPage clickCancel() {
        click(cancelButton);
        return new ProductsPage(driver);
    }
}
