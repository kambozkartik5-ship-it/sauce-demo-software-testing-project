package com.saucedemo.tests;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Test cases verifying product catalog rendering, sorting algorithms, and add-to-cart badges.
 */
public class ProductsTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(driver);
        productsPage = loginPage.loginAs(
                ConfigReader.getStandardUser(),
                ConfigReader.getDefaultPassword()
        );
    }

    @Test(priority = 1, description = "Verify that the inventory catalog displays all 6 standard products")
    public void testProductCatalogCount() {
        Assert.assertTrue(productsPage.isPageLoaded(), "Products catalog page should be loaded");
        int count = productsPage.getProductCount();
        Assert.assertEquals(count, 6, "SauceDemo catalog should display exactly 6 items");
    }

    @Test(priority = 2, description = "Verify sorting products alphabetically from A to Z")
    public void testSortNameAtoZ() {
        productsPage.selectSortOptionByValue("az");
        List<String> actualNames = productsPage.getAllProductNames();

        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames);

        Assert.assertEquals(actualNames, expectedNames, "Products should be sorted alphabetically from A to Z");
    }

    @Test(priority = 3, description = "Verify sorting products alphabetically from Z to A")
    public void testSortNameZtoA() {
        productsPage.selectSortOptionByValue("za");
        List<String> actualNames = productsPage.getAllProductNames();

        List<String> expectedNames = new ArrayList<>(actualNames);
        expectedNames.sort(Collections.reverseOrder());

        Assert.assertEquals(actualNames, expectedNames, "Products should be sorted in reverse alphabetical order Z to A");
    }

    @Test(priority = 4, description = "Verify sorting products by price: Low to High")
    public void testSortPriceLowToHigh() {
        productsPage.selectSortOptionByValue("lohi");
        List<Double> actualPrices = productsPage.getAllProductPrices();

        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        Assert.assertEquals(actualPrices, expectedPrices, "Products should be sorted from lowest price to highest price");
    }

    @Test(priority = 5, description = "Verify sorting products by price: High to Low")
    public void testSortPriceHighToLow() {
        productsPage.selectSortOptionByValue("hilo");
        List<Double> actualPrices = productsPage.getAllProductPrices();

        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        expectedPrices.sort(Collections.reverseOrder());

        Assert.assertEquals(actualPrices, expectedPrices, "Products should be sorted from highest price to lowest price");
    }

    @Test(priority = 6, description = "Verify cart badge increment and decrement upon adding/removing products")
    public void testCartBadgeCountUpdates() {
        Assert.assertEquals(productsPage.getCartBadgeCount(), 0, "Cart badge should initially be empty/zero");

        productsPage.addProductToCart("Sauce Labs Backpack");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart badge should display 1 after adding one item");

        productsPage.addProductToCart("Sauce Labs Bike Light");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 2, "Cart badge should display 2 after adding second item");

        productsPage.removeProductFromCart("Sauce Labs Backpack");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart badge should decrement to 1 after removing an item");
    }
}
