package gov.dhs.tsa.tas.serenity.framework;

import net.serenitybdd.cucumber.SerenityReporter;
import net.thucydides.core.reports.ReportService;
import net.thucydides.core.webdriver.Configuration;

/*
 * Returns a @CachingReportService rather than a @ReportService
 * 
 */
public class CachingSerenityReporter extends SerenityReporter {
	private Configuration systemConfiguration;

	public CachingSerenityReporter(Configuration systemConfiguration) {
		super(systemConfiguration);
		this.systemConfiguration = systemConfiguration;
	}

	@Override
	protected ReportService getReportService() {
		return new CachingReportService(systemConfiguration.getOutputDirectory(), ReportService.getDefaultReporters());
	}
}
