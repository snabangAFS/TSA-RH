package gov.dhs.tsa.tas.serenity.bdd.pages.elis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Duration;

import gov.dhs.tsa.tas.serenity.bdd.pages.BasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;

public class ElisBasePage extends BasePage {

	@FindBy(id = "progress_inner")
	private WebElementFacade progressBox;

	@FindBy(id = "myModalLabelrequestError")
	private WebElementFacade requestErrorModalError;

	@FindBy(xpath = "//input[@id='omni-search']")
	private WebElementFacade searchReceiptNumber;

	@FindBy(xpath = "//div[contains(@class, 'omni-search-result')]")
	private WebElementFacade searchResults;

	@FindBy(id = "ctl00_explitLogout_ExplicitLogin")
	private WebElementFacade logoutButton;

	@FindBy(css = "div#blockUI blockMsg blockPage")
	// @FindBy(xpath="//a[@class='blockUI blockMsg blockPage']")
	// @FindBy(xpath="//div[contains(@class, 'blockUI blockMsg blockPage')]")
	private WebElementFacade processingBox;

	public void clickLogoutButton() {
		clickOn(logoutButton);
	}

	private void updateImplicitScriptTimeout(Duration duration) {
		getDriver().manage().timeouts().setScriptTimeout(duration.in(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);

	}

	/*
	 * 
	 * @see
	 * net.serenitybdd.core.pages.PageObject#waitForAngularRequestsToFinish()
	 * 
	 * Using BasePage.longStaticSleep() until we are confident that
	 * waitForAngularRequestsToFinish() functions properly.
	 */
	public void waitForAngularRequestsToFinish() {
		longStaticSleep();
		/*
		 * updateImplicitScriptTimeout(getImplicitWaitTimeout()); String script
		 * = "var callback = arguments[arguments.length - 1];"; script +=
		 * "angular.element(document.body).injector().get('$browser').notifyWhenNoOutstandingRequests(callback);";
		 * 
		 * ((JavascriptExecutor) this.getDriver()).executeAsyncScript(script,
		 * new Object[] {});
		 */
	}

	public void waitForHttpRequestsToFinish() {
		updateImplicitScriptTimeout(getImplicitWaitTimeout());
		int timePeriodMS = 2000;
		String pendingRequestFunction = "function getRequestCount(){return angular.element(document.body).injector().get('$http').pendingRequests.length;}";
		String retryFunction = "function waitForRequests(callback){if(getRequestCount()==0){callback(); return true;} setTimeout(function(){waitForRequests(callback)}, "
				+ timePeriodMS + "); return false;}";
		String kickoffFunction = "var callback=arguments[arguments.length-1]; waitForRequests(callback);";
		String script = pendingRequestFunction + retryFunction + kickoffFunction;

		((JavascriptExecutor) this.getDriver()).executeAsyncScript(script, new Object[] {});
	}

	public void waitForProcessing() {
		waitForElementToDisappear(progressBox);

		// The progress box doesn't necessarily mean that everything is done
		// loading.
		// As of 20160715 it's on a 450ms delay before it even shows up.
		// waitForHttpRequestsToFinish();

		if (requestErrorModalError.isCurrentlyVisible()) {
			org.junit.Assert.fail("The system error modal is present during UI test!");
		}
	}

	public void waitForPaymentProcessing() {
		waitForElementToDisappear(processingBox);
	}

	// creating a service method to look for dynamic elements with a fast-fail
	public boolean isElementPresent(String xpathSelector) {
		int seconds = (int) getWaitForTimeout().in(TimeUnit.SECONDS);
		try {
			setImplicitTimeout(0, TimeUnit.SECONDS);
			getDriver().findElement(By.xpath(xpathSelector));
			return true;
		} catch (NoSuchElementException nsee) {
			setImplicitTimeout(seconds, TimeUnit.SECONDS);
			return false;
		}
	}

	protected String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public void searchReceiptNumber(String receiptNumber) {
		typeIntoSlowly(searchReceiptNumber, receiptNumber);
		waitFor(searchResults);
		clickOn(searchResults.then("//a[text()='" + receiptNumber + "']"));
		waitForProcessing();
	}

}