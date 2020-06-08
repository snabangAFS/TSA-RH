#Author: Soraj Nabangchang
Feature: Upcoming Events search the TSA Careers Site Test (UI) 

Scenario: As a user I want to search upcoming events on the site 
		
		Given the user navigates to the Careers page (UI)
		And the user navigates to the "Upcoming Events" from the Events dropdown (UI)
		When the users searches events in "Arlington, VA" (UI)
		#And the users searches events on "date" (UI)
		#And the users searches "airport" airport (UI)
		And the users selects "Job Fair" type of event (UI)
		Then verifies the "Arlington, VA" and "Job Fair" events details (UI)