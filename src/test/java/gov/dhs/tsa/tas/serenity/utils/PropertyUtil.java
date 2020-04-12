package gov.dhs.tsa.tas.serenity.utils;

public class PropertyUtil {

	private static String envProperty = "";

	private static final PropertyUtil INSTANCE = new PropertyUtil();

	private PropertyUtil() {

	}

	public static PropertyUtil getInstance() {
		return INSTANCE;
	}

	public String getProperty(String name) {

		envProperty = "";

		if (System.getProperty(name) != null) {
			envProperty = System.getProperty(name);
		}

		return envProperty;
	}

}
