import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class BSTvsBBSTClient {

    /* data structure size */
    private static final int SIZE = 5000;

    private static final int CYCLES = 100000;

    public static void main(String[] args) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        BalancedBinarySearchTree<Integer> bbst = new BalancedBinarySearchTree<>();

        StdOut.println("- Testing client comparing BinarySearchTree.java and java");
        StdOut.println("- Compares worst case and average case for searches with trees of size " + SIZE);

        for (int i = SIZE - 1; i >= 0; i--) {
            bbst.put(i, i);
            bst.put(i, i);
        }

        StdOut.println("\nWorst case for " + CYCLES + " cycles:");
        StdOut.println("BST: O(n) for degenerate tree");
        Stopwatch s1 = new Stopwatch();
        for (int i = 0; i < CYCLES; i++)
            bst.get(0);
        StdOut.println("Time: " + s1.elapsedTime() + " s.");

        StdOut.println("BBST: O(logn) for degenerate tree");
        Stopwatch s2 = new Stopwatch();
        for (int i = 0; i < CYCLES; i++)
            bbst.get(0);
        StdOut.println("Time: " + s2.elapsedTime() + " s.");

        StdOut.println("\nAverage case for " + CYCLES + " cycles:");
        StdOut.println("BST: O(logn)");
        s1 = new Stopwatch();
        for (int i = 0; i < CYCLES; i++) {
            int get = (int) (Math.random() * SIZE);
            bst.get(get);
        }
        StdOut.println("Time: " + s1.elapsedTime() + " s.");

        StdOut.println("BBST: O(logn)");
        s2 = new Stopwatch();
        for (int i = 0; i < CYCLES; i++) {
            int get = (int) (Math.random() * SIZE);
            bbst.get(get);
        }
        StdOut.println("Time: " + s2.elapsedTime() + " s.");
    }
}
