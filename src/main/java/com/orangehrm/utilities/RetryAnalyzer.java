package com.orangehrm.utilities;

import org.testng.IRetryAnalyzer;

public class RetryAnalyzer implements IRetryAnalyzer {

	private int retryCount = 0;
	private static final int maxRetryCount = 3;

	@Override
	public boolean retry(org.testng.ITestResult result) {
		if (retryCount < maxRetryCount) {
			retryCount++;
			return true; // Retry the test
		}
		return false; // Do not retry
	}

}
