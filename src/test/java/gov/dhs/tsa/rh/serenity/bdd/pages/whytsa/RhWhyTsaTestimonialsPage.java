package gov.dhs.tsa.rh.serenity.bdd.pages.whytsa;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/testimonials")
public class RhWhyTsaTestimonialsPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Meet the People of TSA')]")
	private WebElementFacade testimonialsPageTitle;
	
	
    public void verifyTestimonialsPageTitleIsPresent() {
    	testimonialsPageTitle.isDisplayed();
    	testimonialsPageTitle.click();
    }
    
}


