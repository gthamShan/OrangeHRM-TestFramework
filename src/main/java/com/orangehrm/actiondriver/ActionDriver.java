package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) throws NumberFormatException {
		this.driver = driver;
		int explicitWait = 0;
		try {
			explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
	}

	public void click(By by) {
		try {
			waitForElementClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("clicked on element: " + getElementDescription(by));
			logger.info("Clicked on element: " + by.toString());
		} catch (Exception e) {
			ExtentManager.logFailure(BaseClass.getDriver(), "unable to click element:", getElementDescription(by));
			logger.error("Unable to click on element: " + by.toString(), e);
		}
	}

	public void enterText(By by, String value) {
		try {
			waitForElementVisible(by);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text: '" + value + "' in element: " + by.toString());
		} catch (Exception e) {
			logger.error("Unable to enter value: '" + value + "' in element: " + by.toString(), e);
		}
	}

	public String getText(By by) {
		try {
			waitForElementVisible(by);
			logger.info("Getting text from element: " + by.toString());
			return driver.findElement(by).getText();
		} catch (Exception e) {
			logger.error("Unable to get text from element: " + by.toString(), e);
			return "";
		}
	}

	public void compareText(By by, String expectedText) {
		try {
			waitForElementVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equalsIgnoreCase(actualText)) {
				logger.info("Text is matching: '" + actualText + "' equals '" + expectedText + "'");
			} else {
				logger.error("Text is not matching: '" + actualText + "' equals '" + expectedText + "'");
			}
		} catch (Exception e) {
			logger.error("Unable to compare texts: " + e.getMessage());
		}
	}

	public boolean isDisplayed(By by) {
		try {
			waitForElementVisible(by);
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			logger.error("Element is not displayed: " + by.toString() + ". Exception: " + e.getMessage());
			return false;
		}

	}

	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0],scrollIntoView(true)", element);
		} catch (Exception e) {
			logger.error("Unable to scroll to element: " + by.toString() + ". Exception: " + e.getMessage());
		}

	}

	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully within " + timeOutInSec + " seconds.");
		} catch (Exception e) {
			logger.error("Page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
		}

	}

	private void waitForElementClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
			logger.info("Element is clickable: " + by.toString());
		} catch (Exception e) {
			logger.error("Element is not clickable: " + by.toString() + ". Exception: " + e.getMessage());
		}
	}

	private void waitForElementVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			logger.info("Element is visible: " + by.toString());
		} catch (Exception e) {
			logger.error("Element is not visible: " + by.toString() + ". Exception: " + e.getMessage());
		}
	}

	// method to get the description of the element using by locator
	public String getElementDescription(By by) {
		try {
			WebElement element = driver.findElement(by);
			String description = element.getAttribute("title");
			String name = element.getAttribute("name");
			String id = element.getAttribute("id");
			String className = element.getAttribute("class");
			String text = element.getText();
			String placeholder = element.getAttribute("placeholder");

			if (isNotEmpty(description)) {
				logger.info("Element description: " + description);
				return description;
			} else if (isNotEmpty(name)) {
				logger.info("Element name: " + name);
				return name;
			} else if (isNotEmpty(id)) {
				logger.info("Element ID: " + id);
				return id;
			} else if (isNotEmpty(className)) {
				logger.info("Element class: " + className);
				return className;
			} else if (isNotEmpty(text)) {
				logger.info("Element text: " + text);
				return text;
			} else if (isNotEmpty(placeholder)) {
				logger.info("Element placeholder: " + placeholder);
				return placeholder;
			}

			else {
				logger.warn("No description found for element: " + by.toString());
				return "No description available";
			}
		} catch (Exception e) {
			logger.error("Unable to get description for element: " + by.toString(), e);
			return "Error retrieving description";
		}
	}

	// utility method to check a string is not null or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	public void applyBorder(By by, String color) {
		try {
			WebElement webelement = driver.findElement(by);
			String script = "arguments[0].apply.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeAsyncScript(script, webelement);
		} catch (Exception e) {
			logger.warn("Failed to apply the border");

		}

	}

}
