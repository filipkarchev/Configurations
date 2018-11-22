package tree;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import builder.ConfigurationManager;
import builder.MergerType;
import builder.OrderManager;
import config.Configuration;
import config.ConfigurationStatus;
import config.FeatureConfiguration;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.Variable;

public class ConfigurationsBuilderTree implements ConfigurationManager{
	private TreeNode testedConfigurationTree;
	private String treeFileName = "";

	public ConfigurationsBuilderTree(String filePath) {
		//Read serialized file and parse it to the tree
		treeFileName = filePath; 
		try {
			testedConfigurationTree = getTreeFromFile(filePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error"+ e.toString());
		}
	}


	public ConfigurationStatus determineConfigurationStatus(Configuration newConf) {
		Map<String, List<String>> features = newConf.getFeatures();
		ArrayList<String> featuresOrdered = getOrderedFeatures(features); 
		
		// Check if tested tree is empty
		if (testedConfigurationTree == null) {
			return ConfigurationStatus.UNKNOWN;
		}
		
		if(!checkIsValid(featuresOrdered)) {
			return ConfigurationStatus.UNKNOWN;
		}else 
		// Check recursively if this configurations list is tested
		if(testedConfigurationTree.searchNode(featuresOrdered))
		{
			return ConfigurationStatus.TESTED;
		}else
		{
			return ConfigurationStatus.UNTESTED;
		}

	}

	private boolean checkIsValid(ArrayList<String> featuresOrdered) {
		//Check if values in the retrieved features are part of the feature model
		OrderManager order = OrderManager.getInstance();
		FeatureModelVariable fmv = order.getFmv();
		Set<String> setLeaves = fmv.leaves().names();
		
		//Set<String> setFeatures = fmv._getFeatureOptionalOrMandatory(false);
		
		Set<String> allValidFeatures = setLeaves;
		for(String feature: featuresOrdered)
		{
			if(!allValidFeatures.contains("'"+feature+"'") && !feature.equals(""))
			{
				// This is not a valid feature
				return false;
			}
		}
		
		return true;
	}


	private ArrayList<String> getOrderedFeatures(Map<String, List<String>> features) {
		OrderManager order = OrderManager.getInstance();
		ArrayList<String> orderList = new ArrayList<String>();
		if(order==null)
		{return null;}
		
		
		for(String item:order.getKeys())
		{
			List<String> values = features.get(item);
			//TODO values is null???
			if(values!=null && values.size()>0)
			{
				//Add Optional Elements as different level in the tree
				//orderList.addAll(values);
				
				//Add Optional Elements as one concatenated String
				String valuesStr="";
				for(String optionalValue: values)
				{
					valuesStr+= "-"+ optionalValue;
				}
				//remove first -
				valuesStr = valuesStr.replaceFirst("-", "");
				orderList.add(valuesStr);
			}else {
				orderList.add("");
			}
		}
		
		//System.out.println(Arrays.toString(orderList.toArray()));
		
		return orderList;
		
	}


	public boolean addTestedConfiguration(Configuration newConf) {
		// Check if tested Model is empty
		if (testedConfigurationTree == null) {
			System.out.println("tree is null");
			
			// Init the FM with the first configuration name
			testedConfigurationTree = new TreeNode("FM");
		}
		
		ConfigurationStatus currentStatus = determineConfigurationStatus(newConf);
		//Not valid configuration
		if(currentStatus==ConfigurationStatus.UNKNOWN)
		{
			return false;
		}else if(currentStatus==ConfigurationStatus.TESTED)
		{
			//Already presented
			return true;
		}

		testedConfigurationTree.addConfiguration(getOrderedFeatures(newConf.getFeatures()));
		
		//Update the model file
		saveTreeInFile(treeFileName,testedConfigurationTree);
		return true;
	}

	public List<List<TreeNode>> getTestedConfigurationsModel() {
		return testedConfigurationTree.getPaths();
	}

	public void printTestedConfigurations() {
		if(testedConfigurationTree==null)
		{
			return;
		}
		List<List<TreeNode>> lists = testedConfigurationTree.getPaths();
		System.out.println("list size: " + lists.size());
		for (List<TreeNode> list : lists) {
			for (int count = 0; count < list.size(); count++) {
				System.out.print(list.get(count));
				if (count != list.size() - 1) {
					System.out.print("-");
				}
			}
			System.out.println();
		}

	}

	public boolean saveTreeInFile(String address, TreeNode serializedObject) {
		ObjectOutputStream oos = null;
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(address, false);
			oos = new ObjectOutputStream(fout);
			oos.writeObject(serializedObject);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {

					e.printStackTrace();
					return false;
				}
			}
		}
	}

	public TreeNode getTreeFromFile(String filePath) throws IOException, ClassNotFoundException {
		ObjectInputStream objectinputstream = null;
		FileInputStream streamIn = new FileInputStream(filePath);
		objectinputstream = new ObjectInputStream(streamIn);
		TreeNode readCase = (TreeNode) objectinputstream.readObject();

		if (objectinputstream != null) {
			objectinputstream.close();
		}
		
		return readCase;

	}



	public void serializeModel(String filePath) {
		// TODO Auto-generated method stub
		
	}


}
