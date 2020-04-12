package gov.dhs.tsa.tas.serenity.logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.serenitybdd.core.Serenity;

public class DebuggingMetadataLogger {
	public final static String DEBUGGING_METADATA_KEY = "DEBUGGING_METADATA";

	public static void createSessionMap() {
		Serenity.setSessionVariable(DEBUGGING_METADATA_KEY).to(new TreeMap<String, List<String>>());
	}

	public static Map<String, List<String>> getSessionMap() {
		return Serenity.sessionVariableCalled(DEBUGGING_METADATA_KEY);
	}

	public static void storeDebuggingMetadata(String key, Object value) {
		Map<String, List<String>> metadata = Serenity.sessionVariableCalled(DEBUGGING_METADATA_KEY);
		if (!metadata.containsKey(key)) {
			metadata.put(key, new ArrayList<String>());
		}
		metadata.get(key).add(value + "");
	}

	public static void clearDebuggingMetadata(String key) {
		Map<String, List<String>> metadata = Serenity.sessionVariableCalled(DEBUGGING_METADATA_KEY);
		metadata.remove(key);
	}
}
