package gov.dhs.tsa.rh.serenity.bdd.pages.home;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/")
public class RhHomePage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Ensuring Safe')]")
	private WebElementFacade careersHomePageTitle;
	
	@FindBy(xpath="//img[@alt='Ensuring Safe Travels']")
	private WebElementFacade homeMainImage;

	@FindBy(xpath="//*[@id=\"block-theme-tsa-gov-main-menu\"]/ul/li[1]/a") 
    private WebElementFacade overviewDropdownItem;  
	
//	Search Fields - need to update ID's
	@FindBy(id = "edit-keyword")
	private WebElementFacade globalSearchField;
	
	@FindBy(id = "edit-keyword--2")
	private WebElementFacade jobTitleSearchField;
	
	@FindBy(id = "edit-location--2")
	private WebElementFacade jobLocationSearchField;
	
	@FindBy(id = "edit-keyword")
	private WebElementFacade upcomingEventsSearchField;
	
//	Search Buttons
	@FindBy(id = "edit-submit--2")
	private WebElementFacade submitJobsSeachButton;
	
	
    public void verifyHomeTitleIsPresent() {
    	careersHomePageTitle.isDisplayed();
//    	careersHomePageTitle.click();
    }

    //Search Methods
    
    public void globallySearch(String keywords) {
    	globalSearchField.typeAndEnter(keywords);
    }
    
    public void jobSearchTitleByLocation(String jobTitle, String jobLocation) {
    	jobTitleSearchField.type(jobTitle);
    	jobLocationSearchField.type(jobLocation);
    	submitJobsSeachButton.click();
    	
    }
    
    public void upcomingEventSearch(String keywords) {
    	globalSearchField.typeAndEnter(keywords);
    }
    
}

