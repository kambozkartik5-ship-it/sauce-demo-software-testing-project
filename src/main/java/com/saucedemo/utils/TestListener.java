package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestNG listener to log events and attach screenshots on failure into ExtentReports.
 */
public class TestListener implements ITestListener {

    private static ExtentReports extent = ExtentReportManager.getInstance();
    private static ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();

    public static ExtentTest getTest() {
        return testNode.get();
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("====== Starting Test Suite: " + context.getName() + " ======");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("====== Finished Test Suite: " + context.getName() + " ======");
        if (extent != null) {
            extent.flush();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = testName;
        }

        ExtentTest test = extent.createTest(result.getTestClass().getRealClass().getSimpleName() + " :: " + testName, description);
        testNode.set(test);
        test.log(Status.INFO, "Test execution started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (getTest() != null) {
            getTest().log(Status.PASS, "Test PASSED: " + result.getMethod().getMethodName());
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (getTest() != null) {
            getTest().log(Status.FAIL, "Test FAILED: " + result.getMethod().getMethodName());
            getTest().log(Status.FAIL, result.getThrowable());

            // Capture screenshot if WebDriver is available
            Object currentClass = result.getInstance();
            try {
                WebDriver driver = (WebDriver) result.getTestClass().getRealClass()
                        .getMethod("getDriver")
                        .invoke(currentClass);

                if (driver != null) {
                    String screenshotPath = captureScreenshot(driver, result.getMethod().getMethodName());
                    getTest().addScreenCaptureFromPath(screenshotPath);
                }
            } catch (Exception e) {
                getTest().log(Status.WARNING, "Unable to capture screenshot: " + e.getMessage());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (getTest() != null) {
            getTest().log(Status.SKIP, "Test SKIPPED: " + result.getMethod().getMethodName());
        }
    }

    private String captureScreenshot(WebDriver driver, String screenshotName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destDir = System.getProperty("user.dir") + File.separator + "reports" + File.separator + "screenshots";
        File dir = new File(destDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = screenshotName + "_" + timestamp + ".png";
        File destination = new File(destDir + File.separator + fileName);

        try {
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(source, destination);
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }

        return "screenshots" + File.separator + fileName;
    }
}
