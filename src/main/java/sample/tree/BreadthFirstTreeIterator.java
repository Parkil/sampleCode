package sample.tree;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/*
 * See URL: http://en.wikipedia.org/wiki/Breadth-first_search
 */
public class BreadthFirstTreeIterator implements Iterator<TreeNode> {

    private static final int ROOT = 0;

    private final LinkedList<TreeNode> list;
    private final Map<Integer, ArrayList<String>> levels;

    public BreadthFirstTreeIterator(Map<String, TreeNode> treeMap, String identifier) {
        list = new LinkedList<>();
        levels = new ConcurrentSkipListMap<>(); // avoid ConcurrentModificationException

        if (treeMap.containsKey(identifier)) {
            this.buildList(treeMap, identifier, ROOT);

            for (Map.Entry<Integer, ArrayList<String>> entry : levels.entrySet()) {
                for (String child : entry.getValue()) {
                    list.add(treeMap.get(child));
                }
            }
        }
    }

    private void buildList(Map<String, TreeNode> tree, String identifier, int level) {
        if (level == ROOT) {
            list.add(tree.get(identifier));
        }

        List<String> children = tree.get(identifier).getChildNodeList();

        levels.computeIfAbsent(level, k-> levels.put(k, new ArrayList<>()));

        for (String child : children) {
            levels.get(level).add(child);

            // Recursive call
            this.buildList(tree, child, level + 1);
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