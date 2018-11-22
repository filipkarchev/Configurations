package builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fm.StorageManager;
import fr.familiar.experimental.FGroup;
import fr.familiar.variable.FeatureModelVariable;

public class OrderManager {
private String featureModelPath="";
private FeatureModelVariable fmv;
private List<String> extractedFeaturesKeys = new ArrayList<String>();
private static OrderManager instance;

	public static OrderManager getInstance()
	{
		return instance;
	}
	
	public static OrderManager getInstance(String featureModelPath)
	{
		return new OrderManager(featureModelPath);
	}
	
	public OrderManager(String featureModelPath)
	{
		instance = this;
		
		this.featureModelPath = featureModelPath;
		init();
		
		this.extractedFeaturesKeys = extractOrder();
	}


	private void init() {
		StorageManager storage = new StorageManager();
		//fmv = storage.loadFeatureModelFromXmlFile(featureModelPath);
		fmv = storage.loadFeatureModelFromFile(featureModelPath);
	}
	
	private ArrayList<String> extractOrder() {
		ArrayList<String> list = new ArrayList<String>();
		
		//Get all groups. Things that could be selected
		Set<FGroup> groups = fmv.getGroups();
		
		for(FGroup group:groups)
		{
			list.add(group.getTarget().getFeature());
		}
		return list;
	}
	
	public List<String> getKeys()
	{
		return extractedFeaturesKeys;
	}
	
	public FeatureModelVariable getFmv()
	{
		return fmv;
	}
	
}
