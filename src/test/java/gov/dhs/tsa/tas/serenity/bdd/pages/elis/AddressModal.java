package gov.dhs.tsa.tas.serenity.bdd.pages.elis;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;

public class AddressModal extends ElisBasePage {

	public enum mailingAddressPreference {
		STANDARD, ORIGINAL
	}

	@FindBy(id = "standardAddressModal")
	private WebElementFacade mailingAddressPreferenceModal;
	@FindBy(id = "useUserEntedreAddress")
	private WebElementFacade useEnteredAddressButton;
	@FindBy(id = "standardAddressModal")
	private WebElementFacade standardAddressModal;

	public boolean isStandardAddressModalVisible() {
		return standardAddressModal.isVisible();
	}

	public boolean isUseEnteredAddressButtonVisible() {
		return useEnteredAddressButton.isVisible();
	}

	public void useEnteredAddress(WebElementFacade panel) {
		if (useEnteredAddressButton.isVisible()) {
			clickOn(useEnteredAddressButton);
		}
		standardAddressModal.waitUntilNotVisible();
	}

}