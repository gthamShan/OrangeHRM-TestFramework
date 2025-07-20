package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	private static final String BROWSER = "browser";
	private static final String IMPLICIT_WAIT = "implicitWait";
	private static final String URL = "url";
	private static final String CONFIG_FILE_PATH = "/src/main/resources/config.properties";

	protected static Properties prop;
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actiondriver = new ThreadLocal<>();

	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	@BeforeSuite
	public void loadConfig() throws IOException {
		// load config file
		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + CONFIG_FILE_PATH);
		prop.load(fis);
		logger.info("Configuration file loaded successfully.");
	}

	private void launchBrowser() {
		String browser = prop.getProperty(BROWSER);
		try {
			if ("chrome".equalsIgnoreCase(browser)) {
				driver.set(new ChromeDriver());
				ExtentManager.registerDriver(getDriver());
				logger.info("ChromeDriver launched successfully.");
			} else if ("firefox".equalsIgnoreCase(browser)) {
				driver.set(new FirefoxDriver());
				ExtentManager.registerDriver(getDriver());
				logger.info("FirefoxDriver launched successfully.");
			} else if ("edge".equalsIgnoreCase(browser)) {
				driver.set(new EdgeDriver());
				ExtentManager.registerDriver(getDriver());
				logger.info("EdgeDriver launched successfully.");
			} else {
				throw new IllegalArgumentException("browser not supported");
			}
		} catch (Exception e) {
			logger.error("Failed to launch browser: " + browser, e);
			throw new RuntimeException("Browser launch failed", e);
		}
	}

	private void configureBrowser() {
		// implicit wait
		long implicitWaitDuration = Long.parseLong(prop.getProperty(IMPLICIT_WAIT));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWaitDuration));
		getDriver().manage().window().maximize();
		try {
			String url = prop.getProperty(URL);
			getDriver().get(url);
			logger.info("Navigated to URL: " + url);
		} catch (Exception e) {
			logger.error("Failed to navigate to URL: " + prop.getProperty(URL), e);
			throw new RuntimeException("Failed to navigate to URL", e);
		}
	}

	@BeforeMethod
	public synchronized void setup() throws IOException {
		launchBrowser();
		configureBrowser();
		staticWait(5);
		actiondriver.set(new ActionDriver(getDriver()));
		logger.info("Action driver initialized for thread : " + Thread.currentThread().getId());
	}

	@AfterMethod
	public void tearDown() {
		try {
			getDriver().quit();
		} catch (Exception e) {
			logger.error("Failed to quit the driver instance.", e);
		}
		logger.info("Driver instance quit successfully.");
		driver.remove();
		actiondriver.remove();

	}

	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

	public static WebDriver getDriver() {
		if (driver.get() == null) {
			throw new IllegalStateException("WebDriver is not initialized.");
		}
		return driver.get();
	}

	public static ActionDriver getActionDriver() {
		if (actiondriver.get() == null) {
			throw new IllegalStateException("ActionDriver is not initialized.");
		}
		return actiondriver.get();
	}

	public static Properties getProp() {
		return prop;
	}

}
