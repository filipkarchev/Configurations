/**
 * 
 */
package config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xtext.example.mydsl.fml.OpSelection;
import org.xtext.example.mydsl.fml.SliceMode;

import builder.OrderManager;
import fm.StorageManager;
import fr.familiar.operations.FMSlicerBDD;
import fr.familiar.parser.ConfigurationVariableBDDImpl;
import fr.familiar.parser.ConfigurationVariableFactory;
import fr.familiar.parser.FMLCommandInterpreter;
import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureAttribute;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.FeatureVariable;
import fr.familiar.variable.SetVariable;
import fr.familiar.variable.Variable;
import fr.inria.familiar.fmlero.validation.AbstractFmleroJavaValidator;
import gsd.synthesis.FeatureModel;

/**
 * @author fitsum, filip
 *
 */
public class FeatureConfiguration {
	
	private FeatureModelVariable featureModelVariable;
	private ConfigurationVariable configurationVariable;
	private Map<String, List<String>> features;
	
	private boolean isValid;
	
	private ConfigurationStatus status;
	
	public FeatureConfiguration(FeatureModelVariable fmv) {
		featureModelVariable = fmv;
		isValid = true;
	}
	
	FeatureModelVariable newVar;
	public FeatureConfiguration (FeatureModelVariable fmv, Configuration clientConfiguration) {
		featureModelVariable = fmv;
		features = clientConfiguration.getFeatures();
		isValid = true;
		//configurationVariable = new ConfigurationVariableBDDImpl(featureModelVariable, "");
		
		//fmv.removeAllConstraints();
		
		//System.out.println(fmv);
		configurationVariable = ConfigurationVariableFactory.INSTANCE.mkBDD(fmv, "cMerge");
	//	configurationVariable = ConfigurationVariableFactory.INSTANCE.mkBDD(fmv, "c1");
		//configurationVariable = ConfigurationVariableFactory.INSTANCE.mkBDD(fmv, "fm1");
		boolean success = true;
		ArrayList<String> orderedFeatures = getOrderedFeatures(clientConfiguration.getFeatures());
		for (String feature : orderedFeatures) {
			if(feature.equals("")) {
				continue;
			}
			
			success = configurationVariable.applySelection("'"+feature+"'", OpSelection.SELECT);
			isValid = isValid && success;
		}
		
	
		
		Set<String> selectedFeatures = configurationVariable.getSelected();
		System.out.println("1: Selected: " + selectedFeatures);
		
		for(String unselectedFeature: configurationVariable.getUnselected())
		{
			fmv.getFeature(unselectedFeature).setOptionalStatus();
			configurationVariable.applySelection(unselectedFeature, OpSelection.DESELECT);
		}
//		
//		System.out.println("2: Selected: " + configurationVariable.getSelected());
//		System.out.println("2: Delected: " + configurationVariable.getDeselected());
//		System.out.println("2: Unselected: " + configurationVariable.getUnselected());
	}
	
//	public FeatureModelVariable modifySelected(Set<String> selectedFeatures, FeatureVariable featureVariable, FeatureModelVariable newVar)
//	{
//		
//		SetVariable children = featureVariable.children();
//		boolean childSelected = false;
//		
//		if(children.getVars().size()==0)
//		{
//			childSelected = true;
//		}
//		
//		for(Variable child: children.getVars())
//		{
//			FeatureVariable childUsed = modifySelected(selectedFeatures,(FeatureVariable) child);
//			if(childUsed!=null) {
//				childSelected=true;
//			}
//			
//			featureVariable.children().put(child.getIdentifier(), childUsed);	
//		}
//		
//		// selected
//		if(selectedFeatures.contains(featureVariable.getFtName()) && childSelected==true)
//		{
//			System.out.println(featureVariable.getFtName() + " not null");
//			return featureVariable;
//		}else
//		{
//			System.out.println(featureVariable.getFtName() + " is null");
//			return null;
//		}
//	}
	
	public boolean isValid () {
		if (isValid) {
			return configurationVariable.isValid();
		} else {
			return isValid;
		}
	}

	public ConfigurationVariable getConfigurationVariable() {
		return configurationVariable;
	}

	public void setConfigurationVariable(ConfigurationVariable configurationVariable) {
		this.configurationVariable = configurationVariable;
	}

	public FeatureModelVariable getFeatureModelVariable() {
		return featureModelVariable;
	}

	public void setFeatureModelVariable(FeatureModelVariable featureModelVariable) {
		this.featureModelVariable = featureModelVariable;
	}
	
	@Override
	public String toString() {
		if (configurationVariable != null)
			return configurationVariable.toString();
		else
			return "";
	}

	public ConfigurationStatus getStatus() {
		return status;
	}

	public void setStatus(ConfigurationStatus status) {
		this.status = status;
	}

	public Map<String, List<String>> getFeatures() {
		return features;
	}

	public void setFeatures(Map<String, List<String>> features) {
		this.features = features;
	}
	
	private ArrayList<String> getOrderedFeatures(Map<String, List<String>> features) {
		OrderManager order = OrderManager.getInstance();
		ArrayList<String> orderList = new ArrayList<String>();
		if(order==null)
		{return null;}
		
		
		for(String item:order.getKeys())
		{
			List<String> values = features.get(item);
			if(values!=null && values.size()>0)
			{
				//Add Optional Elements as different level in the tree
				orderList.addAll(values);
			}else {
				orderList.add("");
			}
		}
		
		return orderList;
	}
}


