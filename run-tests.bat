@echo off
title SauceDemo Automation Test Execution
cls
echo ========================================================
echo       SauceDemo E-Commerce Automated Test Suite
echo ========================================================
echo.
echo Running all 17 automated tests using TestNG and Selenium...
echo.
call "C:\Users\kambo\apache-maven\apache-maven-3.9.9\bin\mvn.cmd" clean test
echo.
echo ========================================================
echo           Test Execution Finished!
echo ========================================================
echo Opening HTML Test Report in your browser...
if exist reports\LatestReport.html (
    start "" reports\LatestReport.html
) else if exist target\surefire-reports\index.html (
    start "" target\surefire-reports\index.html
)
echo.
pause
