package com.orangehrm.tests;

import org.testng.annotations.BeforeMethod;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;

public class HomePageTest extends BaseClass {
	private HomePage homepage;
	private LoginPage loginPage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
	}
}
