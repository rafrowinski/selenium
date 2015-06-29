package com.maven.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

public class GoogleTest extends AbstractTest {

	protected String pageUrl = "http://www.google.pl/ncr";
	protected String routeToTest = "";

	public GoogleTest(String browser) {
		super(browser);
	}

	@Before
	public void Init() throws InterruptedException {
		Init(pageUrl, routeToTest);
	}

	@Test
	public void googleTitleTest() throws InterruptedException {
		WebElement q = find.byName("q");
		q.sendKeys("Hello world");
		q.submit();
		Thread.sleep(1000);
		assertEquals("Hello world - Google Search", getTitle());
	}
}
