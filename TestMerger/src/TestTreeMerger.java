import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


import org.json.JSONArray;


public class TestTreeMerger {
	private final static int numberTestedConfigurations = 10;
	private final static String pathToJsonFmFile = "/Users/teracomm/Documents/Trento/Research Project/fm_json.txt";

	public static void main(String[] args) {
		System.out.println("Starting TestedTreeMerger");
		// Get the class that creates the set
		ConfigurationsBuilderTree builder = new ConfigurationsBuilderTree();

		// %%%%%%%%% Read the content of the json file and parse it to List of Arrays of
		// Leaves
		String jsonString = "";
		ArrayList<JSONArray> listLeaves = new ArrayList<JSONArray>();
		ArrayList<ArrayList<String>> listLeavesStr = new ArrayList<ArrayList<String>>();
		try {
			jsonString = TreeUtils.readFile(pathToJsonFmFile, StandardCharsets.UTF_8);
			listLeaves = TreeUtils.parseJsonFM(jsonString);
			System.out.println(listLeaves.size());
			listLeavesStr = TreeUtils.getFeaturesValuesFromArray(listLeaves);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// %%% IMPORTANT !!!! For the purpose of the test, we assume that the order in
		// the decision tree will correspond
		// to the order of this list
		ArrayList<String> conf = new ArrayList<String>();
		long timeMergeStart = System.currentTimeMillis();
		for (int j = 0; j < numberTestedConfigurations; j++) {
			conf.clear();
			
			conf = TreeUtils.getRandomConfiguration(listLeavesStr);
			
			//Add this configuration
			builder.addConfiguration(conf);
		}

		System.out.println("***Configurations merged for: "
				+ (System.currentTimeMillis() - timeMergeStart) / 1000 + "s");
		
		//%% Check existing configuration if it is part of the tree
		timeMergeStart = System.currentTimeMillis();
		System.out.println("Existing configuration reported as: " + builder.checkConfiguration(conf) + " for: "
				+ +(System.currentTimeMillis() - timeMergeStart) / 1000 + "s");
		
		
		//% Check possible new configuration if it exist
		ArrayList<String> newConf = TreeUtils.getRandomConfiguration(listLeavesStr);
		
		timeMergeStart = System.currentTimeMillis();
		System.out.println("Possible new configuration reported as: " + builder.checkConfiguration(newConf) + " for: "
				+ +(System.currentTimeMillis() - timeMergeStart) / 1000 + "s");
		
		
		builder.printTestedConfigurations();

	}

}
