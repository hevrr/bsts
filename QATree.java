import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;
import java.io.*;

class QATree {

    /* information stored in tree */
    Node root;
    String colored;
    int win;
    int loss;

    /* constructor */
    public QATree() {
        root = new Node("");
        colored = "";
        win = 0;
        loss = 0;
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

    static class Node {
        /* data and links */
        String element;
        Node left;
        Node right;

        Node(String e) {
            element = e;
            left = null;
            right = null;
        }
    }
}
