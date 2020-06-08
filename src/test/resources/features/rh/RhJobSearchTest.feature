#Author: Soraj Nabangchang
Feature: Jobs search the TSA Careers Site Test (UI) 

Scenario: As a user I want to search jobs on the site 
		
		Given the user navigates to the Careers page (UI)
		And the user searches jobs by "Software" in "Arlington, VA" (UI)
		Then the user opens the job link (UI)
