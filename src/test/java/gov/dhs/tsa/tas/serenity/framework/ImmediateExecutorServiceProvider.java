package gov.dhs.tsa.tas.serenity.framework;

import java.util.concurrent.ExecutorService;

import com.google.common.util.concurrent.MoreExecutors;

import net.thucydides.core.reports.ExecutorServiceProvider;

/*
 * Serenity Reports are generated incorrectly quite often due to 
 * numerous threading issues / dependencies between the different reporters.  
 * 
 * The reason these report generation issues aren't typically seen 
 * is because each test result is actually regenerated multiple times 
 * over the course of the test suite.  The first test result report could be 
 * regenerated as much as 20 times.  This is also why the last result in 
 * a report is the most often to be rendered incorrectly.
 * 
 * If we want to efficiently generate reports (generate the reports only once),
 * then we need a much more stable way of doing so.
 * 
 * Generating the reports sequentially in the same thread 
 * produces consistent, accurate results.
 * 
 */
public class ImmediateExecutorServiceProvider implements ExecutorServiceProvider {

	public ExecutorService getExecutorService() {
		return MoreExecutors.newDirectExecutorService();
	}
}
