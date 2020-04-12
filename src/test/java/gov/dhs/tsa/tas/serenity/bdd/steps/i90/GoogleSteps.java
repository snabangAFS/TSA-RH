package gov.dhs.tsa.tas.serenity.bdd.steps.i90;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gov.dhs.tsa.tas.serenity.bdd.pages.elis.externalApp.GooglePage;

public class GoogleSteps {
	GooglePage googlehomepage;

	@Given("(?i)^the user goes to Google page$")
	public void theUserGoesToGooglePage() {
		//Serenity.setSessionVariable("driver").to(googlePage.getDriver());
		googlehomepage.open();
	}
	
	@When("(?i)^the user searches \"([^\"]*)\"$")
	public void theUserSearchesX(String keywords) {
		googlehomepage.searchFor(keywords);
	}
	
	@Then("(?i)^the user clicks the link$")
	public void theUserClicksTheLink() {
		googlehomepage.clickSerenityLink();
		
	}
}
	