package gov.dhs.tsa.rh.serenity.bdd.pages.whytsa;

import gov.dhs.tsa.rh.serenity.bdd.pages.base.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;


@DefaultUrl("/diversity-and-inclusion")
public class RhWhyTsaDiversityAndInclusionPage extends ElisBasePage {

    
	@FindBy(xpath="//*[contains(text(), 'Diversity &')]")
	private WebElementFacade diversityAndInclusionPageTitle;
	
	
    public void verifyDiversityAndInclusionPageTitleIsPresent() {
    	diversityAndInclusionPageTitle.isDisplayed();
    	diversityAndInclusionPageTitle.click();
    }
    
}


