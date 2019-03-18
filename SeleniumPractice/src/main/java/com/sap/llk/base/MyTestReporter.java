package com.sap.llk.base;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.mail.EmailException;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

public class MyTestReporter implements IReporter {

	public static int cntPass;
	public static int cntFail;
	public static int cntSkip;
	public static long executionTimeInMilliSec;
	public static String executionTime;

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {

		// Iterating over each suite included in the test
		for (ISuite suite : suites) {

			// Following code gets the suite name
			String suiteName = suite.getName();

			// Getting the results for the said suite
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (ISuiteResult sr : suiteResults.values()) {
				ITestContext tc = sr.getTestContext();
				cntPass = tc.getPassedTests().getAllResults().size();
				System.out.println(
						"Passed tests for suite '" + suiteName + "' is:" + tc.getPassedTests().getAllResults().size());
				cntFail = tc.getFailedTests().getAllResults().size();
				System.out.println(
						"Failed tests for suite '" + suiteName + "' is:" + tc.getFailedTests().getAllResults().size());
				cntSkip = tc.getSkippedTests().getAllResults().size();
				System.out.println("Skipped tests for suite '" + suiteName + "' is:"
						+ tc.getSkippedTests().getAllResults().size());
				executionTimeInMilliSec = tc.getEndDate().getTime() - tc.getStartDate().getTime();
				System.out.println("Total excution time for test '" + tc.getName() + "' is:"
						+ (tc.getEndDate().getTime() - tc.getStartDate().getTime()));
			}
		}
		System.out.println("cntPass= " + cntPass);
		System.out.println("cntFail= " + cntFail);
		System.out.println("cntSkip= " + cntSkip);
		System.out.println("executionTimeInMilliSec= " + executionTimeInMilliSec);

		long hours = TimeUnit.MILLISECONDS.toHours(executionTimeInMilliSec);
		executionTimeInMilliSec -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(executionTimeInMilliSec);
		executionTimeInMilliSec -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(executionTimeInMilliSec);

		executionTime = hours + " hours," + minutes + " mins," + seconds + " seconds";
		System.out.println("executionTime= " + executionTime);

		try {
			SendEmailReport.sendEmail();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

}
