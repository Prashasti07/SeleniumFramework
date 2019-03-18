package com.sap.llk.base;

import java.io.File;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import com.sap.llk.util.TestUtil;

public class SendEmailReport extends TestBase {

	// private static final String REPORT_PATH = System.getProperty("user.dir")
	// + "/report/LLK_MAIN_UI_AutomationReport.html";
	public static int cntPass;
	public static int cntFail;
	public static int cntSkip;
	public static String executionTime;
	public static String env;
	public static String oldFileName;
	public static String newFileName;
	public static String version;

	public static void sendEmail() throws EmailException, MalformedURLException {
		env = TestUtil.getEnvironment();

		cntPass = MyTestReporter.cntPass;
		cntFail = MyTestReporter.cntFail;
		cntSkip = MyTestReporter.cntSkip;
		executionTime = MyTestReporter.executionTime;

		System.out.println("Old Report File path : " + reportName);
		File f = new File(reportName);
		oldFileName = f.getName();
		System.out.println("Old File Name: " + oldFileName);
		System.out.println("Old File Path: " + f.getAbsolutePath());
		System.out.println(f.getParent());

		newFileName = env + "_" + oldFileName;
		System.out.println("New File Name: " + newFileName);
		String newFilePath = f.getParent() + "/" + newFileName;
		File f1 = new File(newFilePath);
		if (f.renameTo(f1)) {
			System.out.println("File rename success");
			;
		} else {
			System.out.println("File rename failed");
		}
		System.out.println("New File Path: " + f1.getAbsolutePath());

		Date currentDate = new Date();
		String dateToStr = DateFormat.getDateInstance().format(currentDate);
		System.out.println("Sending Email.....");
		EmailAttachment attachment = new EmailAttachment();
		// attachment.setPath(REPORT_PATH);
		attachment.setPath(newFilePath);
		attachment.setDisposition(EmailAttachment.ATTACHMENT);
		// attachment.setDescription("PPS-UI-Automation-Report.html");
		attachment.setDescription(f1.getName());
		// attachment.setName("PPS_UI_AutomationReport.html");
		attachment.setName(f1.getName());

		// Create the email message
		// MultiPartEmail email = new MultiPartEmail();
		HtmlEmail email = new HtmlEmail();
		email.setHostName("mailhost.wdf.sap.corp");
		email.setSmtpPort(25);

		email.setFrom("donotreply@sap.com", "DoNotReply");
		email.setSubject("Live Link - Admin Portal : " + env + " : " + dateToStr + " : LLK Admin UI Test Automation");

		email.setMsg("<font face='verdana' size='3'>" + "Hi All," + System.lineSeparator() + System.lineSeparator()
				+ "Here is the Live Link - Admin Portal UI Test Automation Execution Report on the " + env
				+ " environment - " + prop.getProperty("url") + System.lineSeparator() + System.lineSeparator()
				+ "Below is the test execution summary: " + System.lineSeparator()
				+ "-------------------------------------------" + "</font>"
				+ "<font face='verdana' size='3' color='green'>" + System.lineSeparator()
				+ "Number of Test Cases Passed : " + "\t" + cntPass + System.lineSeparator() + "</font>"
				+ "<font face='verdana' size='3' color='red'>" + "Number of Test Cases Failed : " + "\t" + cntFail
				+ System.lineSeparator() + "</font>" + "<font face='verdana' size='3' color='orange'>"
				+ "Number of Test Cases Skipped : " + "\t" + cntSkip + System.lineSeparator() + "</font>"
				+ "<font face='verdana' size='3'>" + "Total execution time for tests : " + "\t" + executionTime
				+ System.lineSeparator() + System.lineSeparator() + System.lineSeparator()
				+ "This email was sent automatically by Live Link UI Test Automation Framework. Please do not reply."
				+ System.lineSeparator() + System.lineSeparator() + "Thanks," + System.lineSeparator()
				+ System.lineSeparator() + "Automation Team" + "</font>");

		// add the attachment
		email.attach(attachment);

		// if tests are failing then send the email otherwise don't send
		/*
		 * email.send(); System.out.println("Email Sent Successfully....");
		 */
		if (cntFail > 2) {
			email.addTo("prashasti.porwal@sap.com", "Prashasti Porwal");

		/*	email.addCc("amol.nawale@sap.com", "Amol Nawale");
			email.addCc("michael05.smith@sap.com", "Smith Michael");
			email.addCc("mohit.goel@sap.com", "Mohit Goel");
			email.addCc("bhomik.pande@sap.com", "Bhomik Pande");
			email.addCc("fernando.nakano@sap.com", "Fernando Nakano");
			email.addCc("khalid.abdullah@sap.com", "Abdullah Khalid");
			email.addCc("steven.garcia@sap.com", "Steven Garcia");
			email.addCc("pablo.silva01@sap.com", "Pablo Silva");
			email.addCc("augusto.caetano.de.jesus@sap.com", "Augusto Caetano De Jesus");
			email.addCc("sachin.kesarkar@sap.com", "Sachin Kesarkar");
			email.addCc("hector.rosales.marquez@sap.com", "Hector Rosales Marquez");
			email.addCc("texla.castillo@sap.com", "Texla Castillo");
			email.addCc("c.salgado@sap.com", "Carlos Salgado");
			email.addCc("rafael.madrid@sap.com", "Rafael Madrid");
			email.addCc("gabriel.frei@sap.com", "Gabriel Frei");
			email.addCc("meenal.prasad@sap.com", "Meenal Prasad");
*/
			email.send();
			System.out.println("Email Sent Successfully....");
		} else {
			email.addTo("prashasti.porwal@sap.com", "Prashasti Porwal");
		//	email.addCc("c.salgado@sap.com", "Carlos Salgado");
			email.send();
			System.out.println("All the tests are passing, no need to send email.");
		}

	}

	public static String getLatestGeneratedFilePath(String dirPath) {
		File choice = null;
		try {
			File dir = new File(dirPath);
			File[] files = dir.listFiles();
			if (files == null || files.length == 0) {
				return null;
			}
			// Thread.sleep(60000);
			long lastMod = Long.MIN_VALUE;

			for (File file : files) {
				if (file.lastModified() > lastMod) {
					choice = file;
					lastMod = file.lastModified();
				}
			}
		} catch (Exception e) {
			System.out.println("Exception while getting the last download file");
			e.printStackTrace();
		}
		// test.log(LogStatus.INFO, "The latest generated file is " +
		// choice.getPath());
		System.out.println(choice.getAbsolutePath());
		return choice.getAbsolutePath();
	}
}
