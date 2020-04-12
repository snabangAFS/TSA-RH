package gov.dhs.tsa.tas.serenity.framework;

import gherkin.formatter.model.Tag;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import cucumber.runtime.Runtime;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import cucumber.runtime.model.CucumberTagStatement;

/*
 * Based off of @cucumber.runtime.junit.FeatureRunner
 * 
 * The only functional difference between the two classes is that this class returns
 * rerunnable children rather than the default non-rerunnable ones.
 * 
 * Please see {@link #@getMaxRerunCount() getMaxRerunCount()} 
 * for the exact system property to overwrite to enable rerunnable scenarios.
 * 
 */
public class RerunnableFeatureRunner extends FeatureRunner {
	@SuppressWarnings("rawtypes")
	private final List<ParentRunner> children = new ArrayList<ParentRunner>();

	private final CucumberFeature cucumberFeature;
	private final Runtime runtime;
	private final JUnitReporter jUnitReporter;

	public RerunnableFeatureRunner(CucumberFeature cucumberFeature, Runtime runtime, JUnitReporter jUnitReporter)
			throws InitializationError {
		super(cucumberFeature, runtime, jUnitReporter);
		this.cucumberFeature = cucumberFeature;
		this.runtime = runtime;
		this.jUnitReporter = jUnitReporter;
		buildFeatureElementRunners();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected List<ParentRunner> getChildren() {
		return children;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void runChild(ParentRunner child, RunNotifier notifier) {
		child.run(notifier);
	}

	/*-
	 * Basically, it is not possible to substitute your own RunNotifier because
	 * it acts as a bridge between the test VM created by gradle and the main
	 * VM.
	 * 
	 * The following is a stack of .method invocations that show why this is the case:
	 * 
	 * org.junit.runner.notification.RunNotifier
	 * 	- .listeners = org.junit.runner.notification.SynchronizedRunListener
	 *  - .listener = org.gradle.api.internal.tasks.testing.junit.JUnitTestEventAdapter
	 *  - .resultProcessor = $Proxy(org.gradle.messaging.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler)
	 *  - .dispatch = org.gradle.messaging.actor.internal.DefaultActorFactory$BlockingActor
	 *  - .dispatch = org.gradle.messaging.dispatch.ReflectionDispatch
	 *  - .target = org.gradle.api.internal.tasks.testing.junit.TestClassExecutionEventGenerator
	 *  	- .currentTestClass = org.gradle.api.internal.tasks.testing.DefaultTestClassDescriptor (gov.dhs.uscis.elis2.serenity.CucumberTestSuite0)
	 *  - .resultProcessor = org.gradle.api.internal.tasks.testing.results.AttachParentTestResultProcessor
	 *  - .processor = org.gradle.api.internal.tasks.testing.processors.CaptureTestOutputTestResultProcessor
	 *  	- .outputRedirector = org.gradle.api.internal.tasks.testing.junit.JULRedirector
	 *  - .processor = org.gradle.api.internal.tasks.testing.results.AttachParentTestResultProcessor
	 *  - .processor = $Proxy(org.gradle.messaging.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler)
	 *  - .dispatch = org.gradle.messaging.remote.internal.hub.MessageHub$ChannelDispatch
	 *  	- .channelIdentifier = org.gradle.messaging.remote.internal.hub.protocol.ChannelIdentifier
	 *  		- .name = "org.gradle.api.internal.tasks.testing.TestResultProcessor"
	 *  	- .type = class org.gradle.messaging.dispatch.MethodInvocation
	 *  - .enclosingClass() = org.gradle.messaging.remote.internal.hub.MessageHub
	 *  	- .connections = org.gradle.messaging.remote.internal.hub.ConnectionSet
	 *  		- .map = HashMap<K,V>
	 *  			- .firstKey() = org.gradle.messaging.remote.internal.hub.ConnectionState
	 *  			- .firstValue() = Object (lock?)
	 *  	- .displayName = "/127.0.0.1:64780 to /127.0.0.1:64778"
	 * 
	 */
	@Override
	public void run(RunNotifier notifier) {
		super.run(notifier);
	}

	/*
	 * Returns the maximum number of times a scenario should be rerun.
	 * 
	 * Currently entirely based on the test.maxRerunCount system property.
	 */
	protected int getMaxRerunCount() {
		String maxRerunCount = System.getProperty("test.maxRerunCount");
		if (maxRerunCount != null) {
			try {
				return Integer.parseInt(maxRerunCount);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 1;
	}

	private void buildFeatureElementRunners() throws InitializationError {
		List<Tag> additionalTags = cucumberFeature.getGherkinFeature().getTags();

		for (CucumberTagStatement cucumberTagStatement : cucumberFeature.getFeatureElements()) {
			if (cucumberTagStatement instanceof CucumberScenario) {
				RerunnableScenarioRunner rerunnableScenario = new RerunnableScenarioRunner(runtime,
						(CucumberScenario) cucumberTagStatement, jUnitReporter, getMaxRerunCount(), additionalTags);
				children.add(rerunnableScenario);
			} else {
				ParentRunner<?> featureElementRunner = new RerunnableScenarioOutlineRunner(runtime,
						(CucumberScenarioOutline) cucumberTagStatement, jUnitReporter, getMaxRerunCount());
				children.add(featureElementRunner);
			}
		}
	}
}
