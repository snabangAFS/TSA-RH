package gov.dhs.tsa.tas.serenity.framework;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.reports.OutcomeFormat;
import net.thucydides.core.reports.TestOutcomeLoader;
import net.thucydides.core.reports.TestOutcomes;

public class EnsureNoTestFailures {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Please pass input folder");
			System.exit(1);
		}
		try {
			File sourceDirectory = new File(args[0]);
			TestOutcomes outcomesFilter = TestOutcomeLoader.loadTestOutcomes().inFormat(OutcomeFormat.JSON)
					.from(sourceDirectory);

			Set<TestOutcome> collapsedPositiveOutcomes = createTestOutcomeComparatorSet();
			addTestOutcomesToSet(collapsedPositiveOutcomes, outcomesFilter.getPassingTests());

			Set<TestOutcome> collapsedNegativeOutcomes = createTestOutcomeComparatorSet();
			addTestOutcomesToSet(collapsedNegativeOutcomes, outcomesFilter.getErrorTests());
			addTestOutcomesToSet(collapsedNegativeOutcomes, outcomesFilter.getFailingTests());

			Set<TestOutcome> allFailedOutcomes = findUnmatchedOutcomes(collapsedNegativeOutcomes,
					collapsedPositiveOutcomes);
			Set<TestOutcome> allPassedOutcomes = findUnmatchedOutcomes(collapsedPositiveOutcomes,
					collapsedNegativeOutcomes);
			Set<TestOutcome> mixedOutcomes = findMatchedOutcomes(collapsedNegativeOutcomes, collapsedPositiveOutcomes);

			for (TestOutcome outcome : allPassedOutcomes) {
				log(outcome, "Looks good.  The test passed on the first try.");
			}
			for (TestOutcome outcome : allFailedOutcomes) {
				log(outcome, "Failed every single time.  Please investigate why this test is consistently failing.");
			}
			for (TestOutcome outcome : mixedOutcomes) {
				log(outcome, "is unstable.  It passed eventually, but we will fail the build regardless.");
			}

			System.exit(allFailedOutcomes.size() + mixedOutcomes.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}

	private static void log(TestOutcome outcome, String message) {
		System.out.println(getFeatureName(outcome) + " - " + getScenarioName(outcome) + ": " + message);
	}

	// set theory: (A+B)-((A-B)+(B-A)) = similarity
	// however, need to ensure that this is a set. Otherwise you have 2x the
	// expected amount of outcomes being returned.
	protected static Set<TestOutcome> findMatchedOutcomes(Collection<TestOutcome> outcomesA,
			Collection<TestOutcome> outcomesB) {
		Set<TestOutcome> combinedOutcomes = createTestOutcomeComparatorSet();
		combinedOutcomes.addAll(outcomesA);
		combinedOutcomes.addAll(outcomesB);
		Set<TestOutcome> combinedDifferences = createTestOutcomeComparatorSet();
		combinedDifferences.addAll(findUnmatchedOutcomes(outcomesA, outcomesB));
		combinedDifferences.addAll(findUnmatchedOutcomes(outcomesB, outcomesA));
		return findUnmatchedOutcomes(combinedOutcomes, combinedDifferences);
	}

	protected static Set<TestOutcome> createTestOutcomeComparatorSet() {
		return new TreeSet<TestOutcome>(new Comparator<TestOutcome>() {
			@Override
			public int compare(TestOutcome arg0, TestOutcome arg1) {
				int featureComparison = getFeatureName(arg0).compareTo(getFeatureName(arg1));
				if (featureComparison != 0) {
					return featureComparison;
				}

				int scenarioComparison = getScenarioName(arg0).compareTo(getScenarioName(arg1));
				return scenarioComparison;
			}
		});
	}

	protected static Set<TestOutcome> findUnmatchedOutcomes(Collection<TestOutcome> outcomesBeingFiltered,
			Collection<TestOutcome> outcomesBeingComparedAgainst) {
		Set<TestOutcome> unmatchedOutcomes = createTestOutcomeComparatorSet();
		unmatchedOutcomes.addAll(outcomesBeingFiltered);
		unmatchedOutcomes.removeAll(outcomesBeingComparedAgainst);
		return unmatchedOutcomes;
	}

	protected static String getFeatureName(TestOutcome outcome) {
		return outcome.getTagValue("feature").get();
	}

	protected static String getScenarioName(TestOutcome outcome) {
		String name = outcome.getName() + "";
		int index = name.lastIndexOf("-");
		// gets everything except for the 'retry' suffix. ie instead
		// of 'MyScenario-2'this method returns 'My Scenario'
		if (index != -1) {
			try {
				Integer.parseInt(name.substring(index + 1).trim());
				return name.substring(0, index);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return name;
	}

	private static void addTestOutcomesToSet(Set<TestOutcome> list, TestOutcomes outcomes) {
		for (TestOutcome outcome : outcomes.getTests()) {
			list.add(outcome);
		}
	}
}
