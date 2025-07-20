package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/extentreport/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Report");
			spark.config().setDocumentTitle("Orange HRM Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();
			extent.setSystemInfo("OS", System.getProperty("os.name"));

		}
		return extent;
	}

	public synchronized static ExtentTest startTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	public static void endTest() {
		getReporter().flush();
	}

	public static ExtentTest getTest() {
		return test.get();
	}

	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "No test is currently active for this thread";
		}
	}

	public static void logStep(String logMessage) {
		getTest().info(logMessage);
	}

	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenshotMessage) {
		getTest().pass(logMessage);
		attachScreenshot(driver, screenshotMessage);
	}

	public static void logFailure(WebDriver driver, String logMessage, String screenshotMessage) {
		getTest().fail(logMessage);
		attachScreenshot(driver, screenshotMessage);
	}

	public static void logSkip(String logMessage) {
		String colorMessage = String.format("<span style='color:orange;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
	}

	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);

	}

	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File fs = ts.getScreenshotAs(OutputType.FILE);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		String destPath = System.getProperty("user.dir") + "/src/test/resources/screenshots" + screenshotName + "_"
				+ timeStamp + ".png";

		File dest = new File(destPath);

		try {
			FileUtils.copyDirectory(fs, dest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String base64Format = convertToBase64(fs);
		return base64Format;

	}

	public static String convertToBase64(File screenshotFile) {
		String base64Format = "";

		byte[] fileContent = null;
		try {
			fileContent = FileUtils.readFileToByteArray(screenshotFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		base64Format = Base64.getEncoder().encodeToString(fileContent);
		return base64Format;
	}

	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenshotBase64 = takeScreenshot(driver, message);
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenshotBase64).build());

		} catch (Exception e) {
			getTest().fail("Failed to attach screenshot" + message);
			e.printStackTrace();

		}
	}

}
