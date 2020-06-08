package gov.dhs.tsa.rh.serenity.bdd.pages.whytsa;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/benefits")
public class RhWhyTsaBenefitsPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Competitive')]")
	private WebElementFacade benefitsPageTitle;
	
	
    public void verifyBenefitsPageTitleIsPresent() {
    	benefitsPageTitle.isDisplayed();
    	benefitsPageTitle.click();
    }
    
}

