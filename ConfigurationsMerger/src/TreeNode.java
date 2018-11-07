import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TreeNode {

	protected String data;
	protected TreeNode parent;
	protected List<TreeNode> children;


	public TreeNode(String data) {
		this.data = data;
		this.children = new LinkedList<TreeNode>();
	}

	public TreeNode addChild(String child) {
		TreeNode childNode = new TreeNode(child);
		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
	public boolean searchNode(ArrayList<String> features)
    {
    	if(features.size()==0)
    	{return true;}
    	
    	String data = features.get(0);
    	//Check if this element is part of the next level in the tree
    	TreeNode temp = new TreeNode(data);
    	if(getChildren().contains(temp))
    	{
    		features.remove(0);
    		return getChildren().get(getChildren().indexOf(temp)).searchNode(features);
    	}
		return false;
    	
    }
	
	public void addConfiguration(ArrayList<String> features)
	{
		if(features.size()==0)
    	{return;}
    	
    	String data = features.get(0);
    	
    	//Check if this element is not part of the next level in the tree
    	TreeNode temp = new TreeNode(data);
    	if(!getChildren().contains(temp))
    	{
    		addChild(data);
    	}
    	
    	//Remove the first feature since it is already tested
		features.remove(0);
		getChildren().get(getChildren().indexOf(temp)).addConfiguration(features);
		
		
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!TreeNode.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final TreeNode other = (TreeNode) obj;
        if ((this.data == null) ? (other.data != null) : !this.data.equals(other.data)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }

	
    public List<List<TreeNode>> getPaths() {
        List<List<TreeNode>> retLists = new ArrayList<>();

        if(getChildren().size() == 0) {
            List<TreeNode> leafList = new LinkedList<>();
            leafList.add(this);
            retLists.add(leafList);
        } else {
            for (TreeNode node : getChildren()) {
                List<List<TreeNode>> nodeLists = node.getPaths();
                for (List<TreeNode> nodeList : nodeLists) {
                    nodeList.add(0, this);
                    retLists.add(nodeList);
                }
            }
        }

        return retLists;
    }

    public String toString() { 
        return getData();
    } 
 
}