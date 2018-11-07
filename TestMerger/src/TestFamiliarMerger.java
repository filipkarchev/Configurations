import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureModelVariable;

public class TestFamiliarMerger {

	private static int numberTestedConfigurations;
	private final static String pathToFamiliarFmFile = "/Users/teracomm/Documents/Trento/Research Project/fm_updated.fam";

	public static void main(String[] args) {
		System.out.println("Starting TestedFamiliarMerger");
		
		//Read from the console some parameters
		System.out.println("Enter desired number of configurations that will be tested: ");
		Scanner scanner = new Scanner(System.in);
		numberTestedConfigurations = scanner.nextInt();
		scanner.close();
		
		
		// %%%%%%%%% Read the content of the FM file and parse it to FAMILIAR object
		FeatureModelVariable fmv = null;
		String fMString = "";
		try {
			fMString = TreeUtils.readFile(pathToFamiliarFmFile, StandardCharsets.UTF_8);
			fmv = new FMParser().getFM("fm1", fMString);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("*******Generate " + numberTestedConfigurations + " random feature configurations");

		// Validate number of configurations
		if (numberTestedConfigurations < 0 || numberTestedConfigurations > 1000) {
			System.out.println("Invalid number of configurations");
			return;
		}

		// %%%%%%%%% Store bunch of random generated configurations
		ArrayList<ConfigurationVariable> listConf = new ArrayList<ConfigurationVariable>();

		ConfigurationVariable conf ;
		for (int i = 0; i < numberTestedConfigurations; i++) {
			System.out.print(".");

			// Get random configuration
			conf = FMUtils.getRandomConfiguration(fmv);

			// Add the configuration to the list
			listConf.add(conf);
		}

		
		System.out.println("\nConfigurations Generated");
		
		System.out.println("Start merging all configurations");

		// %%%%%%%%% Start merging the configurations
		ConfigurationsBuilderFam builder = new ConfigurationsBuilderFam();

		long timeMergeStart = System.currentTimeMillis();

		for (ConfigurationVariable confV : listConf) {
			// Add this configuration(merge them)
			builder.addConfiguration(confV);
			System.out.print(".");
		}

		System.out
				.println("\n***Configurations merged for: " + (System.currentTimeMillis() - timeMergeStart) / 1000 + "s");

		// %%%%%%%%% Merge with one new configuration

		// Generate new configuration
		ConfigurationVariable newConf = FMUtils.getRandomConfiguration(fmv);
		timeMergeStart = System.currentTimeMillis();

		// Add it
		builder.addConfiguration(newConf);
		System.out.println("***Possible new configuration merged for: "
				+ (System.currentTimeMillis() - timeMergeStart) / 1000 + "s");

		// %%%%%%%%% Check existing configuration if it is tested
		timeMergeStart = System.currentTimeMillis();

		System.out.println("Existing configuration reported as: " + builder.checkConfiguration(newConf) + " for: "
				+ +(System.currentTimeMillis() - timeMergeStart) / 1000 + "s");

		// %%%%%%%%% Check possible new configuration if it is tested
		timeMergeStart = System.currentTimeMillis();

		System.out.println("New configuration reported as: " + builder.checkConfiguration(FMUtils.getRandomConfiguration(fmv)) + " for: "
				+ +(System.currentTimeMillis() - timeMergeStart) / 1000 + "s");

		//Find the amount of constrains
		System.out.println("Number of constraints: " + builder.getNumberOfConstraints());
		
	}
}
