package sample.tree;

import java.util.ArrayList;
import java.util.List;

/*
 * tree node
 */
public class TreeNode {

    private final String id;
    private final String parentId;
    private final int level;
    private final List<String> childNodeList = new ArrayList<>();
    private final Object attach;

    public TreeNode(String id, String parentId, int level, Object attach) {
        this.id = id;
        this.parentId = parentId;
        this.level = level;
        this.attach = attach;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public int getLevel() {
        return level;
    }

    public List<String> getChildNodeList() {
        return childNodeList;
    }

    public Object getAttach() {
        return attach;
    }

    public void addChild(String identifier) {
        childNodeList.add(identifier);
    }
}