package gov.dhs.tsa.rh.serenity.bdd.pages.whytsa;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/individuals-with-disabilities")
public class RhWhyTsaIndividualWithDisabilitiesPage extends ElisBasePage {

    
	@FindBy(xpath="//*[@id='featured-top-block']/div[1]/div/h1")
	private WebElementFacade individualsWithDisabilitiesPageTitle;
	
	
    public void verifyIndividualsWithDisabilitiesPageTitleIsPresent() {
    	individualsWithDisabilitiesPageTitle.isDisplayed();
    	individualsWithDisabilitiesPageTitle.click();
    }
    
}


