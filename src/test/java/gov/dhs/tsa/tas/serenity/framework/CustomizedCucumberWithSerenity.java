package gov.dhs.tsa.tas.serenity.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.serenitybdd.cucumber.CucumberWithSerenity;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.guice.ThucydidesModule;
import net.thucydides.core.webdriver.Configuration;

import org.junit.runners.model.InitializationError;

import com.google.inject.Module;
import com.google.inject.util.Modules;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;

/*
 * Replaces specific Serenity and Cucumber classes with a custom implementation.
 * 
 * This method is directly invoked by JUnit when a @RunWith()
 * annotation is used.
 * 
 * Because this serves as the entry point into the test suite,
 * literally anything in the Serenity and Cucumber test frameworks can be overwritten
 * here.
 * 
 */
public class CustomizedCucumberWithSerenity extends CucumberWithSerenity {
	private final JUnitReporter jUnitReporter;
	private final List<FeatureRunner> children = new ArrayList<FeatureRunner>();
	private final Runtime runtime;

	public CustomizedCucumberWithSerenity(Class<?> clazz) throws InitializationError, IOException {
		super(clazz);
		ClassLoader classLoader = clazz.getClassLoader();
		RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
		RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

		ResourceLoader resourceLoader = new MultiLoader(classLoader);
		runtime = createRuntime(resourceLoader, classLoader, runtimeOptions);

		final List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);
		jUnitReporter = new JUnitReporter(runtimeOptions.reporter(classLoader), runtimeOptions.formatter(classLoader),
				runtimeOptions.isStrict());
		addChildren(cucumberFeatures);

	}

	@Override
	protected Runtime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader,
			RuntimeOptions runtimeOptions) throws InitializationError, IOException {
		return createCustomizedSerenityEnabledRuntime(resourceLoader, classLoader, runtimeOptions);
	}

	/*
	 * Overwrites certain dependencies in the default ThucydidesModule with our
	 * own CustomizedSerenityModule
	 */
	private Runtime createCustomizedSerenityEnabledRuntime(ResourceLoader resourceLoader, ClassLoader classLoader,
			RuntimeOptions runtimeOptions) {
		Module thucydidesModule = new ThucydidesModule();
		Module customModule = new CustomizedSerenityModule();
		Module overwrittenModule = Modules.override(thucydidesModule).with(customModule);

		Configuration systemConfiguration = Injectors.getInjector(overwrittenModule).getInstance(Configuration.class);
		return createCustomizedSerenityEnabledRuntime(resourceLoader, classLoader, runtimeOptions, systemConfiguration);
	}

	public static Runtime createCustomizedSerenityEnabledRuntime(ResourceLoader resourceLoader, ClassLoader classLoader,
			RuntimeOptions runtimeOptions, Configuration systemConfiguration) {
		ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
		CachingSerenityReporter reporter = new CachingSerenityReporter(systemConfiguration);
		runtimeOptions.addPlugin(reporter);
		return new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
	}

	/*
	 * Run these children.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see cucumber.api.junit.Cucumber#getChildren()
	 */
	@Override
	public List<FeatureRunner> getChildren() {
		return children;
	}

	/*
	 * Returns a set of @RerunnableFeatureRunner instead of @FeatureRunner
	 * children.
	 */
	private void addChildren(List<CucumberFeature> cucumberFeatures) throws InitializationError {
		for (CucumberFeature cucumberFeature : cucumberFeatures) {
			children.add(new RerunnableFeatureRunner(cucumberFeature, runtime, jUnitReporter));
		}
	}

}