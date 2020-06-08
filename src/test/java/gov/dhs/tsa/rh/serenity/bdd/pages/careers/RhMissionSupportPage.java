package gov.dhs.tsa.rh.serenity.bdd.pages.careers;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/mission-support")
public class RhMissionSupportPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Building a Career in ')]")
	private WebElementFacade missionSupportPageTitle;
	
	
    public void verifyMissionSupportPageTitleIsPresent() {
    	missionSupportPageTitle.isDisplayed();
//    	missionSupportPageTitle.click();
    }
    
}

