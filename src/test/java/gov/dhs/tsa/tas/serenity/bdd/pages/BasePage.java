package gov.dhs.tsa.tas.serenity.bdd.pages;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;

import net.serenitybdd.core.Serenity;
import net.serenitybdd.core.pages.HtmlTag;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.pages.Pages;

public class BasePage extends PageObject {

	private final static String XPATH_TRANSLATE_STRING = generateXPathString();
	private final static int IMPLICIT_MODAL_WAIT_TIME = 4;

	private static String generateXPathString() {
		try {
			return java.net.URLDecoder.decode(
					"%27ABCDEFGHIJKLMNOPQRSTUVWXYZ%C3%80%C3%81%C3%82%C3%83%C3%84%C3%85%C3%86%C3%87%C3%88%C3%89%C3%8A%C3%8B%C3%8C%C3%8D%C3%8E%C3%8F%C3%90%C3%91%C3%92%C3%93%C3%94%C3%95%C3%96%C3%98%C3%99%C3%9A%C3%9B%C3%9C%C3%9D%C3%9E%C5%B8%C5%BD%C5%A0%C5%92%27%2C+%27abcdefghijklmnopqrstuvwxyz%C3%A0%C3%A1%C3%A2%C3%A3%C3%A4%C3%A5%C3%A6%C3%A7%C3%A8%C3%A9%C3%AA%C3%AB%C3%AC%C3%AD%C3%AE%C3%AF%C3%B0%C3%B1%C3%B2%C3%B3%C3%B4%C3%B5%C3%B6%C3%B8%C3%B9%C3%BA%C3%BB%C3%BC%C3%BD%C3%BE%C3%BF%C5%BE%C5%A1%C5%93%27",
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void visitPage() {
		this.open();
	}

	public void clearBrowserCookies() {
		getDriver().manage().deleteAllCookies();
	}

	public void shutdownWebDriverSession() {
		getDriver().quit();
	}

	public boolean tableHasValue(WebElementFacade table, Integer columnNumber, String value) {
		HashMap<Integer, String> expectedValues = new HashMap<Integer, String>();
		expectedValues.put(columnNumber, value);
		return tableHasRowWithValues(table, expectedValues, false);
	}

	public boolean tableHasMatchingRows(WebElementFacade table, Integer columnNumber, Integer expectedNumberOfMatches,
			String value) {

		return tableHasMatchingRows(table, columnNumber, value, false) == expectedNumberOfMatches;
	}

	/*
	 * Uses xpath to search a table for a unique match. Useful if you wish to
	 * ensure there are no duplicate cards produced, case actions created, etc.
	 */
	public Integer tableHasMatchingRows(WebElementFacade table, Integer columnNumber, String value,
			boolean caseSensitive) {
		String xpath = ".//tr[";
		if (caseSensitive) {
			xpath += "contains(td[" + columnNumber + "],'" + value + "')";
		} else {
			xpath += "contains(translate(td[" + columnNumber + "], " + XPATH_TRANSLATE_STRING + "), '"
					+ value.toLowerCase() + "')";
		}
		xpath += "]";
		System.out.println("Running:" + xpath);
		List<WebElement> foundElements = table.findElements(By.xpath(xpath));
		return foundElements.size();
	}

	/*
	 * Uses xpath to search a table for a given entry. Pass in a
	 * HashMap<Integer, String> indicating the column numbers and corresponding
	 * values you expect. Returns true if the entry exists. For example: find a
	 * row where column 3 equals 'I-90' and column 6 equals 'Ready for
	 * Adjudication'.
	 * 
	 * nameOfHashMap.put(3, 'I-90'); nameOfHashMap.put(6, 'Ready for
	 * Adjudication');
	 * 
	 * tableHasValue(table, nameOfHashMap, trueOrFalseCaseSensitive)
	 */
	public boolean tableHasRowWithValues(WebElementFacade table, HashMap<Integer, String> columnNumberAndValue,
			boolean caseSensitive) {
		String xpath = ".//tr[";
		boolean first = true;
		for (Entry<Integer, String> entry : columnNumberAndValue.entrySet()) {
			if (!first) {
				xpath += " and ";
			} else {
				first = false;
			}

			if (caseSensitive) {
				xpath += "contains(td[" + entry.getKey() + "],'" + entry.getValue() + "')";
			} else {
				xpath += "contains(translate(td[" + entry.getKey() + "], " + XPATH_TRANSLATE_STRING + "), '"
						+ entry.getValue().toLowerCase() + "')";
			}
		}
		xpath += "]";
		System.out.println("Running:" + xpath);
		return table.findElements(By.xpath(xpath)).isEmpty() == false;
	}

	public void longStaticSleep() {
		try {
			Thread.sleep(7000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sleeps for an arbitrary amount of seconds. Sometimes needed due to
	 * Javascript loading.
	 * 
	 * @param seconds
	 *            The number of seconds to sleep (greater than 0).
	 */
	public static void safeSleep(final int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void enterValueIfNotNull(String value, WebElementFacade field) {
		if (value != null) {
			field.type(value);
		}
	}

	// XPath is 30x faster than using getSelectOptions() and iterating manually
	// for a target text string
	public List<WebElement> rapidSelectMatch(WebElement selectField, String optionText, boolean caseSensitive) {

		String caseInsensitiveXPath = "translate(text(), " + XPATH_TRANSLATE_STRING + ")";
		String caseSensitiveXPath = "text()";

		String textPath = null;
		if (caseSensitive) {
			textPath = caseSensitiveXPath;
		} else {
			textPath = caseInsensitiveXPath;
			optionText = optionText.toLowerCase();
		}
		String xpath = "./option[starts-with(" + textPath + ", '" + optionText + "')]";
		System.out.println("Searching for options matching " + xpath);
		List<WebElement> matchingOptions = selectField.findElements(By.xpath(xpath));
		return matchingOptions;
	}

	public void rapidSelect(WebElementFacade selectField, String optionText, boolean caseSensitive) {
		List<WebElement> elements = rapidSelectMatch(selectField, optionText, caseSensitive);
		selectField.selectByValue(elements.get(0).getAttribute("value"));
	}

	public void typeSlowly(String s, WebElementFacade element) {
		element.clear();
		for (int i = 0; i < s.length(); i++) {
			element.sendKeys(s.substring(i, i + 1));
			waitABit(250);
		}
	}

	public void acceptJSConfirmBox() {
		getJavascriptExecutorFacade().executeScript("window.confirm = function(msg){return true;}");
	}

	/*
	 * Scales the implicit timeout of an element by the given multiplier
	 */
	public void scaleImplicitTimeout(WebElementFacade e, double multiplier) {
		Duration d = new Duration((long) (getImplicitWaitTimeout().in(TimeUnit.MILLISECONDS) * multiplier),
				TimeUnit.MILLISECONDS);
		e.setImplicitTimeout(d);
	}

	public static <T> T findMatchingString(String targetString, Collection<T> collection, boolean caseSensitive) {
		List<T> matchingItems = new ArrayList<T>();
		for (T item : collection) {
			String itemString = item.toString();
			String target = targetString;
			if (!caseSensitive) {
				itemString = itemString.toLowerCase();
				target = target.toLowerCase();
			}
			if (itemString.contains(target)) {
				matchingItems.add(item);
			}
		}
		if (matchingItems.isEmpty())
			return null;

		return matchingItems.get(0);
	}

	public void simulatedClick(WebElementFacade element) {
		element.waitUntilClickable();
		element.sendKeys(Keys.ENTER);
	}

	public void gentleType(String s, WebElementFacade field) {
		if (s == null || s.equals(""))
			return;
		field.waitUntilEnabled();
		clickOn(field);
		field.sendKeys(s);
	}

	public String getValueOfField(WebElementFacade element) {
		return (String) getJavascriptExecutorFacade().executeScript("arguments[0].value", element);
	}

	public void waitForElementToDisappear(WebElementFacade element) {
		Duration oldTimeout = getImplicitWaitTimeout();
		try {
			long endTime = System.currentTimeMillis() + 10000;

			setImplicitTimeout(IMPLICIT_MODAL_WAIT_TIME, TimeUnit.SECONDS);
			while (element.isVisible()) {
				waitABit(100);
				if (System.currentTimeMillis() > endTime) {
					Assert.fail("Element took too long to disappear!");
				}
			}
		} finally {
			setImplicitTimeout((int) oldTimeout.in(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
		}

	}

	public static int getImplicitModalWaitTime() {
		return IMPLICIT_MODAL_WAIT_TIME;
	}

	@Override
	public void clickOn(WebElement e) {
		element(e).waitUntilClickable();
		logElement(element(e), "JavaScript click");
		getJavascriptExecutorFacade().executeScript("arguments[0].click()", e);
	}

	@Override
	public void typeInto(WebElement field, String value) {
		typeWithRetry(element(field), value, false);
	}

	public void typeIntoSlowly(WebElement field, String value) {
		typeWithRetry(element(field), value, true);
	}

	private void typeWithRetry(WebElementFacade field, String value, boolean typeSlowly) {
		int count = 0;
		do {
			field.clear();
			if (typeSlowly) {
				typeSlowly(value, element(field));
			} else {
				field.type(value);
			}
			count++;
		} while (!field.getValue().equals(value) && count < 10);
	}

	public void logElement(WebElementFacade element, String s) {
		LoggerFactory.getLogger(BasePage.class)
				.info(HtmlTag.from(element).inHumanReadableForm() + ":" + "javascript click");
	}

	public static WebElement waitForClickableElement(final By by, final WebDriver driver, final long timeOutInSeconds) {
		try {
			return new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.elementToBeClickable(by));
		} catch (TimeoutException e) {
			throw new IllegalStateException("Could not find visible element by " + by + " when waiting for it for "
					+ timeOutInSeconds + " seconds", e);
		}
	}

	/**
	 * Open the webdriver browser using a URL template and paramaterized URL.
	 * 
	 * @param urlTemplateName
	 *            is represented using @NamedUrl. @NamedUrls( {
	 * @NamedUrl(name = "open.issue", url =
	 *                "http://jira.mycompany.org/issues/{1}") } )
	 * @param parameterValues
	 *            are represented in the URL using {0}, {1}, etc.
	 */
	public void gotoPage(String urlTemplateName, String[] parameterValues) {
		this.open(urlTemplateName, parameterValues);
	}

	public String getReceiptNumber() {
		return Serenity.sessionVariableCalled("receiptNumber").toString();
	}

	public String getBenefitType() {
		return Serenity.sessionVariableCalled("benefitType").toString();
	}

	public void resetBaseUrl(String newUrl) {
		System.setProperty("webdriver.base.url", newUrl);
		Pages myPages = Serenity.sessionVariableCalled("myPages");
		myPages.getConfiguration().getEnvironmentVariables()
				.setProperty(ThucydidesSystemProperty.WEBDRIVER_BASE_URL.getPropertyName(), newUrl);
		System.out.println("Changing base.url to : " + newUrl);
	}

}