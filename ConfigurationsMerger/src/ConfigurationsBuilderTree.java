import java.util.ArrayList;
import java.util.List;

public class ConfigurationsBuilderTree {
	private TreeNode testedConfigurationTree;
	

	public boolean checkConfiguration(ArrayList<String> features) {
		// Check if tested tree is empty
		if (testedConfigurationTree == null) {
			return false;
		}

		// Check recursively if this configurations list is tested 
		return testedConfigurationTree.searchNode(features);
		
	}

	public void addConfiguration(ArrayList<String> conf) {
		// Check if tested Model is empty
		if (testedConfigurationTree == null) {
			// Init the FM with the first configuration name
			testedConfigurationTree = new TreeNode("FM");
		}

		testedConfigurationTree.addConfiguration(conf);
	}

	public List<List<TreeNode>> getTestedConfigurationsModel() {
		return testedConfigurationTree.getPaths();
	}
	
	public void printTestedConfigurations()
	{
		List<List<TreeNode>> lists = testedConfigurationTree.getPaths();
		System.out.println("list size: " +lists.size() );
        for(List<TreeNode> list : lists) {
            for(int count = 0; count < list.size(); count++) {
                System.out.print(list.get(count));
                if(count != list.size() - 1) {
                    System.out.print("-");
                }
            }
            System.out.println();
        }
         
	}
	


}
