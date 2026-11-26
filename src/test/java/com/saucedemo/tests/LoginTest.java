package com.saucedemo.tests;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.ProductsPage;
import com.saucedemo.utils.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test cases verifying SauceDemo authentication and authorization flows.
 */
public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page should be loaded initially");

        ProductsPage productsPage = loginPage.loginAs(
                ConfigReader.getStandardUser(),
                ConfigReader.getDefaultPassword()
        );

        Assert.assertTrue(productsPage.isPageLoaded(), "Products page should load after valid login");
        Assert.assertEquals(productsPage.getTitle(), "Products", "Page header should be 'Products'");
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "URL should contain 'inventory.html'");
    }

    @Test(priority = 2, description = "Verify error message when logging in with a locked out user")
    public void testLockedOutUserLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(ConfigReader.getLockedOutUser(), ConfigReader.getDefaultPassword());

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be visible");
        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Sorry, this user has been locked out"),
                "Error should explicitly state user is locked out"
        );
    }

    @Test(priority = 3, description = "Verify error message with invalid password")
    public void testInvalidPasswordLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(ConfigReader.getStandardUser(), "wrong_password_123");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be visible");
        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Username and password do not match"),
                "Error should state credentials do not match"
        );
    }

    @Test(priority = 4, description = "Verify error message when logging in with blank credentials")
    public void testEmptyCredentialsLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be visible");
        Assert.assertTrue(
                loginPage.getErrorMessage().contains("Username is required"),
                "Error should inform that username is required"
        );
    }

    @Test(priority = 5, description = "Verify logout functionality returns user to login page")
    public void testLogout() {
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.loginAs(
                ConfigReader.getStandardUser(),
                ConfigReader.getDefaultPassword()
        );

        LoginPage postLogoutPage = productsPage.logout();
        Assert.assertTrue(postLogoutPage.isLoginPageLoaded(), "User should be redirected back to Login page after logout");
    }

    @DataProvider(name = "invalidLoginScenarios")
    public Object[][] invalidLoginScenarios() {
        String[] usernames = {
                "unknown_user", "test_user", "user123", "invalid_user", "qa_user",
                "automation_user", "sample_user", "guest_user", "demo_user", "new_user"
        };

        String[] passwords = {
                "wrong_password", "invalid_password", "incorrect123", "password123",
                "not_the_password", "test_password", "letmein123", "secret_wrong"
        };

        Object[][] scenarios = new Object[usernames.length * passwords.length][3];
        int scenarioIndex = 0;
        for (String username : usernames) {
            for (String password : passwords) {
                scenarios[scenarioIndex++] = new Object[] {
                        username, password, "Username and password do not match"
                };
            }
        }
        return scenarios;
    }

    @Test(priority = 6, dataProvider = "invalidLoginScenarios",
            description = "Verify login validation across 80 invalid credential combinations")
    public void testLoginValidationMatrix(String username, String password, String expectedErrorMessage) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(username, password);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be visible for username='" + username + "' and password='" + password + "'");
        Assert.assertTrue(loginPage.getErrorMessage().contains(expectedErrorMessage),
                "Expected error containing '" + expectedErrorMessage + "' but got '" + loginPage.getErrorMessage() + "'");
    }
}
