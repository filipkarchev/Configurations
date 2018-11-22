package fm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import builder.ConfigurationManager;
import builder.OrderManager;
import config.Configuration;
import config.ConfigurationStatus;
import config.FeatureConfiguration;
import fr.familiar.operations.Mode;
import fr.familiar.parser.ConfigurationVariableFactory;
import fr.familiar.variable.Comparison;
import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureModelVariable;
import gsd.synthesis.FeatureModel;

public class ConfigurationsBuilderFam implements ConfigurationManager{

	private FeatureModelVariable testedConfigurationModel;
	private String path= "";
	public ConfigurationsBuilderFam(String filePath)
	{
		if(!filePath.equals(""))
		{
			path = filePath;
			StorageManager manager = new StorageManager();
			testedConfigurationModel = manager.leadFMFromFile(filePath);
			//System.out.println("File: " + testedConfigurationModel);
		}
	}

	public boolean addTestedConfiguration(Configuration conf) {
		FeatureConfiguration confVar = new FeatureConfiguration(OrderManager.getInstance().getFmv(),conf);
		
		
		ConfigurationStatus currentStatus = determineConfigurationStatus(conf);
		System.out.println("currentStatus: " + currentStatus);
		//Not valid configuration
		if(currentStatus==ConfigurationStatus.UNKNOWN)
		{
			return false;
		}else if(currentStatus==ConfigurationStatus.TESTED)
		{
			//Already presented
			return true;
		}

		// Convert the configuration to FM
		FeatureModelVariable fm_conf = new FeatureModelVariable("", confVar.getConfigurationVariable().asFM());

		if(testedConfigurationModel==null)
		{
			testedConfigurationModel = fm_conf;
		}else {
		System.out.println("before merge: " + testedConfigurationModel);
		// Merge the configurations
		testedConfigurationModel = testedConfigurationModel.merge(fm_conf, Mode.Union);
		System.out.println("after merge: " + testedConfigurationModel);
		}
		return updateFile();
	}

	private boolean updateFile() {
		System.out.println("Update file");

		PrintWriter out = null;
	
		File file = new File(path);
		file.delete();
		try {
			file.createNewFile();
			
			out = new PrintWriter(path);
			
			out.println("FM("+testedConfigurationModel.toString()+")");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public ConfigurationStatus determineConfigurationStatus(Configuration conf) {
		FeatureConfiguration confVar = new FeatureConfiguration(OrderManager.getInstance().getFmv(),conf);
		
	

		//It is not a valid configuration
		if(!confVar.isValid())
		{
			return ConfigurationStatus.UNKNOWN;
		}
		
		// Check if tested Model is empty
		if (testedConfigurationModel == null) {
			return ConfigurationStatus.UNTESTED;
		}
		
		//System.out.println("COnfiguration is: " + confVar.getConfigurationVariable().getDeselected());
	//	System.out.println("COnfiguration as FM is: " + confVar.getConfigurationVariable().asFM());
		// Convert the configuration to FM
		FeatureModelVariable fm_conf = new FeatureModelVariable("", confVar.getConfigurationVariable().asFM());
	//	System.out.println(confVar.getConfigurationVariable().getDeselected());
		
		
		// Get the result of the comparison
		Comparison result = testedConfigurationModel.compare(fm_conf);
		//Comparison result2 = fm_conf.compare(testedConfigurationModel);
		
		//System.out.println("Result1: " + result + ", result2: " + result2);

		// Check the result of the compare to find if the configuration is tested
		if(checkConfigurationExists(result))
		{
			return ConfigurationStatus.TESTED;
		}else
		{
			return ConfigurationStatus.UNTESTED;
		}
		
	}

	public FeatureModelVariable getTestedConfigurationsModel() {
		return testedConfigurationModel;
	}

	public int getNumberOfConstraints() {
		return testedConfigurationModel.getAllConstraints().size();
	}

	private boolean checkConfigurationExists(Comparison result) {
		// System.out.println("Compare: " + result);
		switch (result) {
		case GENERALIZATION:
		case REFACTORING:
			return true;
		default:
			return false;

		}

	}


	public void serializeModel(String filePath) {
		// TODO Auto-generated method stub
		
	}


}
