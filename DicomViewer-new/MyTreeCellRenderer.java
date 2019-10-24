package view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;


public class MyTreeCellRenderer extends DefaultTreeCellRenderer {
	
	private String searchString;
	
	public MyTreeCellRenderer(String searchString) {
		this.searchString = searchString;
	}

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);

        // Assuming you have a tree of Strings
        String node = (String) ((DefaultMutableTreeNode) value).getUserObject();

        // If the node is a leaf and ends with "xxx"
        if (searchString != null && leaf && node.toLowerCase().contains(searchString.toLowerCase())) {
            // Paint the node in blue
            setForeground(Color.GREEN);
//        	setBackgroundNonSelectionColor(Color.YELLOW);
        }

        return this;
    }
}