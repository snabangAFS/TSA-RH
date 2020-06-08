package gov.dhs.tsa.rh.serenity.bdd.pages.events;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/events")
public class RhCalendarPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Find TSA Events Near You')]")
	private WebElementFacade eventsPageTitle;
	
	
    public void verifyEventsPageTitleIsPresent() {
    	eventsPageTitle.isDisplayed();
//    	eventsPageTitle.click();
    }
    
}

