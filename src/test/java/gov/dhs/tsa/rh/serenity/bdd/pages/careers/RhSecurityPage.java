package gov.dhs.tsa.rh.serenity.bdd.pages.careers;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/security")
public class RhSecurityPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Working on the')]")
	private WebElementFacade securityPageTitle;
	
	
    public void verifySecurityPageTitleIsPresent() {
    	securityPageTitle.isCurrentlyVisible();
    	securityPageTitle.click();
    }
    
}
