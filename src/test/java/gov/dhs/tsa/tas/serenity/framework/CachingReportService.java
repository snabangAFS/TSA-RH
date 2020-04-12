package gov.dhs.tsa.tas.serenity.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.reports.AcceptanceTestReporter;
import net.thucydides.core.reports.ReportService;
import net.thucydides.core.webdriver.Configuration;

/*
 * Greatly speeds up report generation time by reporting each test outcome only once.
 * 
 * O(n) vs O(n^2)
 * 
 */
public class CachingReportService extends ReportService {

	private static List<TestOutcome> alreadyReportedTestOutcomes = new ArrayList<TestOutcome>();

	@Inject
	public CachingReportService(final Configuration configuration) {
		super(configuration);
	}

	public CachingReportService(final File outputDirectory,
			final Collection<AcceptanceTestReporter> subscribedReporters) {
		super(outputDirectory, subscribedReporters);
	}

	public void generateReportsFor(final List<TestOutcome> testOutcomeResults) {
		List<TestOutcome> filteredTestOutcomes = null;
		synchronized (this) {
			filteredTestOutcomes = filterTestOutcomes(testOutcomeResults, alreadyReportedTestOutcomes);
			alreadyReportedTestOutcomes.addAll(filteredTestOutcomes);
		}
		super.generateReportsFor(filteredTestOutcomes);
	}

	private static List<TestOutcome> filterTestOutcomes(List<TestOutcome> allTestOutcomes,
			List<TestOutcome> alreadyReportedTestOutcomes) {
		List<TestOutcome> filteredTestOutcomes = new ArrayList<TestOutcome>();
		for (TestOutcome outcome : allTestOutcomes) {
			boolean alreadyReported = false;
			for (TestOutcome alreadyReportedTestOutcome : alreadyReportedTestOutcomes) {
				if (outcome.equals(alreadyReportedTestOutcome)) {
					alreadyReported = true;
					break;
				}
			}
			if (!alreadyReported) {
				filteredTestOutcomes.add(outcome);
			}
		}
		return filteredTestOutcomes;
	}
}
