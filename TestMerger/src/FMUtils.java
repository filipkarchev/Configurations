import java.util.Random;
import java.util.Set;

import org.xtext.example.mydsl.fml.OpSelection;

import fr.familiar.parser.ConfigurationVariableFactory;
import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureModelVariable;

public class FMUtils {
	
	public static ConfigurationVariable getRandomConfiguration(FeatureModelVariable fmv)
	{
		ConfigurationVariable conf = ConfigurationVariableFactory.INSTANCE.mkBDD(fmv, "cMerge");
		return getRandomConfiguration(fmv,conf);
	}
	public static ConfigurationVariable getRandomConfiguration(FeatureModelVariable fmv, ConfigurationVariable conf)
	{
		Set<String> unselectedFeaturesSet = null;
		
		do
		{
			//Get all unselected features
			unselectedFeaturesSet = conf.getUnselected();
			Object[] arr = unselectedFeaturesSet.toArray();
			
			if(arr.length==0)break;
			
			//Text one random feature and the name of it
			int featureNumber = new Random().nextInt(arr.length);
			String feature = arr[featureNumber].toString();
			
			//Select the feature in the Configuration
			conf.applySelection(feature, OpSelection.SELECT);
			
		}
		while(unselectedFeaturesSet.size()>0);
		
		return conf;
	}
}
