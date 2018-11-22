package builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import config.Configuration;
import config.ConfigurationStatus;
import fm.ConfigurationsBuilderFam;
import tree.ConfigurationsBuilderTree;

public class ConfigurationsBuilder implements ConfigurationManager{
	private MergerType type = MergerType.TREE;
	private ConfigurationManager manager;
	private String featureModelPath = "";
	private String testedConfPath = "";
	private List<String> extractedFeaturesKeys = new ArrayList<String>();
	
	public ConfigurationsBuilder(MergerType type, String featureModelPath, String testedConfPath)
	{
		this.type = type;
		this.featureModelPath=featureModelPath;
		this.testedConfPath=testedConfPath;
		init();
	}
	
	public ConfigurationsBuilder(String featureModelPath,String testedConfPath)
	{
		this.featureModelPath=featureModelPath;
		this.testedConfPath=testedConfPath;
		init();
	}
	
	private void init()
	{
		//Get the appropriate manager based on the selected type
		if(type==MergerType.FM)
		{
			manager = new ConfigurationsBuilderFam(testedConfPath);
		}else {
			manager = new ConfigurationsBuilderTree(testedConfPath);	
		}
		
		//Determine the order of the keys
		OrderManager order = OrderManager.getInstance(featureModelPath);
		System.out.println(""+ Arrays.toString(order.getKeys().toArray()));
		this.extractedFeaturesKeys = order.getKeys();
		
	}
	
	
	public boolean addTestedConfiguration(Configuration testedConf) {
		return manager.addTestedConfiguration(testedConf);
	}

	public ConfigurationStatus determineConfigurationStatus(Configuration clientConfig) {
		return manager.determineConfigurationStatus(clientConfig);
	}

	public void serializeModel(String filePath) {
		manager.serializeModel(filePath);
	}
	
	public List<String> getKeys()
	{
		return extractedFeaturesKeys;
	}

}
