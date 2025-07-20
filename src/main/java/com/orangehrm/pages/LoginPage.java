package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver actionDriver;

	private By userNameField = By.name("username");
	private By passwordField = By.cssSelector("input[type='password']");
	private By loginButton = By.xpath("//button[@type='submit']");
	private By invalidCredentialsMessage = By.xpath("//p[text()='Invalid credentials']");

	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public void login(String username, String pwd) {
		actionDriver.enterText(userNameField, username);
		actionDriver.enterText(passwordField, pwd);
		actionDriver.click(loginButton);
	}

	public boolean errorMessageDisplayed() {
		return actionDriver.isDisplayed(invalidCredentialsMessage);
	}

	public String getErrorMessage() {
		return actionDriver.getText(invalidCredentialsMessage);
	}
}
