import fr.familiar.operations.Mode;
import fr.familiar.variable.Comparison;
import fr.familiar.variable.ConfigurationVariable;
import fr.familiar.variable.FeatureModelVariable;

public class ConfigurationsBuilderFam {

	private FeatureModelVariable testedConfigurationModel;

	public void addConfiguration(ConfigurationVariable conf) {
		// Check if tested Model is empty
		if (testedConfigurationModel == null) {
			// Init the FM with the configuration
			testedConfigurationModel = new FeatureModelVariable("", conf.asFM());
		}

		// Convert the configuration to FM
		FeatureModelVariable fm_conf = new FeatureModelVariable("", conf.asFM());

		// Merge the configurations
		testedConfigurationModel = testedConfigurationModel.merge(fm_conf, Mode.Union);
	}

	public boolean checkConfiguration(ConfigurationVariable conf) {
		// Check if tested Model is empty
		if (testedConfigurationModel == null) {
			return false;
		}

		// Convert the configuration to FM
		FeatureModelVariable fm_conf = new FeatureModelVariable("", conf.asFM());

		// Get the result of the comparison
		Comparison result = testedConfigurationModel.compare(fm_conf);

		// Check the result of the compare to find if the configuration is tested
		return checkConfigurationExists(result);
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
}
