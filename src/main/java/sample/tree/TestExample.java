package sample.tree;

import java.util.Iterator;

public class TestExample {

    private static JavaTree init() {
        JavaTree javaTree = new JavaTree();

        javaTree.addNode("Harry");
        javaTree.addNode("Jane", "Harry");
        javaTree.addNode("Bill", "Harry");
        javaTree.addNode("Joe", "Jane");
        javaTree.addNode("Diane", "Jane");
        javaTree.addNode("George", "Diane");
        javaTree.addNode("Mary", "Diane");
        javaTree.addNode("Jill", "George");
        javaTree.addNode("Carol", "Jill");
        javaTree.addNode("Grace", "Bill");
        javaTree.addNode("Mark", "Jane");

        return javaTree;
    }

    private static void baseTest() {
        init().printTree("Harry");
    }

    private static void listToRootTest() {
        for(TreeNode treeNode : init().getNodeListToRoot("Mark")) {
            System.out.println(treeNode.getId());
        }
    }

    private static void depthFirstIterTest() {
        Iterator<TreeNode> depthIterator = init().iterator("Harry");

        while (depthIterator.hasNext()) {
            TreeNode node = depthIterator.next();
            System.out.println(node.getId());
        }
    }

    private static void breadthFirstIterTest() {
        Iterator<TreeNode> breadthIterator = init().iterator("Harry",TraversalStrategy.BREADTH_FIRST);

        while (breadthIterator.hasNext()) {
            TreeNode node = breadthIterator.next();
            System.out.println(node.getId());
        }
    }

    public static void main(String[] args) {
        baseTest();
        System.out.println("------------------");
        listToRootTest();
        System.out.println("------------------");
        depthFirstIterTest();
        System.out.println("------------------");
        breadthFirstIterTest();
    }
}