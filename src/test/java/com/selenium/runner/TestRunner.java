
package com.selenium.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.selenium.customexceptions.CustomException;
import com.selenium.reports.CustomReporting;
import com.selenium.utilities.CommonLibrary;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

/**
 * 
 * @author Deepak
 *
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/Features/", glue = { "com.selenium.stepdefinitions" }, plugin = {
		"pretty", "html:target/cucumber-html-report",
		"json:target/cucumber-reports/Cucumber.json" }, dryRun = false, monochrome = true)
public class TestRunner {

	// We can include Logic for custom reports generation (i.e) Extent Reports ,
	// Kibana/ Grafana Dashbaord Integration etc ,.

	// We can also initialize any vault, if we intended to store db creds or any
	// other secrets

	private static final Logger logger = LogManager.getLogger(TestRunner.class);

	private static String startTime = "";

	private TestRunner() {
		super();
	}

	@BeforeClass
	public static void cleanUpOldReports() {
		try {
			// This Cleans up the files present in the mentioned folder
			FileUtils.cleanDirectory(new File("src/test/resources/TextReports/"));
			logger.info("Text Report File has been deleted");
			// Starttime is captured
			startTime = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss").format(new Date());

		} catch (FileNotFoundException e) {
			throw new CustomException(e.getMessage());
		} catch (Exception e) {
			throw new CustomException(e.getMessage());
		}
	}

	@AfterClass
	public static void generateReports() {

		try {
			// Endtime is captured
			String endTime = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss").format(new Date());
			String reportPath = "src/test/resources/TextReports/" + System.getProperty("Environment") + ".txt";
			CustomReporting reporting = new CustomReporting();
			// Custom Emailable Report is generated with all execution status
			reporting.reportGeneration(reportPath, startTime, endTime, "Automation Summary");
		} catch (Exception e) {
			logger.warn("Error While generating the report after execution");
		} finally {
			CommonLibrary.killAllDriverProcess();
		}
	}

}
