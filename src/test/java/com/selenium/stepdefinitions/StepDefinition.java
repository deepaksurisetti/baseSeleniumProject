package com.selenium.stepdefinitions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.selenium.customexceptions.CustomException;
import com.selenium.drivers.DriverConstants;
import com.selenium.drivers.DriverSetup;
import com.selenium.utilities.CommonLibrary;

import io.cucumber.java.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;

/**
 * 
 * @author Deepak
 *
 */
public class StepDefinition extends DriverSetup {
	// Logger Intialization
	private static final Logger logger = LogManager.getLogger(StepDefinition.class);

	private static String scenarioName = "";
	private static String environment = "";
	private static String previousScenario = "";

	public String getFailureMessage() {
		return CommonLibrary.getValueFromGenPropFile("failureMessage");
	}

	public void setFailureMessage(String failureMessage) {
		CommonLibrary.updateValueInGenPropFile("failureMessage", failureMessage);
	}

	public String getBrowser() {
		return CommonLibrary.getValueFromGenPropFile("Browser");
	}

	public void setBrowser(String browser) {
		CommonLibrary.updateValueInGenPropFile("Browser", browser);
	}

	@Before
	public void launchBrowser() {
		try {

			String browser = System.getProperty(DriverConstants.BROWSER);
			setBrowser(browser);
			if (browser.equalsIgnoreCase(DriverConstants.CHROME))
				DriverSetup.chromeSetup();
			// else if ladder blocks can be constructed here, if we intended to use multiple
			// browsers, for now we have considered chrome alone
			else
				logger.error("Invalid Browser Name has been provided");
		} catch (Exception e) {
			setFailureMessage(e.getMessage());
			logger.fatal(e.getMessage());
			Assert.fail(e.getMessage());
		}
	}

	@After
	public void tearDown(Scenario scenario) throws CustomException {
		try {

			scenarioName = scenario.getName();
			environment = System.getProperty("Environment");
			File filePath = null;
			Boolean flag = false;

			Collection<String> tags = scenario.getSourceTagNames();
			for (String eachTag : tags) {
				if ((eachTag.contains("Regression")) || (eachTag.contains("search"))) {
					flag = true;
					filePath = new File("src/test/resources/TextReports/" + environment + ".txt");
				}
			}

			if (scenario.isFailed()) {
				logger.error(scenario.getName());
				TakesScreenshot screenShot = ((TakesScreenshot) driver);
				byte[] imageInBytes = screenShot.getScreenshotAs(OutputType.BYTES);
				scenario.embed(imageInBytes, "image/png");
			}
			driver.close();
			if (flag) {
				addDetailsToReport(scenario.isFailed(), filePath);
			}

		} catch (Exception e) {
			logger.fatal(e.getMessage());
		}
	}

	/**
	 * 
	 * @param scenarioStatus
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void addDetailsToReport(boolean scenarioStatus, File filePath) throws FileNotFoundException, IOException {

		try (FileOutputStream fileOutputStream = new FileOutputStream(filePath, true)) {
			String reportHeader = "| Scenario | Environment | Browser | Status | Failure Message |\r\n";

			String status = "";
			String failed = "| Failed |";
			String passed = "| Passed |";

			// Create a fresh file for each run, old file will be removed on each run.
//			if (!filePath.exists() && !filePath.createNewFile())
//				logger.info("New File created for Report Generation");

			if (filePath.exists()) {
				boolean flag = filePath.createNewFile();
				if (!flag)
					logger.info("New File created for Report Generation");
			}

			if (filePath.length() == 0) {
				byte[] content = reportHeader.getBytes();
				fileOutputStream.write(content);
				fileOutputStream.flush();
				logger.info("Headers has been created for Report Generation");
			}

			if (scenarioName.equals(previousScenario))
				status = "|" + " " + "|" + environment.toUpperCase() + "|" + getBrowser().toUpperCase();
			else
				status = "|" + scenarioName + "|" + environment.toUpperCase() + "|" + getBrowser().toUpperCase();

			previousScenario = scenarioName;

			if (scenarioStatus) {
				status = status.concat(failed);

				if (getFailureMessage().length() > 250)
					setFailureMessage(getFailureMessage().substring(0, 250));

				status = status.concat(getFailureMessage() + "|\r\n");
			} else {
				status = status.concat(passed);
				setFailureMessage("N/A");
				status.concat(getFailureMessage() + "|\r\n");
			}

			byte[] content = status.getBytes();
			fileOutputStream.write(content);
			fileOutputStream.flush();
			logger.info("Test Status has been captured in Report Sucessfully");
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			setFailureMessage("");
		}
	}

}
