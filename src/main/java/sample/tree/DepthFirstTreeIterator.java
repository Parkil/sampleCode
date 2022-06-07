package sample.tree;

import java.util.*;

/*
 * See URL: http://en.wikipedia.org/wiki/Depth-first_search
 */
public class DepthFirstTreeIterator implements Iterator<TreeNode> {
    private final LinkedList<TreeNode> list;

    public DepthFirstTreeIterator(Map<String, TreeNode> tree, String identifier) {
        list = new LinkedList<>();

        if (tree.containsKey(identifier)) {
            this.buildList(tree, identifier);
        }
    }

    private void buildList(Map<String, TreeNode> treeMap, String identifier) {
        list.add(treeMap.get(identifier));
        List<String> children = treeMap.get(identifier).getChildNodeList();
        for (String child : children) {
            // Recursive call
            this.buildList(treeMap, child);
        }
    }

    @Override
    public boolean hasNext() {
        return !list.isEmpty();
    }

    @Override
    public TreeNode next() {
        return list.poll();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

