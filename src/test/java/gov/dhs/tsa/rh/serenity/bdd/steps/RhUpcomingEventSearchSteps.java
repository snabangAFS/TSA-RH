package gov.dhs.tsa.rh.serenity.bdd.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import gov.dhs.tsa.rh.serenity.bdd.pages.events.RhUpcomingEventsPage;

public class RhUpcomingEventSearchSteps {

	RhUpcomingEventsPage rhUpcomingEventsPage;

	@When("(?i)^the users searches events in \"([^\"]*)\" \\(UI\\)$")
	public void theUserSearchesUpcomingEventsLocation(String location) {
		rhUpcomingEventsPage.searchEventLocation(location);
	}

	@When("(?i)^the users searches events on \"([^\"]*)\" \\(UI\\)$")
	public void theUserSearchesUpcomingEventsDate(String date) {
		rhUpcomingEventsPage.searchEventDate(date);
	}

	@When("(?i)^the users searches \"([^\"]*)\" airport \\(UI\\)$")
	public void theUserSearchesUpcomingEventsAirport(String airport) {
		rhUpcomingEventsPage.searchEventAirport(airport);
	}

	@Given("^the users selects \"([^\"]*)\" type of event \\(UI\\)$")
	public void theUserNavigatesToXAtCareersDropdown(String eventType) {

		switch (eventType) {
		case "Application Assistance Event":
			rhUpcomingEventsPage.selectEventTypeDropdown("2");
			rhUpcomingEventsPage.clickApplyButton();
			break;

		case "Conference":
			rhUpcomingEventsPage.selectEventTypeDropdown("3");
			rhUpcomingEventsPage.clickApplyButton();
			break;

		case "Exhibitor - Conference":
			rhUpcomingEventsPage.selectEventTypeDropdown("2");
			rhUpcomingEventsPage.clickApplyButton();
			break;

		case "Express Hiring Event":
			rhUpcomingEventsPage.selectEventTypeDropdown("2");
			rhUpcomingEventsPage.clickApplyButton();
			break;

		case "Job Fair":
			rhUpcomingEventsPage.selectEventTypeDropdown("1");
			rhUpcomingEventsPage.clickApplyButton();
			break;

		case "Multi-step Hiring Event":
			rhUpcomingEventsPage.selectEventTypeDropdown("68");
			rhUpcomingEventsPage.clickApplyButton();
			break;

		case "Other":
			rhUpcomingEventsPage.selectEventTypeDropdown("69");
			rhUpcomingEventsPage.clickApplyButton();
			break;
		}

	}

	@When("(?i)^verifies the \"([^\"]*)\" and \"([^\"]*)\" events details \\(UI\\)$")
	public void verifiesResultsDisplayed(String location, String type) {
		rhUpcomingEventsPage.clickAndVerifyArlingtonLink();
	}

}
