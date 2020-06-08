package gov.dhs.tsa.rh.serenity.bdd.pages.whytsa;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/students")
public class RhWhyTsaStudentsPage extends ElisBasePage {

    
	@FindBy(xpath="//*[@id='featured-top-block']/div[1]/div/h1")
	private WebElementFacade studentsPageTitle;
	
	
    public void verifyStudentsPageTitleIsPresent() {
    	studentsPageTitle.isDisplayed();
    	studentsPageTitle.click();
    }
    
}


