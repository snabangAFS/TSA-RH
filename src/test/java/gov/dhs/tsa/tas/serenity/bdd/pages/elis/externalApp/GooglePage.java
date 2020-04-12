package gov.dhs.tsa.tas.serenity.bdd.pages.elis.externalApp;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import gov.dhs.tsa.tas.serenity.bdd.pages.elis.ElisBasePage;
import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;

@DefaultUrl("http://www.google.com")
public class GooglePage extends ElisBasePage {

	@FindBy(name="q")
	WebElementFacade search;
    
	@FindBy(xpath="//a[contains(@href, 'http://thucydides.info/docs/serenity-staging/')]") 
    private WebElementFacade serenityLink;    		

    public void searchFor(String keywords) {
        search.typeAndEnter(keywords);
    }
    
    public void clickSerenityLink() {
    	serenityLink.isCurrentlyVisible();
    	serenityLink.click();
    }
}
