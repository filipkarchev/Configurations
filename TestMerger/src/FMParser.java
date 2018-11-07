import fr.familiar.interpreter.FMLShell;
import fr.familiar.parser.FMLCommandInterpreter;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.Variable;

public class FMParser{
	public FeatureModelVariable getFM(String fmID,String fmSpecification) throws Exception {
		FMLShell _shell = FMLShell.instantiateStandalone(null);
		FMLCommandInterpreter _environment = _shell.getCurrentEnv();
		
		
		String actualFmSpecification = "";
		if (!fmSpecification.startsWith("FM (")) {
			actualFmSpecification = "FM (" + fmSpecification + " )";
		} else
			actualFmSpecification = fmSpecification;

		_shell.parse(fmID + " = " + actualFmSpecification + "\n");
		
		Variable v = _environment.getVariable(fmID);
		FeatureModelVariable fmv = (FeatureModelVariable) v;
		return fmv;
	}
}
