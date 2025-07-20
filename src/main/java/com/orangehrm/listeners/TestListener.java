package com.orangehrm.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class TestListener implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test started" + testName);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed Successfully", testName);
	}

	@Override
	public void onTestFailure(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Failed ", testName);
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped" + testName);
	}

	@Override
	public void onStart(ITestContext context) {
		ExtentManager.getReporter();
	}

	@Override
	public void onFinish(ITestContext context) {
		ExtentManager.endTest();
	}

}
