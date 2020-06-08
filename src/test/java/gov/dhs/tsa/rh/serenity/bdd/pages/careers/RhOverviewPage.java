package gov.dhs.tsa.rh.serenity.bdd.pages.careers;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/careers-at-tsa")
public class RhOverviewPage extends ElisBasePage {
	
	@FindBy(xpath="//*[contains(text(), 'One agency. ')]")
	private WebElementFacade careersOverviewPageTitle;
	
	
    public void verifyCareersOverviewPageTitleIsPresent() {
    	careersOverviewPageTitle.isDisplayed();
//    	careersOverviewPageTitle.click();
    }
    
}

