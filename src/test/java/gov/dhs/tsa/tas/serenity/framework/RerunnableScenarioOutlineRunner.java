package gov.dhs.tsa.tas.serenity.framework;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import cucumber.runtime.Runtime;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;

/*
 * Based off of @cucumber.runtime.model.ScenarioOutlineRunner
 * 
 * Examples are rendered very poorly in the reports when retries come into play.
 * To remedy this issue, this class decomposes scenario outlines into individual
 * scenarios and runs those instead.  This bypasses example handling in Serenity
 * entirely.
 * 
 * Returns @RerunnableScenarioRunner instead of @ExamplesRunner children.
 */

public class RerunnableScenarioOutlineRunner extends Suite {
	private final CucumberScenarioOutline cucumberScenarioOutline;
	private Description description;

	public RerunnableScenarioOutlineRunner(Runtime runtime, CucumberScenarioOutline cucumberScenarioOutline,
			JUnitReporter jUnitReporter, int maxRetries) throws InitializationError {
		super(RerunnableScenarioRunner.class,
				buildRunners(runtime, cucumberScenarioOutline, jUnitReporter, maxRetries));
		this.cucumberScenarioOutline = cucumberScenarioOutline;
	}

	private static List<Runner> buildRunners(Runtime runtime, CucumberScenarioOutline cucumberScenarioOutline,
			JUnitReporter jUnitReporter, int maxRetries) throws InitializationError {
		List<Runner> runners = new ArrayList<Runner>();
		for (CucumberExamples cucumberExamples : cucumberScenarioOutline.getCucumberExamplesList()) {
			List<CucumberScenario> exampleScenarios = cucumberExamples.createExampleScenarios();
			for (CucumberScenario scenario : exampleScenarios) {
				try {
					RerunnableScenarioRunner exampleScenarioRunner = new RerunnableScenarioRunner(runtime, scenario,
							jUnitReporter, maxRetries);
					runners.add(exampleScenarioRunner);
				} catch (InitializationError initializationError) {
					initializationError.printStackTrace();
				}
			}
		}
		return runners;
	}

	@Override
	public String getName() {
		return cucumberScenarioOutline.getVisualName();
	}

	@Override
	public Description getDescription() {
		if (description == null) {
			description = Description.createSuiteDescription(getName(), cucumberScenarioOutline.getGherkinModel());
			for (Runner child : getChildren()) {
				description.addChild(describeChild(child));
			}
		}
		return description;
	}

	@Override
	public void run(final RunNotifier notifier) {
		super.run(notifier);
	}
}
