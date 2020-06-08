package gov.dhs.tsa.rh.serenity.bdd.pages.search;

import org.openqa.selenium.support.ui.ExpectedConditions;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;


public class UsaJobsSearchResultsPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Executive Assistant')]")
	private WebElementFacade eventsPageTitle;
	
	@FindBy(xpath="//a[contains(@href, 'https://www.usajobs.gov:443/GetJob/ViewDetails/569795100')]") 
	private WebElementFacade executiveAssistantLink;
		
	
	private void openNewWindow(WebElementFacade link){
		final int currentWindowsOpen = getDriver().getWindowHandles().size();
		String currentWindowHandle = getDriver().getWindowHandle();

		clickOn(link);
		
		waitFor(ExpectedConditions.numberOfWindowsToBe(currentWindowsOpen+1));
		
		for(String s : getDriver().getWindowHandles()){
			if(!s.equals(currentWindowHandle)){
				//TODO: This all happens very quickly.  Better way to verify content?
				getDriver().switchTo().window(s);
				getDriver().close();
				getDriver().switchTo().window(currentWindowHandle);
				break;
			}
		}
	}
	
    public void clickExecutiveAssistantLink() {
//    	update job link as time progresses
    	executiveAssistantLink.isCurrentlyVisible();
    	openNewWindow(executiveAssistantLink);
    	
    }
    
}

