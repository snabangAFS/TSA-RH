package gov.dhs.tsa.rh.serenity.bdd.pages.careers;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/federal-hiring-process")
public class RhFederalHiringProcessPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Understanding the')]")
	private WebElementFacade federalHiringProcessPageTitle;
	
	
    public void verifyFederalHiringProcessPageTitleIsPresent() {
    	federalHiringProcessPageTitle.isCurrentlyVisible();
    	federalHiringProcessPageTitle.click();
    }
    
}

