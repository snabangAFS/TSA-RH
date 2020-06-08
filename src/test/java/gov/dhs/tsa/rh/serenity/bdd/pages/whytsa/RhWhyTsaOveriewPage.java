package gov.dhs.tsa.rh.serenity.bdd.pages.whytsa;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/why-tsa")
public class RhWhyTsaOveriewPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Serve the Nation.')]")
	private WebElementFacade whyTsaOverviewPageTitle;
	
	
    public void verifyWhyTsaOverviewPageTitleIsPresent() {
    	whyTsaOverviewPageTitle.isDisplayed();
    	whyTsaOverviewPageTitle.click();
    }
    
}

