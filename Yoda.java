import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.StringTokenizer;

public class Yoda extends JPanel implements Runnable {

    /* question and answer tree */
    private static QATree tree = new QATree();
    private static QATree.Node node = tree.root;

    /* file location for reading/writing and size */
    private static final int APPLICATION_WIDTH = 500;
    private static final int APPLICATION_HEIGHT = 720;

    /* graphics */
    private static JFrame f;
    private static JTextField textField;
    private static JButton yes;
    private static JButton no;
    private static JButton play;
    private static JLabel dialogue;
    private static JLabel background;

    /* user interface */
    private static boolean reachedAnswer = false;
    private static boolean secondInput = false;
    private static String input;
    private static String temp;

    /* font */
    private static final Font TEXT_FONT = new Font("Heiti SC", Font.PLAIN, 30);

    /* for Yoda's expressions */
    private enum Expression {
        curious, happy, angry
    }

    private static Expression expression = Expression.curious;

    /* main */
    public static void main(String[] args) {
        init();
    }

    /* initialize */
    private static void init() {
        loadQA("src/QA.txt");
        StdOut.println("- Play a game with Yoda! Just like 21 questions. Please press the red x button to exit and save data. Enjoy!");
        SwingUtilities.invokeLater(Yoda::start);
    }

    /* graphics settings */
    private Yoda() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));
        setFocusable(true);
        initializeJObjects();
        drawTree();
    }

    /* for initializing JObjects */
    private void initializeJObjects() {
        /* yes button */
        yes = new JButton("Yes");
        yes.addActionListener(e -> {
            /* after losing */
            if (secondInput) {
                dialogue.setText(convertToDialogue("I have learned! The score is now " + tree.win + ":" + tree.loss + "! Ask me more questions!"));
                /* left leaf is no, right leaf is yes */
                node.left = new QATree.Node(temp);
                node.right = new QATree.Node(input);
                secondInput = false;
                /* update visualization */
                tree.colored = input;
                playMode();

                /* after winning */
            } else if (reachedAnswer) {
                /* update wins and expression */
                tree.win++;
                dialogue.setText(convertToDialogue("Yoda wins! The score is now " + tree.win + ":" + tree.loss + "!"));
                reachedAnswer = false;
                expression = Expression.happy;
                playMode();

                /* keep going down the tree */
            } else if (!reachedFinalAnswer(node.right)) {
                node = node.right;
                tree.colored = node.element;
                dialogue.setText(convertToDialogue(node.element.substring(3)));

                /* final answer has been reached */
            } else {
                node = node.right;
                tree.colored = node.element;
                reachedAnswer = true;
            }
            /* update graphics */
            drawTree();
            drawYodas();
        });

        /* no button */
        no = new JButton("No");
        no.addActionListener(e -> {
            /* after losing */
            if (secondInput) {
                dialogue.setText(convertToDialogue("I have learned! The score is now " + tree.win + ":" + tree.loss + "! Ask me more questions!"));
                /* left leaf is no, right leaf is yes */
                node.right = new QATree.Node(temp);
                node.left = new QATree.Node(input);
                secondInput = false;
                /* update visualization */
                tree.colored = input;
                playMode();

                /* after winning */
            } else if (reachedAnswer) {
                /* update losses and expression */
                dialogue.setText(convertToDialogue("I lost! What were you thinking of?"));
                reachedAnswer = false;
                tree.loss++;
                expression = Expression.angry;
                textFieldMode();

                /* keep going down the tree */
            } else if (!reachedFinalAnswer(node.left)) {
                node = node.left;
                tree.colored = node.element;
                dialogue.setText(convertToDialogue(node.element.substring(3)));

                /* final answer has been reached */
            } else {
                node = node.left;
                tree.colored = node.element;
                reachedAnswer = true;
            }
            drawTree();
            drawYodas();
        });

        /* play button */
        play = new JButton("Play ⟳");
        play.addActionListener(e -> {
            /* setting up parameters and restarting up the tree */
            node = tree.root;
            dialogue.setText(convertToDialogue(node.element.substring(3)));
            expression = Expression.curious;
            drawYodas();
            yesNoMode();
            tree.colored = tree.root.element;
            drawTree();
        });

        /* textfield */
        textField = new JTextField();
        textField.addActionListener(e -> {
            /* get new question */
            if (!secondInput) {
                input = "A: " + textField.getText();
                temp = node.element;
                textField.setText("");
                dialogue.setText(convertToDialogue("Enter a question to distinguish " + temp.substring(3) + " and " + input.substring(3) + "."));
                secondInput = true;

                /* get yes/no answer */
            } else {
                node.element = "Q: " + textField.getText();
                dialogue.setText(convertToDialogue("And what is the answer for " + input.substring(3) + "?"));
                yesNoMode();
            }
        });
        textField.setBounds(23, 627, 455, 72);
        textField.setFont(TEXT_FONT);

        /* dialogue */
        dialogue = new JLabel(convertToDialogue("Hello, I am Yoda! Ask me questions and I will guess the answer!"));
        dialogue.setBounds(30, 500, 440, 130);
        dialogue.setFont(TEXT_FONT);
        dialogue.setHorizontalAlignment(SwingConstants.CENTER);
        dialogue.setVerticalAlignment(SwingConstants.CENTER);

        background = new JLabel();
        background.setBounds(0, 0, 500, 720);
        drawYodas();

        /* jbutton setting s*/
        JButtonSettings(play, 23, 627, 455, 70, "src/blue.png");
        JButtonSettings(no, 23, 627, 225, 70, "src/red.png");
        JButtonSettings(yes, 253, 627, 225, 70, "src/green.png");
        playMode();

        /* add everything in */
        f.add(dialogue);
        f.add(yes);
        f.add(no);
        f.add(textField);
        f.add(play);
        f.add(background);
    }

    /* until final answer is reached */
    private static boolean reachedFinalAnswer(QATree.Node node) {
        if (node.element.charAt(0) != 'A')
            return false;
        else {
            dialogue.setText(convertToDialogue("Are you thinking of " + node.element.substring(3) + "?"));
            return true;
        }
    }

    /* dialogue formatting */
    private static String convertToDialogue(String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, "!, ");
        int threshold = 20;
        int count = 0;
        String text2 = "";

        /* shape string such that it fits within screen and looks nice */
        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken();
            text2 += line;
            count += line.length();
            String line2 = "";
            if (tokenizer.hasMoreTokens())
                line2 = tokenizer.nextToken();
            if (count + line2.length() > threshold) {
                text2 += "<br>";
                threshold *= 2;
            }
            text2 += line2;
        }
        text2 = "<html><div style='text-align: center;'><center>" + text;
        text2 += "</center></div></html>";
        return text2;
    }

    /* creates buttons with given setting s*/
    private static void JButtonSettings(JButton j, int x, int y, int width, int height, String loc) {
        j.setBounds(x, y, width, height);
        j.setOpaque(false);
        j.setContentAreaFilled(false);
        j.setBorderPainted(false);
        j.setHorizontalTextPosition(j.CENTER);
        j.setVerticalTextPosition(j.CENTER);
        j.setFont(TEXT_FONT);
        try {
            Image img = ImageIO.read(new File(loc));
            j.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* utility function for play mode */
    private static void playMode() {
        play.setEnabled(true);
        play.setVisible(true);
        yes.setEnabled(false);
        yes.setEnabled(false);
        yes.setVisible(false);
        no.setVisible(false);
        textField.setEnabled(false);
        textField.setVisible(false);
    }

    /* utility function for textfield mode */
    private static void textFieldMode() {
        yes.setEnabled(false);
        yes.setEnabled(false);
        yes.setVisible(false);
        no.setVisible(false);
        textField.setEnabled(true);
        textField.setVisible(true);
        textField.setText("");
    }

    /* utility function for yes/no mode */
    private static void yesNoMode() {
        textField.setEnabled(false);
        textField.setVisible(false);
        play.setEnabled(false);
        play.setVisible(false);
        yes.setEnabled(true);
        yes.setEnabled(true);
        yes.setVisible(true);
        no.setVisible(true);
    }

    /* graphics setting s*/
    private static void start() {
        f = new JFrame();
        f.setTitle("Yoda.java");
        f.setResizable(false);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                writeQA("src/QA.txt");
                System.exit(0);
            }
        });
        f.add(new Yoda(), BorderLayout.CENTER);
        f.pack();
        f.setLocation(500, 0);
        f.setVisible(true);
    }

    /* for graphic expressions */
    private void drawYodas() {
        String location = "src/";
        switch (expression) {
            case happy:
                location += "w";
                location += String.valueOf((int) (Math.random() * 3));
                break;
            case angry:
                location += "l";
                location += String.valueOf((int) (Math.random() * 3));
                break;
            default:
                location += "q";
                location += String.valueOf((int) (Math.random() * 3));
                break;
        }
        location += ".png";
        background.setIcon(new ImageIcon(location));
    }

    /* loading questions and answers pre-order based on location */
    private static void loadQA(String location) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(location));
            /* for wins and losses */
            tree.win = Integer.parseInt(reader.readLine());
            tree.loss = Integer.parseInt(reader.readLine());
            loadQA(tree.root, reader);
            tree.colored = tree.root.element;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadQA(QATree.Node n, BufferedReader reader) throws IOException {
        String line = reader.readLine();
        /* if nothing is there, reached end */
        if (line == null)
            return;
            /* if line is an answer, then node is a leaf and therefore go back up */
        else if (line.charAt(0) == 'A') {
            n.element = line;
            return;
        }
        /* line is a question, and add more nodes below */
        n.element = line;
        /* load left then right */
        n.left = new QATree.Node("");
        loadQA(n.left, reader);
        n.right = new QATree.Node("");
        loadQA(n.right, reader);
    }

    /* writing questions and answers pre-order based on location */
    private static void writeQA(String location) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(location));
            /* for wins and losses */
            writer.write(String.valueOf(tree.win));
            writer.newLine();
            writer.write(String.valueOf(tree.loss));
            writer.newLine();
            writeQA(tree.root, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeQA(QATree.Node n, BufferedWriter writer) throws IOException {
        /* if reached a null/empty node */
        if (n == null || n.element.equals("")) return;
        /* write in element */
        writer.write(n.element);
        writer.newLine();
        /* write left then right */
        writeQA(n.left, writer);
        writeQA(n.right, writer);
    }

    /* graphics parameters */
    private final int CANVAS_WIDTH = 500;
    private final int CANVAS_HEIGHT = 696;
    private final double INITIAL_Y = 0.9;
    private final double INITIAL_X = 0.5;

    /* draws tree on canvas */
    public void drawTree() {
        Font TEXT_FONT = new Font("Heiti SC", Font.PLAIN, Math.min(30, (int) (CANVAS_HEIGHT * (INITIAL_Y / (tree.height() - 1)) / 10)));
        StdDraw.clear();
        StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        StdDraw.setFont(TEXT_FONT);
        drawTree(tree.root, INITIAL_X, INITIAL_Y + (1 - INITIAL_Y) / 2, INITIAL_X / 2, INITIAL_Y / (tree.height() - 1), INITIAL_X, INITIAL_Y + (1 - INITIAL_Y) / 2 + 2 * Math.min(INITIAL_Y / (tree.height()) / 8, INITIAL_Y / (tree.height() - 1) / 8), 1);
    }

    private void drawTree(QATree.Node n, double x, double y, double xIncrement, double heightIncrement, double prevX, double prevY, int level) {
        if (n == null)
            return;
        else {
            double length = Math.min(INITIAL_Y / 16, INITIAL_Y / (tree.height()) / 8);
            if (n.element.equals(tree.colored))
                StdDraw.setPenColor(StdDraw.BOOK_RED);
            StdDraw.text(x, y, String.valueOf(n.element));
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.line(x, y + length, prevX, prevY - length);
            drawTree(n.left, x - xIncrement, y - heightIncrement, xIncrement / 2, heightIncrement, x, y, level + 1);
            drawTree(n.right, x + xIncrement, y - heightIncrement, xIncrement / 2, heightIncrement, x, y, level + 1);
        }
    }

    @Override
    public void run() {
    }
}
