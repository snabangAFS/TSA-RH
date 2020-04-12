package gov.dhs.tsa.tas.serenity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

import cucumber.runtime.java.StepDefAnnotation;

/**
 * The {@link CucumberScanner} class provides an application that will output
 * all the step definition "value" formats in the
 * "gov.dhs.uscis.elis2.serenity.bdd.steps" package and any sub-packages into a
 * logger (which normally outputs to the console).
 */
public class CucumberScanner {

	/**
	 * The {@link Logger} instance to output the values to.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(CucumberScanner.class);

	/**
	 * Creates a new {@link CucumberScanner} instance.
	 */
	public CucumberScanner() {
		super();
	}

	/**
	 * Outputs the values contained in any {@link StepDefAnnotation} annotated
	 * annotation in any method found in the
	 * "gov.dhs.uscis.elis2.serenity.bdd.steps" package or any subpackage into a
	 * {@link Logger}.
	 * 
	 * @param args
	 *            Not used, but provided as part of the main method signature.
	 * @throws Exception
	 *             Should not be thrown, but the program will halt if this
	 *             occurs.
	 */
	public static void main(String[] args) throws Exception {
		Set<ClassInfo> classInfo = ClassPath.from(CucumberScanner.class.getClassLoader())
				.getTopLevelClassesRecursive("gov.dhs.uscis.elis2.serenity.bdd.steps");
		for (ClassInfo info : classInfo) {
			Class<?> currentClass = Class.forName(info.getName());
			Method[] methods = currentClass.getDeclaredMethods();
			for (Method method : methods) {
				for (Annotation annotation : method.getAnnotations()) {
					final Class<? extends Annotation> annotationType = annotation.annotationType();
					if (annotationType.getAnnotation(StepDefAnnotation.class) != null) {
						LOG.info("Inside " + info.getName() + " (method named " + method.getName() + "): "
								+ annotationType.getMethod("value").invoke(annotation).toString());
					}
				}
			}
		}
	}
}
