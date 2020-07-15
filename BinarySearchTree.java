import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

public class BinarySearchTree<T> {

    Node root;

    public BinarySearchTree() {
        root = null;
    }

    /* inserts the specified key-value pair into the symbol table, overwriting the old value with the new value if the symbol table already contains the specified key */
    public void put(int key, T element) {
        root = put(root, key, element);
    }

    private Node put(Node node, int key, T element) {
        if (node == null)
            return new Node(key, element);
        if (key < node.key)
            node.left = put(node.left, key, element);
        else if (key > node.key)
            node.right = put(node.right, key, element);
        else
            node.element = element;
        return node;
    }

    /* gets value at given key */
    public T get(int key) {
        return get(root, key);
    }

    private T get(Node node, int key) {
        if (node == null)
            return null;
        if (key < node.key)
            return get(node.left, key);
        else if (key > node.key)
            return get(node.right, key);
        else
            return node.element;
    }

    /* removes the specified key and its associated value from this symbol table (if the key is in this symbol table) */
    public void delete(int key) {
        root = delete(root, key);
    }

    private Node delete(Node node, int key) {
        if (node == null)
            return null;
        if (key < node.key)
            node.left = delete(node.left, key);
        else if (key > node.key)
            node.right = delete(node.right, key);
        else if (node.right == null)
            return node.left;
        else if (node.left == null)
            return node.right;
        else {
            Node temp = node;
            node = min(temp.right);
            node.right = deleteMin(temp.right);
            node.left = temp.left;
        }
        return node;
    }

    /* deletes minimum */
    private Node deleteMin(Node node) {
        if (node.left == null)
            return node.right;
        node.left = deleteMin(node.left);
        return node;
    }

    /* returns the largest key in the symbol table */
    public int max() {
        if (root == null)
            return 0;
        return max(root).key;
    }

    private Node max(Node node) {
        if (node.right == null)
            return node;
        else
            return max(node.right);
    }

    /* returns the smallest key in the symbol table */
    public int min() {
        if (root == null)
            return 0;
        return min(root).key;
    }

    private Node min(Node node) {
        if (node.left == null)
            return node;
        else
            return min(node.left);
    }

    /* checks whether tree contains given key */
    public boolean contains(int key) {
        return get(key) != null;
    }

    /* returns height of tree*/
    public int height() {
        return height(root) + 1;
    }

    private int height(Node node) {
        if (node == null)
            return -1;
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    /* returns the number of key-value pairs in this symbol table */
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null)
            return 0;
        return size(node.left) + size(node.right) + 1;
    }

    /* returns whether tree is empty */
    public boolean isEmpty() {
        return root == null;
    }

    /* in order traversal wrapper function */
    public void levelOrder() {
        int height = height();
        for (int i = 1; i <= height; i++)
            levelOrder(root, i);
        StdOut.println();
    }

    /* level order traversal helper function */
    private void levelOrder(Node root, int level) {
        if (root == null)
            return;
        if (level == 1)
            StdOut.print(root.element + " ");
        else if (level > 1) {
            levelOrder(root.left, level - 1);
            levelOrder(root.right, level - 1);
        }
    }

    /* in order traversal wrapper function */
    public void inOrder() {
        inOrder(root);
        StdOut.println();
    }

    /* in order traversal helper function */
    private void inOrder(Node node) {
        if (node != null) {
            inOrder(node.left);
            StdOut.print(node.element + " ");
            inOrder(node.right);
        }
    }

    /* post order traversal wrapper function */
    public void postOrder() {
        postOrder(root);
        StdOut.println();
    }

    /* post order traversal helper function */
    private void postOrder(Node node) {
        if (node == null)
            return;
        /* recursion of left node */
        postOrder(node.left);
        /* recursion of right node */
        postOrder(node.right);
        /* of current node */
        StdOut.print(node.element + " ");
    }

    /* pre order traversal wrapper function */
    public void preOrder() {
        preOrder(root);
        StdOut.println();
    }

    /* pre order traversal helper function */
    private void preOrder(Node node) {
        if (node == null)
            return;
        /* of current node */
        StdOut.print(node.element + " ");
        /* recursion of left node */
        preOrder(node.left);
        /* recursion of right node */
        preOrder(node.right);
    }

    /* recursive visualizeTree */
    public String visualizeTree() {
        return visualizeTree("", true, "", root);
    }

    private String visualizeTree(String prev, boolean tail, String s, Node n) {
        if (n == null)
            return "";

        if (n.right != null) {
            String temp = "";
            temp += prev;
            if (tail)
                temp += "│   ";
            else
                temp += "    ";
            s = visualizeTree(temp, false, s, n.right);
        }

        s += prev;
        if (tail)
            s += "└─ ";
        else
            s += "┌─ ";
        s += n.element.toString() + "\n";

        if (n.left != null) {
            String temp = "";
            temp += prev;
            if (tail)
                temp += "    ";
            else
                temp += "│   ";
            s = visualizeTree(temp, true, s, n.left);
        }
        return s;
    }

    /* node class */
    public class Node {
        /* elements */
        int key;
        T element;
        Node left, right;

        /* constructor */
        public Node(int key, T element) {
            this.key = key;
            this.element = element;
            this.left = this.right = null;
        }

    }
}
