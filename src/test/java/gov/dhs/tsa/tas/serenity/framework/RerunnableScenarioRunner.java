package gov.dhs.tsa.tas.serenity.framework;

import gherkin.I18n;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Step;
import gherkin.formatter.model.Tag;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import cucumber.runtime.CucumberException;
import cucumber.runtime.Runtime;
import cucumber.runtime.junit.ExecutionUnitRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberBackground;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberTagStatement;
import cucumber.runtime.model.StepContainer;

/*
 * This class allows cucumber scenarios to be rerun if they fail.
 * 
 * When a cucumber scenario fails, this class ensures that a
 * 'testAssumptionFailure' event is fired, instead of the normal 'testFailure' event.
 * This marks the test as pending, rather than failing, preventing the test suite from failing.
 *  
 * When a scenario passes, it is not run again, which helps speed up execution.
 * 
 * This class generates new scenarios from the scenario passed into its constructor
 * via reflection.  Each new scenario is given a new name and id based on its execution count,
 * which makes the order of when each scenario was retried clear.
 * 
 * Whether or not a scenario passes depends on whether it's annotated with @mature, @experimental, or neither
 * 
 */
public class RerunnableScenarioRunner extends ParentRunner<ExecutionUnitRunner> {
	private int maxReruns;
	private Runtime runtime;
	private CucumberScenario cucumberScenario;
	private JUnitReporter jUnitReporter;
	private List<ExecutionUnitRunner> children;
	private int currentExecutionCount = 0;
	private boolean finishedEarly = false;
	private Failure lastEncounteredFailure = null;
	private List<Tag> additionalTags;

	protected RerunnableScenarioRunner(Runtime runtime, CucumberScenario cucumberScenario, JUnitReporter jUnitReporter,
			int maxReruns) throws InitializationError {
		this(runtime, cucumberScenario, jUnitReporter, maxReruns, new ArrayList<Tag>());
	}

	protected RerunnableScenarioRunner(Runtime runtime, CucumberScenario cucumberScenario, JUnitReporter jUnitReporter,
			int maxReruns, List<Tag> additionalTags) throws InitializationError {
		super(RerunnableScenarioRunner.class);
		this.maxReruns = maxReruns;
		this.runtime = runtime;
		this.additionalTags = additionalTags;
		this.cucumberScenario = cucumberScenario;
		this.jUnitReporter = jUnitReporter;
		this.children = loadChildren();
		ensureTagConsistency();
	}

	/*
	 * Ensures that both @mature and @experimental are not set for a cucumber
	 * scenario.
	 */
	protected void ensureTagConsistency() throws InitializationError {
		if (isMature() && isExperimental()) {
			throw new InitializationError(new CucumberException(
					"Cannot have both the @mature AND @experimental tags set in the same scenario."));
		}
	}

	protected Collection<Tag> getAdditionalTags() {
		return this.additionalTags;
	}

	/*
	 * Returns the cucumber scenario that is being retried.
	 */
	protected CucumberScenario getCucumberScenario() {
		return this.cucumberScenario;
	}

	/*
	 * If the cucumber scenario being run is marked with mature, then the
	 * scenario will always fail if it is ever rerun due to a failure.
	 */
	protected boolean isMature() {
		return hasTag(getCucumberScenario(), getAdditionalTags(), "@mature");
	}

	/*
	 * If the cucumber scenario being run is marked with experimental, then the
	 * scenario will always pass.
	 */
	protected boolean isExperimental() {
		return hasTag(getCucumberScenario(), getAdditionalTags(), "@experimental");
	}

	/*
	 * Returns whether or not the provided cucumberScenario is annotated with a
	 * specific tag.
	 */
	private static boolean hasTag(CucumberScenario cucumberScenario, Collection<Tag> additionalTags, String target) {
		Set<Tag> allTags = new HashSet<Tag>();
		allTags.addAll(cucumberScenario.getGherkinModel().getTags());
		allTags.addAll(additionalTags);
		for (Tag tag : allTags) {
			if (tag.getName().equalsIgnoreCase(target)) {
				return true;
			}
		}
		return false;
	}

	private static CucumberFeature getCucumberFeatureFromScenario(CucumberScenario cucumberScenario)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return getValue(StepContainer.class, cucumberScenario, "cucumberFeature");
	}

	/*
	 * Creates a shallow copy of a step.
	 */
	private static Step duplicateStep(Step original) {
		return new Step(original.getComments(), original.getKeyword(), original.getName(), original.getLine(),
				original.getRows(), original.getDocString());
	}

	private String generateExecutionPrefix() {
		return "";
	}

	/*
	 * Creates a similar scenario to the original that acts as one retry. The
	 * scenario is annotated with a suffix that specifies the retry number.
	 */
	private CucumberScenario createDuplicateScenario(CucumberScenario original, int index)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {

		CucumberFeature originalCucumberFeature = getCucumberFeatureFromScenario(original);
		CucumberBackground cucumberBackground = getValue(CucumberFeature.class, originalCucumberFeature,
				"cucumberBackground");
		Background background = null;
		if (cucumberBackground != null) {
			background = getValue(StepContainer.class, cucumberBackground, "statement");
		}
		I18n i18n = originalCucumberFeature.getI18n();
		String scenarioString = i18n.keywords("scenario").get(0);
		Scenario originalScenario = getValue(CucumberScenario.class, original, "scenario");

		LinkedList<String> allSegments = new LinkedList<String>();
		allSegments.addAll(Arrays.asList(originalScenario.getId().split(";")));

		List<String> outputSegments = new ArrayList<String>();

		String idSuffix = "-" + (index + 1);
		String nameSuffix = " - " + (index + 1);
		String namePrefix = generateExecutionPrefix();
		if (allSegments.size() == 4)// scenario outline
		{
			try {
				int x = Integer.parseInt(allSegments.getLast());
				idSuffix += "-" + x;
				nameSuffix = " " + original.getVisualName() + nameSuffix;
			} catch (Exception e) {
			}
		}

		if (allSegments.size() >= 2) {
			outputSegments.add(allSegments.poll());// feature-name
			outputSegments.add(allSegments.poll() + idSuffix);// scenario-name
		}

		/*-
		 * feature;scenario-1   (Scenario)
		 * feature;scenario-1-1 (Scenario Outline)
		 */
		String newId = StringUtils.join(outputSegments, ";");

		/*- 
		 * (MAT) Scenario - 1           (Scenario)
		 * (EXP) Scenario | a | b | - 1 (Scenario Outline)
		 */
		String newName = namePrefix + originalScenario.getName() + nameSuffix;

		Scenario duplicateScenario = new Scenario(originalScenario.getComments(), originalScenario.getTags(),
				scenarioString, newName, originalScenario.getDescription(), originalScenario.getLine(), newId);

		List<Step> steps = original.getSteps();
		Feature originalFeature = originalCucumberFeature.getGherkinFeature();
		Feature duplicateFeature = new Feature(originalFeature.getComments(), originalFeature.getTags(),
				originalFeature.getKeyword(), originalFeature.getName(), originalFeature.getDescription(),
				originalFeature.getLine(), originalFeature.getId());
		String duplicatePath = originalCucumberFeature.getPath();

		CucumberFeature copyCucumberFeature = new CucumberFeature(duplicateFeature, duplicatePath);
		if (background != null) {
			copyCucumberFeature.background(background);
			for (Step step : cucumberBackground.getSteps()) {
				Step duplicateStep = duplicateStep(step);

				copyCucumberFeature.step(duplicateStep);
			}
		}
		copyCucumberFeature.setI18n(i18n);
		copyCucumberFeature.scenario(duplicateScenario);
		for (Step step : steps) {
			Step duplicateStep = duplicateStep(step);
			copyCucumberFeature.step(duplicateStep);
		}

		CucumberScenario copy = null;
		for (CucumberTagStatement statement : copyCucumberFeature.getFeatureElements()) {
			if (statement instanceof CucumberScenario) {
				copy = (CucumberScenario) statement;
				break;
			}
		}

		System.out.println("RerunnableScenarioRunner: " + originalScenario.getId() + " -> " + newId);
		return copy;
	}

	/*
	 * Sets the value for the specified field regardless of visiblity or if the
	 * field is marked final
	 */
	@SuppressWarnings("unused")
	private static void setValueIgnoringFinal(final Class<?> clazz, final Object original, final String variableName,
			final Object newValue)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = clazz.getDeclaredField(variableName);
		field.setAccessible(true);
		// remove final
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(original, newValue);
		field.setAccessible(false);
	}

	/*
	 * Returns the value for the specified field regardless of visibility
	 */
	private static <T> T getValue(Class<?> clazz, Object original, String variableName)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		Field field = clazz.getDeclaredField(variableName);
		return getValueFromField(field, original);
	}

	/*
	 * Returns the value of a field regardless of its visibility
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getValueFromField(Field field, Object original)
			throws IllegalArgumentException, IllegalAccessException {
		boolean originallyAccessible = field.isAccessible();
		if (!originallyAccessible) {
			field.setAccessible(true);
		}
		try {
			Object value = field.get(original);
			return (T) value;
		} finally {
			if (!originallyAccessible) {
				field.setAccessible(false);
			}
		}
	}

	/*
	 * Turns the provided cucumberScenario into a list of similar
	 * cucumberScenarios that act as retries in the test suite.
	 */
	private List<ExecutionUnitRunner> loadChildren() throws InitializationError {
		List<ExecutionUnitRunner> runList = new ArrayList<ExecutionUnitRunner>();
		for (int x = 0; x < maxReruns; x++) {
			try {
				CucumberScenario duplicateScenario = createDuplicateScenario(cucumberScenario, x);

				ExecutionUnitRunner unitRunner = new ExecutionUnitRunner(runtime, duplicateScenario, jUnitReporter);
				runList.add(unitRunner);
			} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
				throw new InitializationError(e);
			}

		}
		return runList;
	}

	/*
	 * Returns all similar scenarios that were built from the provided original
	 * scenario. Each similar scenario represents a retry.
	 */
	@Override
	protected List<ExecutionUnitRunner> getChildren() {
		return children;
	}

	/*
	 * Returns the child's own description of itself.
	 */
	@Override
	protected Description describeChild(ExecutionUnitRunner child) {
		return child.getDescription();
	}

	/*
	 * Reruns the originally provided scenario some amount of times.
	 * 
	 * Has different behavior depending on whether the mature or experimental
	 * tags are present, but will always short-circuit execution when a scenario
	 * passes.
	 */
	@Override
	protected void runChild(ExecutionUnitRunner child, final RunNotifier notifier) {
		if (hasFinishedEarly()) {
			increaseExecutionCount();
			return;
		}
		if (isMature()) {
			System.out.println("RerunnableScenarioRunner: Running mature child.");
			runMatureChild(child, notifier);
		} else if (isExperimental()) {
			System.out.println("RerunnableScenarioRunner: Running experimental child.");
			runExperimentalChild(child, notifier);
		} else {
			System.out.println("RerunnableScenarioRunner: Running default child.");
			runDefaultChild(child, notifier);
		}
		increaseExecutionCount();
	}

	/*
	 * Pass the test if the first retry passes. Otherwise keep retrying until
	 * the test passes, but fail the test regardless.
	 */
	protected void runMatureChild(ExecutionUnitRunner child, RunNotifier notifier) {
		if (this.isFinalExecution()) {
			runNormally(child, notifier);
		} else {
			runIgnoringErrors(child, notifier);
		}

		if ((isFinalExecution() || hasFinishedEarly()) && hasFailedBefore()) {
			Description description = Description.createTestDescription(RerunnableScenarioRunner.class,
					"Enforcing that a @mature scenario cannot pass after it failed once!");
			notifier.fireTestStarted(description);
			notifier.fireTestFailure(new Failure(description, this.getLastEncounteredFailure().getException()));
			notifier.fireTestFinished(description);
		}
	}

	/*
	 * Always pass the test.
	 */
	protected void runExperimentalChild(ExecutionUnitRunner child, RunNotifier notifier) {
		runIgnoringErrors(child, notifier);
	}

	/*
	 * If it's the last retry, run the test normally. Otherwise, ignore test
	 * failures.
	 */
	protected void runDefaultChild(ExecutionUnitRunner child, RunNotifier notifier) {
		if (this.isFinalExecution()) {
			runNormally(child, notifier);
		} else {
			runIgnoringErrors(child, notifier);
		}
	}

	/*
	 * Short-circuit the max number of retries.
	 */
	protected void setFinishedEarly(boolean finishedEarly) {
		this.finishedEarly = finishedEarly;
	}

	/*
	 * Returns whether the test should finish early before using up every retry.
	 */
	protected boolean hasFinishedEarly() {
		return this.finishedEarly;
	}

	/*
	 * Return the maximum number of retries the test can perform.
	 */
	protected int getMaxRetries() {
		return this.maxReruns;
	}

	/*
	 * Returns the number of times the test has already ran.
	 */
	protected int getExecutionCount() {
		return this.currentExecutionCount;
	}

	/*
	 * Increase the number of the times the test ran.
	 */
	protected void increaseExecutionCount() {
		this.currentExecutionCount++;
	}

	/*
	 * Returns whether or not this is the final retry.
	 */
	protected boolean isFinalExecution() {
		return getExecutionCount() >= (getMaxRetries() - 1);
	}

	/*
	 * Returns whether or not a retry has failed before.
	 */
	protected boolean hasFailedBefore() {
		return this.lastEncounteredFailure != null;
	}

	/*
	 * Sets the last encountered retry failure.
	 */
	protected void setLastEncounteredFailure(Failure failure) {
		this.lastEncounteredFailure = failure;
	}

	/*
	 * Gets the last encountered retry failure.
	 */
	protected Failure getLastEncounteredFailure() {
		return this.lastEncounteredFailure;
	}

	/*
	 * Execute without ignoring errors.
	 */
	protected void runNormally(ExecutionUnitRunner child, RunNotifier notifier) {
		child.run(notifier);
	}

	/*
	 * Execute and ignore errors.
	 */
	protected void runIgnoringErrors(ExecutionUnitRunner child, final RunNotifier notifier) {

		setFinishedEarly(true);
		final RunListener listener = new CascadingListener(notifier) {
			public void testFailure(Failure failure) throws Exception {
				setLastEncounteredFailure(failure);
				setFinishedEarly(false);
				getRunNotifier().fireTestAssumptionFailed(failure);
			}
		};

		RunNotifier customNotifier = new RunNotifier();
		customNotifier.addListener(listener);

		child.run(customNotifier);
	}
}

class CascadingListener extends RunListener {
	private RunNotifier notifier;

	public CascadingListener(RunNotifier notifier) {
		this.notifier = notifier;
	}

	public RunNotifier getRunNotifier() {
		return this.notifier;
	}

	public void setRunNotifier(RunNotifier notifier) {
		this.notifier = notifier;
	}

	@Override
	public void testRunStarted(Description description) throws Exception {
		notifier.fireTestRunStarted(description);
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		notifier.fireTestRunFinished(result);
	}

	@Override
	public void testStarted(Description description) throws Exception {
		notifier.fireTestStarted(description);
	}

	@Override
	public void testFinished(Description description) throws Exception {
		notifier.fireTestFinished(description);
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		notifier.fireTestFailure(failure);
	}

	@Override
	public void testAssumptionFailure(Failure failure) {
		notifier.fireTestAssumptionFailed(failure);
	}

	@Override
	public void testIgnored(Description description) throws Exception {
		notifier.fireTestIgnored(description);
	}
}
