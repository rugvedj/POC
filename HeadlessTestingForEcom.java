package com.enterprise.MyPOCs;

import java.io.IOException;
import java.util.Calendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.enterprise.object.BookingWidgetObject;
import com.enterprise.object.CarObject;
import com.enterprise.object.EnterpriseBaseObject;
import com.enterprise.object.ExtrasObject;
import com.enterprise.object.ReservationObject;
import com.enterprise.util.BrowserDrivers;
import com.enterprise.util.Constants;
import com.enterprise.util.FileAppendWriter;
import com.enterprise.util.ScreenshotFactory;

public class HeadlessTestingForEcom {
	private static final String LOCATION = "CDG";
	private WebDriver driver = null;
	private String className = "";
	private String url = "";
	
	@Before
	public void setup() throws IOException {
		className = this.getClass().getSimpleName();
		System.setProperty("webdriver.chrome.driver", BrowserDrivers.CHROME_DRIVER);
		driver = new HtmlUnitDriver(true);
//		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		BrowserDrivers.maximizeScreen(driver);
		url = Constants.URL;
		driver.get(url);		
	}
	
	@Test
	public void test_HeadlessTestingForEcom() throws Exception {
		try{
			// File to keep records of reservation
			FileAppendWriter fafw = new FileAppendWriter();
			// Keep track of reservation number
			String reservationNumber = null;
			// Calendar for creating timestamp in the confirmation file
			Calendar calendar = Calendar.getInstance();
			// Test booking widget
			BookingWidgetObject eHome = new BookingWidgetObject(driver);
			eHome.printLog("=== BEGIN " + className + " === " + url);
			eHome.confirmLocalWebsite(driver, url);
			eHome.enterAndVerifyFirstLocationOnList(driver, LOCATION, BookingWidgetObject.PICKUP_LOCATION);
			eHome.enterAndVerifyPickupDateAndTimeOfDoubleCalendars(driver, EnterpriseBaseObject.DESKTOP_BROWSER);
			eHome.enterAndVerifyReturnDateAndTimeOfDoubleCalendars(driver, EnterpriseBaseObject.DESKTOP_BROWSER);
			eHome.verifyContinueButtonAndClick(driver);
			
			CarObject car = new CarObject(driver); 
			car.clickFirstCar(driver, url, LOCATION);
			car.clickPayLaterButton(driver, url, LOCATION);
			
			ExtrasObject carExtra = new ExtrasObject(driver);
			carExtra.verifyPageHeaderAndPayButtons(driver);
			carExtra.verifyReviewAndPayButtonAndClick(driver);
			
			ReservationObject reservation = new ReservationObject(driver);
			reservation.enterPersonalInfoForm(driver);
			reservation.enterFlightNumber(driver, url);
			reservation.submitReservationForm(driver);
			reservationNumber = reservation.getReservationNumber();
			fafw.appendToFile(calendar.getTime() + String.valueOf('\t') + reservationNumber + String.valueOf('\t') + "CREATED    " + String.valueOf('\t') + url);
			
			// Cancel reservation directly from the reserve confirmed page
			reservation.cancelReservationFromGreenLinkOnReserveConfirmed(driver);
			fafw.appendToFile(calendar.getTime() + String.valueOf('\t') + reservationNumber + String.valueOf('\t') + "CANCELLED" + String.valueOf('\t') + url);
			reservation.printLog("=== END " + className + " === " + url);
		}catch(Exception e){
			ScreenshotFactory.captureScreenshot(driver, className);
			throw(e);
		}
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
