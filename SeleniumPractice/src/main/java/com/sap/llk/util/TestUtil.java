package com.sap.llk.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.sap.llk.base.TestBase;
import com.warrenstrange.googleauth.GoogleAuthenticator;

public class TestUtil extends TestBase {

	public static long PAGE_LOAD_TIMEOUT = 25;
	public static long IMPLICIT_WAIT = 25;

	public static final String CREATE_ADMIN_ACCOUNT_SUCCESS_MSG = "The account was created successfully! Click 'OK' button to continue.";
	public static final String SAVE_ACCOUNT_CHANGES_CONFIRMATION_MSG = "You have made some changes. Review them before saving.";
	public static final String DELETE_ADMIN_ACCOUNT_CONFIRMATION_MSG = "Selected Accounts will be permanently erased, are you sure you want to continue?";
	public static final String DELETE_PHONE_NUMBER_CONFIRMATION_MSG = "Selected Phone Numbers will be permanently erased, are you sure you want to continue?";
	public static final String CREATE_USER_SUCCESS_MSG = "The user was created successfully! Click 'OK' button to continue.";
	public static final String EDIT_USER_SUCCESS_MSG = "The user was successfully updated! Click 'OK' button to continue.";
	public static final String DELETE_USER_CONFIRMATION_MSG = "Selected Users will be permanently erased, are you sure you want to continue?";
	public static final String CREATE_TRIAL_ACCOUNT_SUCCESS_MSG = "The trial account was created successfully! Click 'OK' button to continue.";
	public static final String SAVE_TRIAL_ACCOUNT_CHANGES_CONFIRMATION_MSG = "Are you sure you want to save the trial account changes?";
	public static final String DELETE_TRIAL_ADMIN_ACCOUNT_CONFIRMATION_MSG = "Selected trial account will be permanently erased, are you sure you want to continue?";
	public static final String TPOA_WARNING="No checks will be made that the TPOA is valid for this customer. Only use for select Enterprise customers.";
	
	public static void takeScreenshotAtEndOfTest() throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String currentDir = System.getProperty("user.dir");

		FileUtils.copyFile(scrFile, new File(currentDir + "/screenshots/" + System.currentTimeMillis() + ".png"));

	}

	@SuppressWarnings("deprecation")
	public static String randomf() {
		Random r = new Random();
		int num = r.nextInt(300);
		Date d = new Date();
		return String.valueOf(num) + "_" + d.getDate() + d.getYear();
	}

	public static String randomSAPUserID() {
		Random r = new Random();
		int num = r.nextInt(899999) + 100000;
		String sapUserID = "T" + String.valueOf(num);
		System.out.println("random SAP User ID is: " + sapUserID);
		return sapUserID;
	}

	public static String randomName() {
		Random r = new Random(); // just create one and keep it around
		String alphabet = "abcdefghijklmnopqrstuvwxyz";

		final int N = 8;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
		}
		String randomName = sb.toString();
		System.out.println(randomName);
		return randomName;
	}

	public static String randomAgentKey() {
		Random r = new Random(); // just create one and keep it around
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

		final int N = 10;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
		}
		String randomAgentKey= sb.toString();
		System.out.println("Random Agent Key is : " + randomAgentKey);
		return randomAgentKey;
	}
	
	public static String randomReportID() {
		Random r = new Random(); // just create one and keep it around
		String alphabet = "0123456789";

		final int N = 6;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
		}
		String randomReportID= sb.toString();
		System.out.println("Random Agent ID is : " + randomReportID);
		return randomReportID;
	}

/*	public static String randomPhoneNumber() {
	//	long num = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		System.out.println("The Random Phone Number is: " + num);
		String number = String.valueOf(num);
		return number;
	}
*/
	public static String getContractEffectiveDate() {
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
		System.out.println("today: " + today);
		calendar.add(Calendar.DAY_OF_YEAR, 2);
		Date tomo = calendar.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("MMM dd, yyyy");
		String tomorrow = formater.format(tomo);
		System.out.println("Tommorow is : " + tomorrow);
		return tomorrow;
	}

	public static String getEnvironment() throws MalformedURLException {
		URL url = new URL(prop.getProperty("url"));
		String urlStr = prop.getProperty("url");
		String hostname = url.getHost();
		String env = null;
		if (hostname.equals("livelink.sapmobileservices.com")) {
			env = "PROD";
		} else if (hostname.equals("demo-livelink.sapmobileservices.com") || urlStr.contains("172.24.227.31")) {
			env = "DEMO";
		} else if (hostname.equals("llbpal55.pal.sap.corp")) {
			env = "QAS";
		} else {
			System.out.println("Please specify the correct environment");
		}
		return env;

	}

	// Extract QR Code value
	public static String extractQRCodeValue(WebElement element) throws IOException, NotFoundException {
		String url = element.getAttribute("src");
		System.out.println("QR Code Image URL is : " + url);
		URL urlOfImage = new URL(url);
		BufferedImage bf = ImageIO.read(urlOfImage);
		LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bf);
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
		Result result = new MultiFormatReader().decode(binaryBitmap);
		String textInQRCode = result.getText();
		System.out.println("Text in QR Code is : " + textInQRCode);
		return textInQRCode;
	}

	// Extract QR Code using Google Authenticator
	public static String extractQRCode(String regCode) {
		GoogleAuthenticator ga = new GoogleAuthenticator();
		int token = ga.getTotpPassword(regCode);
		System.out.println("The QR Code value is : " + token);
		String str = String.valueOf(token);
		return str;
	}
}
