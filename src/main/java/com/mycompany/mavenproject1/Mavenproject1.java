package com.mycompany.mavenproject1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Mavenproject1 extends JFrame implements ActionListener {

    private static final int ROWS = 4;
    private static final int COLS = 4;
    private static final int NUM_CARDS = ROWS * COLS;

    private JButton[] cards;
    private String[] cardValues;
    private int[] cardIndices;
    private boolean[] cardFlipped;
    private int numFlippedCards;
    private int score;
    private boolean isProcessing;
    private int[] flippedIndices; // Array to hold the flipped card indices
    private Timer hideTimer;

    public Mavenproject1() {
        setTitle("Flip-Flop Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(ROWS, COLS));
        cards = new JButton[NUM_CARDS];
        cardValues = new String[NUM_CARDS];
        cardIndices = new int[NUM_CARDS];
        cardFlipped = new boolean[NUM_CARDS];
        flippedIndices = new int[2]; // Initialize the array

        for (int i = 0; i < NUM_CARDS; i++) {
            cards[i] = new JButton();
            cards[i].addActionListener(this);
            cards[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 5),
                    BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(204, 153, 255))
            ));
            cards[i].setBackground(new Color(230, 204, 255)); // Set light purple background color
            panel.add(cards[i]);
        }

        add(panel);
        setSize(800, 600); // Set the size of the frame
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(153, 102, 255)); // Set the background color to dark purple

        initializeGame();
    }
    private void initializeGame() {
        numFlippedCards = 0;
        score = 0;
        isProcessing = false;

        // Create pairs of cards with unique values
        String[] values = {
                "A", "A", "B", "B", "C", "C", "D", "D",
                "E", "E", "F", "F", "G", "G", "H", "H"
        };

        // Shuffle the card values
        for (int i = 0; i < NUM_CARDS; i++) {
            int j = (int) (Math.random() * NUM_CARDS);
            String temp = values[i];
            values[i] = values[j];
            values[j] = temp;
        }

        // Assign the shuffled values to cards
        for (int i = 0; i < NUM_CARDS; i++) {
            cardValues[i] = values[i];
            cardIndices[i] = i;
            cardFlipped[i] = false;
            cards[i].setText("");
            cards[i].setEnabled(true);
            cards[i].setFont(new Font("Arial", Font.BOLD, 32)); // Set font size to 32
        }
    }

    private void flipCard(int index) {
        if (!cardFlipped[index] && !isProcessing) {
            cardFlipped[index] = true;
            cards[index].setText(cardValues[index]);
            numFlippedCards++;

            if (numFlippedCards == 1) {
                flippedIndices[0] = index; // Store the index of the first flipped card
            } else if (numFlippedCards == 2) {
                flippedIndices[1] = index; // Store the index of the second flipped card

                if (cardValues[flippedIndices[0]].equals(cardValues[flippedIndices[1]])) {
                    cards[flippedIndices[0]].setEnabled(false);
                    cards[flippedIndices[1]].setEnabled(false);
                    score++;
                } else {
                    isProcessing = true;
                    hideTimer = new Timer(1000, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            cardFlipped[flippedIndices[0]] = false;
                            cardFlipped[flippedIndices[1]] = false;
                            cards[flippedIndices[0]].setText("");
                            cards[flippedIndices[1]].setText("");
                            isProcessing = false;
                            hideTimer.stop();
                        }
                    });
                    hideTimer.setRepeats(false);
                    hideTimer.start();
                }

                numFlippedCards = 0;
            }

            if (score == NUM_CARDS / 2) {
                JOptionPane.showMessageDialog(this, "Congratulations! You completed the game with a score of " + score + "!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
                initializeGame();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < NUM_CARDS; i++) {
            if (e.getSource() == cards[i]) {
                flipCard(i);
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Mavenproject1().setVisible(true);
        });
    }
}
