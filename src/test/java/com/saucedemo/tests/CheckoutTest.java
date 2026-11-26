package com.saucedemo.tests;

import com.saucedemo.pages.*;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test cases verifying customer checkout workflows, input validations, and financial calculations.
 */
public class CheckoutTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(driver);
        productsPage = loginPage.loginAs(
                ConfigReader.getStandardUser(),
                ConfigReader.getDefaultPassword()
        );
    }

    @Test(priority = 1, description = "Verify complete End-to-End order placement and confirmation")
    public void testEndToEndCheckoutFlow() {
        String product = "Sauce Labs Backpack";
        productsPage.addProductToCart(product);

        CartPage cartPage = productsPage.clickCartIcon();
        CheckoutStepOnePage stepOne = cartPage.clickCheckout();

        Assert.assertTrue(stepOne.isPageLoaded(), "Checkout step one should load");

        stepOne.fillCustomerInfo("Rahul", "Sharma", "110001");
        CheckoutStepTwoPage stepTwo = stepOne.clickContinue();

        Assert.assertTrue(stepTwo.isPageLoaded(), "Checkout step two (Overview) should load");
        Assert.assertTrue(stepTwo.getItemNames().contains(product), "Product should be present on overview");
        Assert.assertTrue(stepTwo.isPriceCalculationAccurate(), "Tax + Subtotal must equal Final Total");

        CheckoutCompletePage completePage = stepTwo.clickFinish();
        Assert.assertTrue(completePage.isOrderCompletedSuccessfully(), "Order confirmation message should appear");
        Assert.assertEquals(completePage.getCompleteHeader(), "Thank you for your order!");
        Assert.assertTrue(completePage.getCompleteText().contains("Your order has been dispatched"));

        ProductsPage returnedHome = completePage.clickBackHome();
        Assert.assertTrue(returnedHome.isPageLoaded(), "Clicking Back Home should return to inventory catalog");
    }

    @Test(priority = 2, description = "Verify validation error messages for missing checkout fields")
    public void testCheckoutMissingInformationValidations() {
        productsPage.addProductToCart("Sauce Labs Onesie");
        CartPage cartPage = productsPage.clickCartIcon();
        CheckoutStepOnePage stepOne = cartPage.clickCheckout();

        // 1. Submit with all blank fields
        stepOne.clickContinue();
        Assert.assertTrue(stepOne.isErrorMessageDisplayed(), "Error should display when fields are blank");
        Assert.assertTrue(stepOne.getErrorMessage().contains("First Name is required"));

        // 2. Fill First Name only
        stepOne.enterFirstName("Rahul");
        stepOne.clickContinue();
        Assert.assertTrue(stepOne.getErrorMessage().contains("Last Name is required"));

        // 3. Fill Last Name only (leave Postal Code blank)
        stepOne.enterLastName("Sharma");
        stepOne.clickContinue();
        Assert.assertTrue(stepOne.getErrorMessage().contains("Postal Code is required"));
    }

    @Test(priority = 3, description = "Verify item subtotal and tax calculation accuracy on multiple items")
    public void testFinancialCalculationsOnOverview() {
        productsPage.addProductToCart("Sauce Labs Backpack");
        productsPage.addProductToCart("Sauce Labs Fleece Jacket");

        CartPage cartPage = productsPage.clickCartIcon();
        CheckoutStepOnePage stepOne = cartPage.clickCheckout();
        stepOne.fillCustomerInfo("Aarav", "Patel", "400001");

        CheckoutStepTwoPage stepTwo = stepOne.clickContinue();
        Assert.assertTrue(stepTwo.isPageLoaded());

        double itemTotal = stepTwo.getItemTotal();
        double tax = stepTwo.getTax();
        double total = stepTwo.getTotal();

        // Validate individual values
        Assert.assertTrue(itemTotal > 0, "Item total must be positive");
        Assert.assertTrue(tax > 0, "Tax amount must be positive");
        Assert.assertEquals(Math.round((itemTotal + tax) * 100.0) / 100.0, total, "Subtotal + Tax must exactly match Total");
    }
}
