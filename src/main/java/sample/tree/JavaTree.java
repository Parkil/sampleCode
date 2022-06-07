package sample.tree;

import sample.tree.exception.ParentNodeNotFoundException;

import java.util.*;
import java.util.Map.Entry;

public class JavaTree {
    private static final int ROOT = 0;

    private final Map<String, TreeNode> nodes;
    private final TraversalStrategy traversalStrategy;


    // Constructors
    public JavaTree() {
        this(TraversalStrategy.DEPTH_FIRST);
    }

    public JavaTree(TraversalStrategy traversalStrategy) {
        this.nodes = new HashMap<>();
        this.traversalStrategy = traversalStrategy;
    }

    // Public interface
    public void addNode(String id) {
        this.addNode(id, null);
    }

    public void addNode(String id, String parentId) {
        TreeNode parentTreeNode = nodes.get(parentId);

        if(parentId != null && parentTreeNode == null) {
            throw new ParentNodeNotFoundException(String.format("parent tree node not found [parentId : %s]", parentId));
        }

        int level = 0;

        if(parentTreeNode != null) {
            level = parentTreeNode.getLevel() + 1;
            parentTreeNode.addChild(id);
        }

        TreeNode treeNode = new TreeNode(id, parentId, level, null);
        nodes.put(id, treeNode);
    }

    public void printTree(String identifier) {
        this.printTree(identifier, ROOT);
    }

    public void printTree(String identifier, int depth) {
        List<String> children = nodes.get(identifier).getChildNodeList();

        if (depth == ROOT) {
            System.out.println(nodes.get(identifier).getId()+"=="+nodes.get(identifier).getLevel());
        } else {
            String tabs = String.format("%0" + depth + "d", 0).replace("0", "    "); // 4 spaces
            System.out.println(tabs + nodes.get(identifier).getId()+"=="+nodes.get(identifier).getLevel());
        }

        depth++;

        for (String child : children) {
            // Recursive call
            this.printTree(child, depth);
        }
    }

    public Iterator<TreeNode> iterator(String identifier) {
        return this.iterator(identifier, traversalStrategy);
    }

    public Iterator<TreeNode> iterator(String identifier,
                                       TraversalStrategy traversalStrategy) {
        return traversalStrategy == TraversalStrategy.BREADTH_FIRST
                ? new BreadthFirstTreeIterator(nodes, identifier)
                : new DepthFirstTreeIterator(nodes, identifier);
    }

    public List<TreeNode> getNodeListByLevel(int level) {
        List<TreeNode> nodeList = new ArrayList<>();

        for(Entry<String, TreeNode> entry : nodes.entrySet()) {
            if(entry.getValue().getLevel() == level) {
                nodeList.add(entry.getValue());
            }
        }

        return nodeList;
    }

    public List<TreeNode> getNodeListToRoot(String identifier) {
        List<TreeNode> nodeList = new ArrayList<>();

        TreeNode temp = nodes.get(identifier);
        nodeList.add(temp);

        while(temp != null && temp.getLevel() != 0) {
            temp = nodes.get(temp.getParentId());
            nodeList.add(temp);
        }

        return nodeList;
    }
}
