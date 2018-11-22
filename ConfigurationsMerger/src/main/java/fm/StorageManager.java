package fm;
/**
 * 
 */


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

import fr.familiar.fm.FeatureModelChecker;
import fr.familiar.fm.converter.SPLOTtoFML;
import fr.familiar.interpreter.DefaultOutput;
import fr.familiar.interpreter.FMLAssertionError;
import fr.familiar.interpreter.FMLBasicInterpreter;
import fr.familiar.interpreter.FMLFatalError;
import fr.familiar.interpreter.FMLShell;
import fr.familiar.parser.FMLCommandInterpreter;
import fr.familiar.variable.FeatureModelVariable;
import fr.familiar.variable.Variable;
import splar.core.fm.FeatureModelException;
import splar.core.fm.XMLFeatureModel;

/**
 * @author fitsum
 *
 */
public class StorageManager {
	
	FeatureModelChecker featureModelChecker;
	FMLBasicInterpreter fmlInterpreter;
	FMLShell fmlShell;
	
	public StorageManager() {
		fmlInterpreter = new FMLBasicInterpreter();
		
		fmlShell = new FMLShell(System.in, new DefaultOutput(), false);
		File basePath = new File("");
		fmlShell.addPath(basePath.getAbsoluteFile());
	}
	
	/**
	 * Use the FML shell to load compatible feature models from file.
	 * 
	 * TODO clarify the path search strategy, it gives errors if there are multiple files
	 * in different locations.
	 * @param fmPath
	 * @return
	 */
	public FeatureModelVariable loadFeatureModelFromFile (String fmPath) {
		File f = new File(fmPath);
		if(!f.exists()) { return null;}
		String cmd = "fm=FM('"+ fmPath +"')";
		Variable variable = fmlShell.parse(cmd);
		FeatureModelVariable fmv = (FeatureModelVariable)variable;
		return fmv;
	}
	
	
	public FeatureModelVariable loadFeatureModelFromXmlFile (String fmPath) {
		File splotFile = new File (fmPath);
		splar.core.fm.FeatureModel featureModelSPLOT = new XMLFeatureModel(
				splotFile.getAbsolutePath(),
				XMLFeatureModel.USE_VARIABLE_NAME_AS_ID);
		try {
			featureModelSPLOT.loadModel();
		} catch (FeatureModelException e) {
			System.err.println("Unable to load SPLOT feature model "
							+ e.getMessage());
		}

		gsd.synthesis.FeatureModel<String> rFM = new SPLOTtoFML().convertToFeatureModel(featureModelSPLOT);
		FeatureModelVariable fmv = new FeatureModelVariable(splotFile.getName(), rFM) ;
		
		
		return fmv;
	}
	
	public FeatureModelVariable loadFeatureModelFromString (String strFm) {
		Variable eval;
		try {
			eval = fmlInterpreter.eval(strFm);
			FeatureModelVariable fmVar = (FeatureModelVariable)eval;
			if (fmVar.isValid()) {
				return fmVar;
			}
		} catch (FMLFatalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FMLAssertionError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String serializeFM2SPLOT (String strFM, String splotFileName) {
		// if filename supplied with extension, remove the extension
		int posDot = splotFileName.indexOf(".");
		if (posDot >=0 ) {
			splotFileName = splotFileName.substring(0, posDot);
		}
		fmlShell.parse(splotFileName + " = " + strFM);
		fmlShell.parse("serialize " + splotFileName + " into SPLOT");
		String pathTosplotFM = "output/" + splotFileName + ".xml"; // FIXME figure out how to parameterize serialize output file
		return pathTosplotFM;
	}
	
	public FeatureModelVariable getFM(String fmID,String fmSpecification) throws Exception {
		FMLShell _shell = FMLShell.instantiateStandalone(null);
		FMLCommandInterpreter _environment = _shell.getCurrentEnv();
		
		
		String actualFmSpecification = "";
		if (!fmSpecification.startsWith("FM(")) {
			actualFmSpecification = "FM(" + fmSpecification + ")";
		} else
			actualFmSpecification = fmSpecification;

		_shell.parse(fmID + " = " + actualFmSpecification + "\n");
		
		Variable v = _environment.getVariable(fmID);
		FeatureModelVariable fmv = (FeatureModelVariable) v;
		return fmv;
	}
	
	private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public FeatureModelVariable leadFMFromFile(String file)
	{
		FeatureModelVariable fmv = null;
		String fMString = "";
		try {
			fMString = readFile(file, StandardCharsets.UTF_8);
			fmv = getFM("fm1", fMString);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return fmv;
	}
	
	
}
