package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	private ActionDriver actionDriver;

	private By AdminTab = By.xpath("//span[text()='Admin']");
	private By userIdButton = By.className("oxd-userdropdown-tab");
	private By logoutlink = By.xpath("//a[text()='Logout']");
	private By OrangeHRMLogo = By.xpath("//div[@class='oxd-brand-banner']");

	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(AdminTab);
	}

	public boolean verifyOrangeHRMLogo() {
		return actionDriver.isDisplayed(OrangeHRMLogo);
	}

	public void clickLogout() {
		actionDriver.click(userIdButton);
		actionDriver.click(logoutlink);
	}

}
