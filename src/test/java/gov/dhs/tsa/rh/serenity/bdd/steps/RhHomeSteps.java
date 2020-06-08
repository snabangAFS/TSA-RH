package gov.dhs.tsa.rh.serenity.bdd.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import gov.dhs.tsa.rh.serenity.bdd.pages.careers.RhExecutivePositionsPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.careers.RhFederalHiringProcessPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.careers.RhLawEnforcementPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.careers.RhMissionSupportPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.careers.RhOverviewPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.careers.RhSecurityPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.events.RhCalendarPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.events.RhUpcomingEventsPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.home.RhHomePage;
import gov.dhs.tsa.rh.serenity.bdd.pages.whytsa.RhWhyTsaBenefitsPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.whytsa.RhWhyTsaDiversityAndInclusionPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.whytsa.RhWhyTsaIndividualWithDisabilitiesPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.whytsa.RhWhyTsaOveriewPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.whytsa.RhWhyTsaStudentsPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.whytsa.RhWhyTsaTestimonialsPage;
import gov.dhs.tsa.rh.serenity.bdd.pages.whytsa.RhWhyTsaVeteransPage;

public class RhHomeSteps {

	RhHomePage rhHomePage;
	
	RhOverviewPage rhOverviewPage;
	RhExecutivePositionsPage rhExecutivePositionsPage;
	RhMissionSupportPage rhMissionSupportPage;
	RhSecurityPage rhSecurityPage;
	RhLawEnforcementPage rhLawEnforcementPage;
	RhFederalHiringProcessPage rhFederalHiringProcessPage;
	
	RhCalendarPage rhCalendarPage;
	RhUpcomingEventsPage rhUpcomingEventsPage;
	
	RhWhyTsaOveriewPage rhWhyTsaOveriewPage;
	RhWhyTsaTestimonialsPage rhWhyTsaTestimonialsPage;
	RhWhyTsaIndividualWithDisabilitiesPage rhWhyTsaIndividualWithDisabilitiesPage;
	RhWhyTsaBenefitsPage rhWhyTsaBenefitsPage;
	RhWhyTsaStudentsPage rhWhyTsaStudentsPage;
	RhWhyTsaDiversityAndInclusionPage rhWhyTsaDiversityAndInclusionPage;
	RhWhyTsaVeteransPage rhWhyTsaVeteransPage;
	
	

	@Given("(?i)^the user navigates to the Careers page \\(UI\\)$")
	public void theUserNavigatesToTheCareersPage() {
		rhHomePage.open();
		rhHomePage.verifyHomeTitleIsPresent();
	}

	@Given("^the user navigates to the \"([^\"]*)\" from the Careers dropdown \\(UI\\)$")
	public void theUserNavigatesToXAtCareersDropdown(String page) {

		switch (page) {
		case "Overview":
			rhOverviewPage.open();
			rhOverviewPage.verifyCareersOverviewPageTitleIsPresent();
			break;

		case "Executive Positions":
			rhExecutivePositionsPage.open();
			rhExecutivePositionsPage.verifyExecutiveLeadershipPageTitleIsPresent();
			break;

		case "Mission Support":
			rhMissionSupportPage.open();
			rhMissionSupportPage.verifyMissionSupportPageTitleIsPresent();
			break;

		case "Security":
			rhSecurityPage.open();
			rhSecurityPage.verifySecurityPageTitleIsPresent();
			break;

		case "Law Enforcement":
			rhLawEnforcementPage.open();
			rhLawEnforcementPage.verifyLawEnforcementPageTitleIsPresent();
			break;

		case "Federal Hiring Process":
			rhFederalHiringProcessPage.open();
			rhFederalHiringProcessPage.verifyFederalHiringProcessPageTitleIsPresent();
			break;

		}

	}

	@Given("^the user navigates to the \"([^\"]*)\" from the Events dropdown \\(UI\\)$")
	public void theUserNavigatesToXAtEventsDropdown(String page) {

		switch (page) {
		case "Calendar":
			rhCalendarPage.open();
			rhCalendarPage.verifyEventsPageTitleIsPresent();
			break;

		case "Upcoming Events":
			rhUpcomingEventsPage.open();
			rhUpcomingEventsPage.verifyUpcomingEventsPageTitleIsPresent();
			break;
		}
	}

	@Given("^the user navigates to the \"([^\"]*)\" from the Why TSA dropdown \\(UI\\)$")
	public void theUserNavigatesToXAtWhyTsaDropdown(String page) {

		switch (page) {
		case "Overview":
			rhWhyTsaOveriewPage.open();
			rhWhyTsaOveriewPage.verifyWhyTsaOverviewPageTitleIsPresent();
			break;

		case "Testimonials":
			rhWhyTsaTestimonialsPage.open();
			rhWhyTsaTestimonialsPage.verifyTestimonialsPageTitleIsPresent();
			break;

		case "Individuals With Disabilities":
			rhWhyTsaIndividualWithDisabilitiesPage.open();
			rhWhyTsaIndividualWithDisabilitiesPage.verifyIndividualsWithDisabilitiesPageTitleIsPresent();			
			break;

		case "Benefits":
			rhWhyTsaBenefitsPage.open();
			rhWhyTsaBenefitsPage.verifyBenefitsPageTitleIsPresent();
			break;

		case "Students":
			rhWhyTsaStudentsPage.open();
			rhWhyTsaStudentsPage.verifyStudentsPageTitleIsPresent();
			break;

		case "Diversity And Inclusion":
			rhWhyTsaDiversityAndInclusionPage.open();
			rhWhyTsaDiversityAndInclusionPage.verifyDiversityAndInclusionPageTitleIsPresent();
			break;
			
		case "Veterans":
			rhWhyTsaVeteransPage.open();
			rhWhyTsaVeteransPage.verifyVeteransPageTitleIsPresent();
			break;
		}

	}
	
	@When("(?i)^the user globally searches \"([^\"]*)\" \\(UI\\)$")
	public void theUserGloballySearches(String keywords) {
		rhHomePage.globallySearch(keywords);
		
	}
	
	@When("(?i)^the user searches jobs by \"([^\"]*)\" in \"([^\"]*)\" \\(UI\\)$")
	public void theUserSearchesJobsbyTitleAndLocation(String jobTitle, String jobLocation) {
		rhHomePage.jobSearchTitleByLocation(jobTitle, jobLocation);
	}
	
	@When("(?i)^the user searches events by \"([^\"]*)\" \\(UI\\)$")
	public void theUserSearchesEvents(String events) {
		rhHomePage.upcomingEventSearch(events);
	}
}
