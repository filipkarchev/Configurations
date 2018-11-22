package config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Configuration{
	private Map<String, List<String>> features;

	public Configuration() {
		features = new HashMap<String, List<String>>();
	}

	
	public Configuration(Map<String, List<String>> fs) {
		features = new HashMap<String, List<String>>();
		features.putAll(fs);
	}
	
	public Map<String, List<String>> getFeatures() {
		return features;
	}

	public void setFeatures(Map<String, List<String>> features) {
		this.features = features;
	}
	
}
