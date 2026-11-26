package com.saucedemo.tests;

import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test cases verifying shopping cart state, item verification, and cart actions.
 */
public class CartTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeMethod(alwaysRun = true)
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(driver);
        productsPage = loginPage.loginAs(
                ConfigReader.getStandardUser(),
                ConfigReader.getDefaultPassword()
        );
    }

    @Test(priority = 1, description = "Verify added products appear correctly in the shopping cart")
    public void testAddAndVerifyItemsInCart() {
        String item1 = "Sauce Labs Backpack";
        String item2 = "Sauce Labs Bolt T-Shirt";

        productsPage.addProductToCart(item1);
        productsPage.addProductToCart(item2);

        CartPage cartPage = productsPage.clickCartIcon();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be loaded");
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should contain exactly 2 items");

        List<String> itemsInCart = cartPage.getCartItemNames();
        Assert.assertTrue(itemsInCart.contains(item1), "Cart must contain: " + item1);
        Assert.assertTrue(itemsInCart.contains(item2), "Cart must contain: " + item2);
    }

    @Test(priority = 2, description = "Verify removing an item from within the cart page")
    public void testRemoveItemFromCart() {
        String item1 = "Sauce Labs Backpack";
        String item2 = "Sauce Labs Bike Light";

        productsPage.addProductToCart(item1);
        productsPage.addProductToCart(item2);

        CartPage cartPage = productsPage.clickCartIcon();
        Assert.assertEquals(cartPage.getCartItemCount(), 2);

        // Remove item1
        cartPage.removeItem(item1);

        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart item count should be 1 after removal");
        List<String> remainingItems = cartPage.getCartItemNames();
        Assert.assertFalse(remainingItems.contains(item1), "Removed item should no longer be in the cart");
        Assert.assertTrue(remainingItems.contains(item2), "Second item should still be present in the cart");
    }

    @Test(priority = 3, description = "Verify Continue Shopping button redirects back to the products catalog")
    public void testContinueShoppingNavigation() {
        CartPage cartPage = productsPage.clickCartIcon();
        Assert.assertTrue(cartPage.isPageLoaded(), "Cart page should be visible");

        ProductsPage returnedProductsPage = cartPage.clickContinueShopping();
        Assert.assertTrue(returnedProductsPage.isPageLoaded(), "Should be redirected back to the Products page");
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "URL should contain 'inventory.html'");
    }
}
