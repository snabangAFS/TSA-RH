package gov.dhs.tsa.tas.serenity.bdd.pages.elis;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.Duration;

import gov.dhs.tsa.tas.serenity.bdd.pages.BasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;

public class ElisErrorModal extends ElisBasePage {

	@FindBy(id = "requestError")
	private WebElementFacade requestErrorModal;
	@FindBy(id = "myModalCloserequestError")
	private WebElementFacade closeRequestErrorButton;

	public void closeRequestError() {
		requestErrorModal.setImplicitTimeout(new Duration(BasePage.getImplicitModalWaitTime(), TimeUnit.SECONDS));
		if (requestErrorModal.isPresent() && requestErrorModal.isVisible())
			clickOn(closeRequestErrorButton);
		else
			System.out.println("Could not find " + requestErrorModal);
		requestErrorModal.resetTimeouts();
	}

}
