import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Basictictactoe {

    static JFrame frame;
    static CardLayout cardLayout;
    static JPanel mainPanel;

    static String player1, player2;
    static String player1Symbol, player2Symbol;
    static boolean player1Turn = true;

    static int score1 = 0;
    static int score2 = 0;

    static JButton[] buttons = new JButton[9];
    static JLabel statusLabel;
    static JLabel scoreLabel;
    static JLabel resultLabel;

    static Color blueBg = new Color(18, 32, 72);
    static Color neonYellow = new Color(255, 235, 59);

    public static void main(String[] args) {

        frame = new JFrame("Tic Tac Toe");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(homeScreen(), "Home");
        mainPanel.add(gameScreen(), "Game");
        mainPanel.add(resultScreen(), "Result");

        frame.add(mainPanel);
        frame.setSize(550, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // HOME SCREEN
    static JPanel homeScreen() {

        JPanel panel = new JPanel();
        panel.setBackground(blueBg);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("TIC TAC TOE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 42));
        title.setForeground(neonYellow);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField p1 = styledTextField();
        JTextField p2 = styledTextField();

        JComboBox<String> symbolBox = new JComboBox<>(new String[]{"X", "O"});
        symbolBox.setMaximumSize(new Dimension(200, 40));
        symbolBox.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton start = styledButton("Start Game");

        start.addActionListener(e -> {

            player1 = p1.getText().trim();
            player2 = p2.getText().trim();

            if (player1.isEmpty() || player2.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter both player names.");
                return;
            }

            if (symbolBox.getSelectedItem().equals("X")) {
                player1Symbol = "X";
                player2Symbol = "O";
            } else {
                player1Symbol = "O";
                player2Symbol = "X";
            }

            player1Turn = true;
            statusLabel.setText(player1 + "'s Turn");
            scoreLabel.setText(getScoreText());
            cardLayout.show(mainPanel, "Game");
        });

        panel.add(Box.createVerticalStrut(60));
        panel.add(title);
        panel.add(Box.createVerticalStrut(50));
        panel.add(createLabel("Enter Player 1 Name"));
        panel.add(p1);
        panel.add(Box.createVerticalStrut(20));
        panel.add(createLabel("Enter Player 2 Name"));
        panel.add(p2);
        panel.add(Box.createVerticalStrut(20));
        panel.add(createLabel("Choose Symbol"));
        panel.add(symbolBox);
        panel.add(Box.createVerticalStrut(40));
        panel.add(start);

        return panel;
    }

    // GAME SCREEN
    static JPanel gameScreen() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(blueBg);

        statusLabel = new JLabel("Game Start", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 30)); // Increased size
        statusLabel.setForeground(Color.WHITE);

        scoreLabel = new JLabel("", JLabel.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        scoreLabel.setForeground(neonYellow);

        JPanel topPanel = new JPanel(new GridLayout(2,1));
        topPanel.setBackground(blueBg);
        topPanel.add(statusLabel);
        topPanel.add(scoreLabel);

        JPanel grid = new JPanel(new GridLayout(3,3,10,10));
        grid.setBackground(blueBg);
        grid.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial Black", Font.BOLD, 85));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setPreferredSize(new Dimension(150,150));

            buttons[i].addActionListener(e -> handleMove((JButton)e.getSource()));
            grid.add(buttons[i]);
        }

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(grid, BorderLayout.CENTER);

        return panel;
    }

    static void handleMove(JButton btn) {

        if (!btn.getText().equals("")) return;

        if (player1Turn) {
            btn.setText(player1Symbol);
            btn.setForeground(player1Symbol.equals("X") ? Color.BLUE : Color.RED);
        } else {
            btn.setText(player2Symbol);
            btn.setForeground(player2Symbol.equals("X") ? Color.BLUE : Color.RED);
        }

        if (checkWinner()) {
            Toolkit.getDefaultToolkit().beep(); // WIN SOUND
            if (player1Turn) score1++;
            else score2++;
            showResult((player1Turn ? player1 : player2) + " has won this round");
            return;
        }

        if (isDraw()) {
            showResult("Match Draw");
            return;
        }

        player1Turn = !player1Turn;
        statusLabel.setText((player1Turn ? player1 : player2) + "'s Turn");
    }

    static boolean checkWinner() {

        int[][] win = {
                {0,1,2},{3,4,5},{6,7,8},
                {0,3,6},{1,4,7},{2,5,8},
                {0,4,8},{2,4,6}
        };

        for (int[] w : win) {
            if (!buttons[w[0]].getText().equals("") &&
                buttons[w[0]].getText().equals(buttons[w[1]].getText()) &&
                buttons[w[1]].getText().equals(buttons[w[2]].getText())) {

                for(int i : w)
                    buttons[i].setBackground(new Color(144, 238, 144));

                return true;
            }
        }
        return false;
    }

    static boolean isDraw() {
        for (JButton b : buttons)
            if (b.getText().equals("")) return false;
        return true;
    }

    // RESULT SCREEN
    static JPanel resultScreen() {

        JPanel panel = new JPanel();
        panel.setBackground(blueBg);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        resultLabel = new JLabel("", JLabel.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        resultLabel.setForeground(neonYellow);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel resultScore = new JLabel("", JLabel.CENTER);
        resultScore.setFont(new Font("Segoe UI", Font.BOLD, 20));
        resultScore.setForeground(Color.WHITE);
        resultScore.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton replay = styledButton("Replay");
        JButton close = styledButton("Close");

        replay.addActionListener(e -> {
            resetBoard();
            scoreLabel.setText(getScoreText());
            cardLayout.show(mainPanel, "Game");
        });

        close.addActionListener(e -> System.exit(0));

        panel.add(Box.createVerticalStrut(150));
        panel.add(resultLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(resultScore);
        panel.add(Box.createVerticalStrut(40));
        panel.add(replay);
        panel.add(Box.createVerticalStrut(20));
        panel.add(close);

        panel.putClientProperty("scoreRef", resultScore);

        return panel;
    }

    static void showResult(String msg) {

        JPanel resultPanel = (JPanel) mainPanel.getComponent(2);
        JLabel resultScore = (JLabel) resultPanel.getClientProperty("scoreRef");

        resultLabel.setText(msg);
        resultScore.setText(getScoreText());

        cardLayout.show(mainPanel, "Result");
    }

    static void resetBoard() {
        for (JButton b : buttons) {
            b.setText("");
            b.setBackground(Color.WHITE);
        }
        player1Turn = true;
        statusLabel.setText(player1 + "'s Turn");
    }

    static String getScoreText() {
        return player1 + " : " + score1 + "    |    " + player2 + " : " + score2;
    }

    static JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setMaximumSize(new Dimension(220, 50));
        return btn;
    }

    static JTextField styledTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(320, 45));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        field.setHorizontalAlignment(JTextField.CENTER);
        return field;
    }

    static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Now bold
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
