package com.enterprise.MyPOCs;

import org.junit.After;
import org.junit.Test;

public class Ecom_RoundTrip_PayLater_CreateCancelReservation {
	private String className = "";
	private String url = "";
	
/*	@Before
	public void setup() throws IOException {
		className = this.getClass().getSimpleName();
		System.setProperty("webdriver.chrome.driver", BrowserDrivers.CHROME_DRIVER);
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		BrowserDrivers.maximizeScreen(driver);
		url = Constants.URL;
		driver.get(url);	
	}*/
	
	@Test
	public void test_Ecom_RoundTrip_PayLater_CreateCancelReservation() throws Exception {
		try {
			GetRequest request = new GetRequest();
			request.verifyReservation();
			request.printLog("=== END " + className + " === " + url);
		}catch(Exception e){
//			ScreenshotFactory.captureScreenshot(driver, className);
			throw(e);
		}
	}
	
	@After
	public void tearDown() throws Exception {
//		driver.quit();
	}
		
}
