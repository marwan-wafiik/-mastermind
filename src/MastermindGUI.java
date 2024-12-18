import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class MastermindGUI {
    private final String[] colors = {"R", "G", "B", "Y", "O", "P"};
    private String[] secretCode;
    private final int codeLength = 4;
    private final int maxAttempts = 10;
    private int attemptCount = 0;

    private JFrame frame;
    private JPanel inputPanel, feedbackPanel, attemptPanel;
    private JTextField[] guessFields;
    private JTextArea feedbackArea;
    private JButton submitButton;

    public MastermindGUI() {
        generateSecretCode();
        createGUI();
    }

    // Generate the secret code
    private void generateSecretCode() {
        Random random = new Random();
        secretCode = new String[codeLength];
        for (int i = 0; i < codeLength; i++) {
            secretCode[i] = colors[random.nextInt(colors.length)];
        }
    }

    // Create the GUI
    private void createGUI() {
        frame = new JFrame("Mastermind Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Input panel for guesses
        inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.setBackground(new Color(220, 240, 255));

        guessFields = new JTextField[codeLength];
        for (int i = 0; i < codeLength; i++) {
            guessFields[i] = new JTextField(2);
            guessFields[i].setFont(new Font("Arial", Font.PLAIN, 16));
            inputPanel.add(guessFields[i]);
        }

        submitButton = new JButton("Submit Guess");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(100, 200, 100));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(new SubmitListener());
        inputPanel.add(submitButton);

        // Feedback panel
        feedbackPanel = new JPanel();
        feedbackPanel.setLayout(new BorderLayout());
        feedbackPanel.setBackground(new Color(255, 230, 220));

        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        feedbackPanel.add(new JScrollPane(feedbackArea), BorderLayout.CENTER);

        // Attempt panel
        attemptPanel = new JPanel();
        attemptPanel.setLayout(new FlowLayout());
        attemptPanel.setBackground(new Color(240, 240, 240));

        JLabel attemptLabel = new JLabel("Attempts Left: " + (maxAttempts - attemptCount));
        attemptLabel.setFont(new Font("Arial", Font.BOLD, 16));
        attemptPanel.add(attemptLabel);

        // Add panels to frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(feedbackPanel, BorderLayout.CENTER);
        frame.add(attemptPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Check the player's guess
    private String checkGuess(String[] guess) {
        int correctPosition = 0;
        int correctColor = 0;

        boolean[] usedInSecret = new boolean[codeLength];
        boolean[] usedInGuess = new boolean[codeLength];

        for (int i = 0; i < codeLength; i++) {
            if (guess[i].equals(secretCode[i])) {
                correctPosition++;
                usedInSecret[i] = true;
                usedInGuess[i] = true;
            }
        }

        for (int i = 0; i < codeLength; i++) {
            if (!usedInGuess[i]) {
                for (int j = 0; j < codeLength; j++) {
                    if (!usedInSecret[j] && guess[i].equals(secretCode[j])) {
                        correctColor++;
                        usedInSecret[j] = true;
                        break;
                    }
                }
            }
        }

        return "Correct Positions: " + correctPosition + ", Correct Colors: " + correctColor;
    }

    // Listener for submit button
    private class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] guess = new String[codeLength];
            for (int i = 0; i < codeLength; i++) {
                guess[i] = guessFields[i].getText().toUpperCase();
            }

            attemptCount++;
            String feedback = checkGuess(guess);
            feedbackArea.append("Attempt " + attemptCount + ": " + String.join(" ", guess) + " -> " + feedback + "\n");

            if (feedback.contains("Correct Positions: " + codeLength)) {
                JOptionPane.showMessageDialog(frame, "Congratulations! You cracked the code!", "Victory", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else if (attemptCount >= maxAttempts) {
                JOptionPane.showMessageDialog(frame, "Game Over! The secret code was: " + String.join(" ", secretCode), "Game Over", JOptionPane.ERROR_MESSAGE);
                frame.dispose();
            }

            for (JTextField field : guessFields) {
                field.setText("");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MastermindGUI::new);
    }
}
