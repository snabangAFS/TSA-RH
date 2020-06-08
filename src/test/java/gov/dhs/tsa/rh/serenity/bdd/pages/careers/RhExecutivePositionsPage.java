package gov.dhs.tsa.rh.serenity.bdd.pages.careers;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/executive-positions")
public class RhExecutivePositionsPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Executive Leadership')]")
	private WebElementFacade careersOverviewPageTitle;
	
	
    public void verifyExecutiveLeadershipPageTitleIsPresent() {
    	careersOverviewPageTitle.isDisplayed();
//    	careersOverviewPageTitle.click();
    }
    
}