package com.maven.test;



import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parameterized.class)
public abstract class AbstractTest {
	@Parameterized.Parameters(name = " {0} ")
	public static Iterable<Object[]> primeNumbers() {
		return Arrays.asList(new Object[][] { { "firefox" }, { "chrome" },
				{ "internet explorer" } });
	}

	// TODO: zmienić kiedyś na enum + wczytywanie z pliku
	protected String pageUrl;
	protected final String browser;
	protected String routeToTest;
	protected Find find;
	protected FindList findList;
	protected Wait wait;
	protected final int waitTime = 30;
	protected WebDriver webDriver;
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected AbstractTest(String browser) {
		this.browser = browser;
	}

	protected void Init(String pageUrl, String routeToTest)
			throws InterruptedException {
		this.webDriver = resolveWebDriver(browser);
		this.webDriver.get(pageUrl + routeToTest);
		this.find = new Find(webDriver);
		this.findList = new FindList(webDriver);
		this.wait = new Wait(webDriver, waitTime);
		waitForDocumentToLoad();
		logger.info("Beginning testing with " + browser);
	}

	protected void waitForDocumentToLoad() throws InterruptedException {
		while (!executeJavaScript("return document.readyState").equals(
				"complete")) {
			Thread.sleep(100);
		}
	}

	protected Object executeJavaScript(String javascript) {
		if (webDriver instanceof JavascriptExecutor) {
			return ((JavascriptExecutor) webDriver).executeScript(javascript);
		} else {
			throw new RuntimeException("javascript excecutor not supported!");
		}
	}

	private WebDriver resolveWebDriver(String browser) {
		switch (browser) {
		case "firefox":
			return new FirefoxDriver();
		case "chrome":
			setupChromeDriver();
			return new ChromeDriver();
		case "internet explorer":
			setupIEDriver();
			return new InternetExplorerDriver();
		default:
			return new HtmlUnitDriver();
		}
	}
	
	protected void setupChromeDriver() {
		URL url = this.getClass().getResource("chromedriver.exe");
		File file = new File(url.getPath());
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		
	}
	
	protected void setupIEDriver() {
		URL url = this.getClass().getResource("IEDriverServer.exe");
		File file = new File(url.getPath());
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		
	}

	protected String getTitle() {
		return webDriver.getTitle();
	}

	protected final class Find {
		private WebDriver webDriver;

		public Find(WebDriver webDriver) {
			this.webDriver = webDriver;
		}

		public WebElement byClassName(String className) {
			return webDriver.findElement(By.className(className));
		}

		public WebElement byCssSelector(String selector) {
			return webDriver.findElement(By.cssSelector(selector));
		}

		public WebElement byId(String id) {
			return webDriver.findElement(By.id(id));
		}

		public WebElement byLinkText(String linkText) {
			return webDriver.findElement(By.linkText(linkText));
		}

		public WebElement byName(String name) {
			return webDriver.findElement(By.name(name));
		}

		public WebElement byPartialLinkText(String linkText) {
			return webDriver.findElement(By.partialLinkText(linkText));
		}

		public WebElement byTagName(String name) {
			return webDriver.findElement(By.tagName(name));
		}

		public WebElement byXpath(String xpath) {
			return webDriver.findElement(By.xpath(xpath));
		}

		public WebElement by(By by) {
			return webDriver.findElement(by);
		}
	}

	protected final class FindList {
		private WebDriver webDriver;

		public FindList(WebDriver webDriver) {
			this.webDriver = webDriver;
		}

		public List<WebElement> byClassName(String className) {
			return webDriver.findElements(By.className(className));
		}

		public List<WebElement> byCssSelector(String selector) {
			return webDriver.findElements(By.cssSelector(selector));
		}

		public List<WebElement> byId(String id) {
			return webDriver.findElements(By.id(id));
		}

		public List<WebElement> byLinkText(String linkText) {
			return webDriver.findElements(By.linkText(linkText));
		}

		public List<WebElement> byName(String name) {
			return webDriver.findElements(By.name(name));
		}

		public List<WebElement> byPartialLinkText(String linkText) {
			return webDriver.findElements(By.partialLinkText(linkText));
		}

		public List<WebElement> byTagName(String name) {
			return webDriver.findElements(By.tagName(name));
		}

		public List<WebElement> byXpath(String xpath) {
			return webDriver.findElements(By.xpath(xpath));
		}

		public List<WebElement> by(By by) {
			return webDriver.findElements(by);
		}
	}

	protected class Wait {
		private WebDriverWait wait;

		public Wait(WebDriver webDriver, int waitTime) {
			wait = new WebDriverWait(webDriver, waitTime);
		}

		public void toBeClickable(WebElement element) {
			wait.until(ExpectedConditions.elementToBeClickable(element));
		}

		// public <T> void until(final Predicate<WebDriver> isTrue) {
		// wait.until(isTrue);
		// }
	}

	@After
	public void closeBrowser() {
		webDriver.close();
	}
}
