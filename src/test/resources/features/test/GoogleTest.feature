
Feature: testing the login functinalaties
	Scenario: Test user can search on Google
		Given the user goes to Google page
		When the user searches "Serenity BDD Manual"
		Then the user clicks the link