package com.saucedemo.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manages ExtentReports lifecycle and formatting for rich HTML test reports.
 */
public class ExtentReportManager {

    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportDir = System.getProperty("user.dir") + File.separator + "reports";
            File dir = new File(reportDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String reportPath = reportDir + File.separator + "SauceDemo_Test_Report_" + timestamp + ".html";
            String latestReportPath = reportDir + File.separator + "LatestReport.html";

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(latestReportPath);
            sparkReporter.config().setDocumentTitle("SauceDemo Automation Test Execution Report");
            sparkReporter.config().setReportName("SauceDemo E-commerce Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // System environment metadata
            extent.setSystemInfo("Application", "SauceDemo");
            extent.setSystemInfo("Environment", "QA / Staging");
            extent.setSystemInfo("Author", "Software Testing Team");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", ConfigReader.getBrowser().toUpperCase());
        }
        return extent;
    }
}
