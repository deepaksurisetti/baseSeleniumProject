package com.selenium.drivers;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.selenium.constants.Constants;

/**
 * 
 * @author Deepak
 *
 */
public class DriverSetup {

	public static WebDriver driver;

	// Intialization of Logger
	public static final Logger logger = LogManager.getLogger(DriverSetup.class);

	/**
	 * This method initiates the Google Chrome browser Driver
	 * 
	 * @return
	 */
	public static WebDriver chromeSetup() {

		try {
			File file = new File(DriverConstants.CHROME_DRIVER_PATH);
			System.setProperty(DriverConstants.CHROME_WD_PROPERTY, file.getAbsolutePath());
			HashMap<String, Object> chromePreferences = new HashMap<>();
			chromePreferences.put(DriverConstants.CHROME_PREF_DEFAULT_CONTENT, 0);
			logger.info("File Download Path -->");
			chromePreferences.put(DriverConstants.CHROME_PREF_DEFAULT_DIR, Constants.FILE_DOWNLOAD_PATH);
			// To Disable the download Dialog Box in Chrome
			chromePreferences.put(DriverConstants.CHROME_ENABLE_AUTO_DOWN, false);
			boolean headlessMode = Boolean.parseBoolean(System.getProperty(DriverConstants.HEADLESS));
			logger.info("Headless Mode is -->" + String.valueOf(headlessMode));

			// Chrome Options
			ChromeOptions chromeOptions = new ChromeOptions().setAcceptInsecureCerts(true) // accept insecure certs
					.setHeadless(headlessMode) // to enable/disable headless mode run
					.setExperimentalOption(DriverConstants.CHROME_PREFS, chromePreferences).addArguments(
							DriverConstants.INCOGNITO, DriverConstants.LOG_LEVEL_SEVERE, DriverConstants.WIN_SIZE_1920);
			// launch browser in remote webdriver if GRID is Yes
			if (Constants.YES.equalsIgnoreCase(System.getProperty(Constants.GRID))) {
				chromeOptions.setCapability("browserName", "chrome");
				chromeOptions.setCapability("platform", Platform.ANY);
				driver = new RemoteWebDriver(new URL("Specify Node Url"), chromeOptions);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				logger.info("Chrome Browser is Successfully Loaded in GRID");
			} else {
				driver = new ChromeDriver(chromeOptions);
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				logger.info("Chrome Browser is Successfully Loaded in Local Machine");

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return driver;
	}

}
