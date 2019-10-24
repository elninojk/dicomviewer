package view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;


public class TreeCellColorRenderer extends DefaultTreeCellRenderer {
	
	private String searchString;
	private boolean flag = false;
	
	public boolean isFlag() {
		return flag;
	}

	public TreeCellColorRenderer(String searchString) {
		this.searchString = searchString;
	}

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);

        String node = (String) ((DefaultMutableTreeNode) value).getUserObject();

        if (searchString != null && leaf && node.toLowerCase().contains(searchString.toLowerCase())) {
            setForeground(Color.GREEN);
            flag = true;
        }

        return this;
    }
}