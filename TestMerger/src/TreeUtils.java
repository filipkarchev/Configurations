import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TreeUtils {
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static ArrayList<JSONArray> parseJsonFM(String json) throws JSONException {
		ArrayList<JSONArray> leafList = new ArrayList<JSONArray>();
	
		JSONObject fullJson = new JSONObject(json.substring(1));
		JSONObject tree = fullJson.getJSONObject("tree");
		JSONObject firstBranch = tree.getJSONObject("branch");
		JSONArray firstArray = firstBranch.getJSONArray("branch");
		for (int i = 0; i < firstArray.length(); i++) {
			JSONObject obj = firstArray.getJSONObject(i);
			leafList = getInnerBranch(obj, leafList);
		}
		return leafList;

	}

	
	private static ArrayList<JSONArray> getInnerBranch(JSONObject obj, ArrayList<JSONArray> leafList) {
		JSONArray nextArr = obj.optJSONArray("branch");
		if (nextArr != null) {
			for (int i = 0; i < nextArr.length(); i++) {
				JSONObject nextObj = nextArr.optJSONObject(i);
				leafList = getInnerBranch(nextObj, leafList);
			}

		} else {
			JSONObject nextObj = obj.optJSONObject("branch");

			if (nextObj != null) {
				JSONArray leaves = nextObj.optJSONArray("leaf");

				if (leaves != null) {
					leafList.add(leaves);
				}else
				{
					leafList = getInnerBranch(nextObj, leafList);
				}
				
			}
			
			JSONArray array = obj.optJSONArray("leaf");
			if(array!=null)
			{
				leafList.add(array);
			}
		}
		return leafList;
	}

	public static ArrayList<ArrayList<String>> getFeaturesValuesFromArray(ArrayList<JSONArray> arr)
			throws JSONException {
		ArrayList<ArrayList<String>> values = new ArrayList<>();
		for (int i = 0; i < arr.size(); i++) {
			ArrayList<String> innerValues = new ArrayList<>();
			JSONArray list = arr.get(i);
			for (int j = 0; j < list.length(); j++) {
				// innerValues.clear();

				JSONObject nextObject = list.getJSONObject(j);
				JSONArray attribute = nextObject.getJSONArray("attribute");
				for (int k = 0; k < attribute.length(); k++) {

					JSONObject nextAttr = attribute.getJSONObject(k);
					String name = nextAttr.getString("_name");
					if (name.equals("name")) {
						String value = nextAttr.getString("_value");
						innerValues.add(value.toString());
					}
				}

			}

			values.add(innerValues);
		}

		return values;
	}

	public static ArrayList<String> getRandomConfiguration(ArrayList<ArrayList<String>> listLeavesStr) {
		ArrayList<String> conf = new ArrayList<String>();
		
		// Select one random feature from every list
		for (int i = 0; i < listLeavesStr.size(); i++) {
			int featureNumber = new Random().nextInt(listLeavesStr.get(i).size());

			String selectedFeature = listLeavesStr.get(i).get(featureNumber);
			conf.add(selectedFeature);
		}
		return conf;
	}
}
