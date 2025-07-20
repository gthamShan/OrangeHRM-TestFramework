package com.orangehrm.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;

public class LoginPageTest extends BaseClass {

	private HomePage homepage;
	private LoginPage loginPage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}

	@Test
	public void verifyLoginTest() {
		loginPage.login("admin", "admin123");
		Assert.assertTrue(homepage.isAdminTabVisible(), "Admin tab should be visible");
		homepage.clickLogout();
		staticWait(2);
	}

	@Test
	public void verifyInvalidCredentialsTest() {
		loginPage.login("admin", "admin");
		Assert.assertEquals(loginPage.getErrorMessage(), "Invalid credentials");
	}
	
	

}
