package gov.dhs.tsa.rh.serenity.bdd.pages.careers;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/law-enforcement")
public class RhLawEnforcementPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Experts in Law')]")
	private WebElementFacade lawEnforcementPageTitle;
	
	
    public void verifyLawEnforcementPageTitleIsPresent() {
    	lawEnforcementPageTitle.isCurrentlyVisible();
    	lawEnforcementPageTitle.click();
    }
    
}

