package gov.dhs.tsa.rh.serenity.bdd.pages.events;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("/job-events")
public class RhUpcomingEventsPage extends ElisBasePage {

	@FindBy(xpath="//*[@id='block-theme-tsa-gov-content']/div/div/div/div[1]/h2")
	private WebElementFacade upcomingEventsPageTitile;
	
	//*[@id="featured-top-block"]/div[1]/div/h1
	
	@FindBy(id = "edit-keyword")
	private WebElementFacade locationSearchField;

	@FindBy(id = "edit-date")
	private WebElementFacade dateSearchField;
	
	@FindBy(id = "edit-airport")
	private WebElementFacade airportSearchField;
	
	@FindBy(id = "edit-event-type")
	private WebElementFacade eventTypeSearchDropdown;
	
	@FindBy(id = "edit-submit-events")
	private WebElementFacade applyButton;
	
//	Links on Upcoming Events
	
	@FindBy(xpath="//a[contains(@href, '/event/2020/07/arlington-diversity-employment-day-career-fair')]") 
	private WebElementFacade arlingtonDiversityCareerFairLink;
	
	@FindBy(xpath="//*[@id='featured-top-block']/div/div[1]/div/h1/span")
	private WebElementFacade arlingtonDivEmpDayTitile;
	
	
	
	
	public void verifyUpcomingEventsPageTitleIsPresent() {
		upcomingEventsPageTitile.isDisplayed();
//		upcomingEventsPageTitile.click();
	}
	
    public void searchEventLocation(String location) {
    	locationSearchField.type(location);
    }
    
    public void searchEventDate(String keywords) {
    	dateSearchField.type(keywords);
    }
    
    public void searchEventAirport(String airport) {
    	airportSearchField.type(airport);
    }
    
    public void selectEventTypeDropdown(String number) {
    	eventTypeSearchDropdown.selectByValue(number);
    }
    
    public void clickApplyButton() {
    	applyButton.click();
    }
    
    public void clickAndVerifyArlingtonLink() {
    	arlingtonDiversityCareerFairLink.click();
    	arlingtonDivEmpDayTitile.isDisplayed();
    	
    }
    
	

}
