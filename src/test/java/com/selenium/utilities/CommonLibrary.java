package com.selenium.utilities;

import java.io.FileReader;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import com.google.common.base.Function;
import com.selenium.drivers.DriverSetup;

/**
 * 
 * @author Deepak
 *
 */
public class CommonLibrary extends DriverSetup {

	public static final Logger logger = LogManager.getLogger(CommonLibrary.class);

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static Properties readPropertFile(String filePath) {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(filePath));
			return properties;
		} catch (Exception e) {
			Assert.fail("Property File Read Error");
		}
		return null;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static void updateValueInGenPropFile(String key, String value) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(
					"src\\test\\resources\\PropertyFiles\\general.properties");
			config.setProperty(key, value);
			config.save();
		} catch (ConfigurationException e) {
			logger.fatal("ConfigurationException in Gen Prop File");
		} catch (Exception e) {
			logger.fatal("Error in Updating Gen Prop File");
		}
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public static String getValueFromGenPropFile(String key) {
		try {
			PropertiesConfiguration config = new PropertiesConfiguration(
					"src\\test\\resources\\PropertyFiles\\general.properties");
			return config.getProperty(key).toString();

		} catch (ConfigurationException e) {
			logger.fatal("ConfigurationException in Gen Prop File");
		} catch (Exception e) {
			logger.fatal("Error in Updating Gen Prop File");
		}
		return null;
	}

	/**
	 * This method kills the current running driver threads and also quits the
	 * driver
	 */
	public static void killAllDriverProcess() {

		try {
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
			Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");

			logger.info("Killed All Driver Process");
			driver.quit();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 
	 * @param by
	 * @param timeOut
	 * @return
	 */
	public WebElement isExist(final By by, int timeOut) {

		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
					.pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class)
					.ignoring(WebDriverException.class).ignoring(StaleElementReferenceException.class);
			return wait.until(new Function<WebDriver, WebElement>() {

				@Override
				public WebElement apply(WebDriver webDriver) {
					return driver.findElement(by);
				}
			});
		} catch (TimeoutException e) {
			return null;
		}

	}
}