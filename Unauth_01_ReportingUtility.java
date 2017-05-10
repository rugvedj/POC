package com.enterprise.MyPOCs;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.enterprise.object.BookingWidgetObject;
import com.enterprise.object.CarObject;
import com.enterprise.object.EnterpriseBaseObject;
import com.enterprise.object.ExtrasObject;
import com.enterprise.object.ReservationObject;
import com.enterprise.util.BrowserDrivers;
import com.enterprise.util.Constants;
import com.enterprise.util.FileAppendWriter;
import com.enterprise.util.MailUtility;
import com.enterprise.util.ReportsUtility;
import com.enterprise.util.ScreenshotFactory;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;




public class Unauth_01_ReportingUtility {
	private static final String LOCATION = "STLT61";
	private WebDriver driver = null;
	private String className = "";
	private String url = "";
	private ReportsUtility reportUtil;
	private ExtentReports extent;
	private ExtentTest extentTest;
	
	@Before
	public void setup() throws IOException {
		className = this.getClass().getSimpleName();
		System.setProperty("webdriver.chrome.driver", BrowserDrivers.CHROME_DRIVER);
		driver = new ChromeDriver();
		// Create object of extent report and specify the report file path.
		reportUtil = new ReportsUtility();
		extent = reportUtil.createReport();
		// Start the test using the ExtentTest class object.
		extentTest = extent.startTest("testUnauth_01_CreateAndCancelEuPayLater");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		BrowserDrivers.maximizeScreen(driver);
		extentTest.log(LogStatus.INFO, "Browser Launched");
		url = Constants.URL;
		driver.get(url);
		extentTest.log(LogStatus.INFO, "Navigated to "+url);
	}
	
	@Test
	public void testUnauth_01_CreateAndCancelEuPayLater() throws Exception {
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
			extentTest.log(LogStatus.INFO, "=== BEGIN " + className + " === " + url);
			eHome.confirmLocalWebsite(driver, url);
			extentTest.log(LogStatus.INFO, "confirmLocalWebsite");
			eHome.enterAndVerifyFirstLocationOnList(driver, LOCATION, BookingWidgetObject.PICKUP_LOCATION);
			extentTest.log(LogStatus.PASS, "PICKUP_LOCATION verified");
			eHome.enterAndVerifyPickupDateAndTimeOfDoubleCalendars(driver, EnterpriseBaseObject.DESKTOP_BROWSER);
			extentTest.log(LogStatus.PASS, "PickupDateAndTime verified");
			eHome.enterAndVerifyReturnDateAndTimeOfDoubleCalendars(driver, EnterpriseBaseObject.DESKTOP_BROWSER);
			extentTest.log(LogStatus.PASS, "ReturnDateAndTime verified");
			// eHome.enterAndVerifyCoupon(driver, COUPON_CODE);
			eHome.verifyContinueButtonAndClick(driver);
			extentTest.log(LogStatus.PASS, "ContinueButton verified");
			
			CarObject car = new CarObject(driver); 
			car.clickFirstCar(driver, url, LOCATION);
			extentTest.log(LogStatus.PASS, "clickFirstCar verified");
			car.clickPayLaterButton(driver, url, LOCATION);
			extentTest.log(LogStatus.PASS, "clickPayLaterButton verified");
			
			ExtrasObject carExtra = new ExtrasObject(driver);
			carExtra.verifyPageHeaderAndPayButtons(driver);
			carExtra.verifyReviewAndPayButtonAndClick(driver);
			extentTest.log(LogStatus.PASS, "VerifyExtrasPage verified");
			
			ReservationObject reservation = new ReservationObject(driver);
			reservation.enterPersonalInfoForm(driver);
			reservation.enterFlightNumber(driver, url);
			reservation.submitReservationForm(driver);
			extentTest.log(LogStatus.PASS, "submitReservationForm verified");
			reservationNumber = reservation.getReservationNumber();
			fafw.appendToFile(calendar.getTime() + String.valueOf('\t') + reservationNumber + String.valueOf('\t') + "CREATED    " + String.valueOf('\t') + url);
			
			// Cancel reservation directly from the reserve confirmed page
			reservation.cancelReservationFromGreenLinkOnReserveConfirmed(driver);
			fafw.appendToFile(calendar.getTime() + String.valueOf('\t') + reservationNumber + String.valueOf('\t') + "CANCELLED" + String.valueOf('\t') + url);
			reservation.printLog("=== END " + className + " === " + url);
			extentTest.log(LogStatus.INFO, "=== END " + className + " === " + url);
		}catch(Exception e){
			ScreenshotFactory.captureScreenshot(driver, className);
			extentTest.log(LogStatus.FAIL, "BOOM !!! " + className + " === " + url);
			throw(e);
		}
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		extentTest.log(LogStatus.INFO, "Browser closed");
		extent.endTest(extentTest);
		MailUtility.execute(System.getProperty("user.dir")
				+ "\\extentReportFile.html");
		extent.flush();
	}
}
