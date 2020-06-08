package gov.dhs.tsa.rh.serenity.bdd.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import gov.dhs.tsa.rh.serenity.bdd.pages.search.UsaJobsSearchResultsPage;

public class RhJobSearchSteps {

	UsaJobsSearchResultsPage usaJobsSearchResultsPage;


	@When("(?i)^the user opens the job link \\(UI\\)$")
	public void verifiesResultsDisplayed() {
		 usaJobsSearchResultsPage.clickExecutiveAssistantLink();
	}

}
