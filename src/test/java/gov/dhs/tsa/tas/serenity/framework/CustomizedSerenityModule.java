package gov.dhs.tsa.tas.serenity.framework;

import net.thucydides.core.reports.ExecutorServiceProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/*
 * This class allows you to overwrite the dependencies injected by Serenity with your own.
 * Please see @net.thucydides.core.guice.ThucydidesModule for a non-comprehensive list 
 * of dependencies you can replace.
 * 
 */
public class CustomizedSerenityModule extends AbstractModule {

	/*
	 * Overwrite dependencies here.
	 */
	protected void configure() {
		bind(ExecutorServiceProvider.class).to(ImmediateExecutorServiceProvider.class).in(Singleton.class);
	}
}
