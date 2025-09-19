package modified_TickTack;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

enum Difficulty {
    EASY,
    MEDIUM,
    HARD
}

public class TicTacToeGame extends JFrame implements ActionListener {
    private JButton[][] buttons = new JButton[3][3];
    private boolean playerTurn = true; // true = X, false = O
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JButton restartButton;
    private Difficulty difficulty; // Add this as a class-level field
    private JLabel difficultyLabel;
    private int playerScore = 0, aiScore = 0;
    private Timer turnTimer;
private JLabel timerLabel;
private int timeLeft = 10;

    
    public TicTacToeGame() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(
        this,
        "Choose Difficulty Level:",
        "Tic Tac Toe",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]);

  switch (choice) {
    case 0 -> difficulty = Difficulty.EASY;
    case 1 -> difficulty = Difficulty.MEDIUM;
    case 2 -> difficulty = Difficulty.HARD;
    default -> difficulty = Difficulty.EASY;
}


        setTitle("Tic Tac Toe - Java Swing");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create game board
        JPanel board = new JPanel(new GridLayout(3, 3));
        Font font = new Font("Arial", Font.BOLD, 48);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(font);
                buttons[i][j].addActionListener(this);
                board.add(buttons[i][j]);
            }

        // Status and score labels
        statusLabel = new JLabel("Your Turn (X)", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 20));
         
        timerLabel = new JLabel("Time Left: 10s", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        difficultyLabel = new JLabel("Difficulty: " + difficulty, JLabel.CENTER);
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        scoreLabel = new JLabel("Score - You: 0 | Game: 0", JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        // Restart button
        restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> resetBoard());

        // Bottom panel

        JPanel bottomPanel = new JPanel(new GridLayout(5, 1));
        bottomPanel.add(difficultyLabel);
        bottomPanel.add(statusLabel);
        bottomPanel.add(timerLabel);
        bottomPanel.add(scoreLabel);
        bottomPanel.add(restartButton);

        add(board, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
        startTurnTimer();
    }
     private void startTurnTimer() {
    if (turnTimer != null && turnTimer.isRunning()) {
        turnTimer.stop();
    }

    switch (difficulty) {
    case EASY -> timeLeft = 10;
    case MEDIUM -> timeLeft = 8;
    case HARD -> timeLeft = 5;
}

    timerLabel.setText("Time Left: "+ timeLeft + "s");

    turnTimer = new Timer(1000, e -> {
        timeLeft--;
        timerLabel.setText("Time Left: " + timeLeft + "s");

        if (timeLeft <= 0) {
            turnTimer.stop();
            if (playerTurn) {
                statusLabel.setText("Time's up! Game's Turn");
                playerTurn = false;
                Timer aiDelay = new Timer(500, ev -> {
                    aiMove();
                    ((Timer) ev.getSource()).stop();
                });
                aiDelay.setRepeats(false);
                aiDelay.start();
            }
        }
    });

    turnTimer.start();
}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!playerTurn) return;

        JButton btn = (JButton) e.getSource();
        if (!btn.getText().equals("")) return;

        btn.setText("X");
        playerTurn = false;
        turnTimer.stop();  // Just after player sets text and before AI starts

        if (checkWin("X")) {
            playerScore++;
            statusLabel.setText("You Win!");
            turnTimer.stop();
            updateScore();
            disableAllButtons();
            return;
        }

        if (isFull()) {
            statusLabel.setText("Draw!");
            turnTimer.stop();
            return;
        }

        statusLabel.setText("Game is Thinking...");

        Timer timer = new Timer(400, ev -> {
            aiMove();
            ((Timer) ev.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void aiMove() {
    int row = -1, col = -1;

    switch (difficulty) {
        case EASY:
            do {
                row = (int) (Math.random() * 3);
                col = (int) (Math.random() * 3);
            } while (!buttons[row][col].getText().equals(""));
            break;

        case MEDIUM:
            if (Math.random() < 0.5) { // 50% chance random
                do {
                    row = (int) (Math.random() * 3);
                    col = (int) (Math.random() * 3);
                } while (!buttons[row][col].getText().equals(""));
            } else {
                int[] move = MinimaxAI.findBestMove(buttons);
                row = move[0];
                col = move[1];
            }
            break;

        case HARD:
            int[] move = MinimaxAI.findBestMove(buttons);
            row = move[0];
            col = move[1];
            break;
    }

    if (row != -1 && col != -1) {
        buttons[row][col].setText("O");
    }

    if (checkWin("O")) {
        aiScore++;
        statusLabel.setText("Game Wins!");
        turnTimer.stop();
        updateScore();
        disableAllButtons();
    } else if (isFull()) {
        statusLabel.setText("Draw!");
        turnTimer.stop();
    } else {
        playerTurn = true;
        statusLabel.setText("Your Turn (X)");
    }
    if (playerTurn) {
    startTurnTimer();
}

}


    private boolean isFull() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                if (btn.getText().equals("")) return false;
        return true;
    }

    private boolean checkWin(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (symbol.equals(buttons[i][0].getText()) &&
                symbol.equals(buttons[i][1].getText()) &&
                symbol.equals(buttons[i][2].getText())) return true;

            if (symbol.equals(buttons[0][i].getText()) &&
                symbol.equals(buttons[1][i].getText()) &&
                symbol.equals(buttons[2][i].getText())) return true;
        }

        if (symbol.equals(buttons[0][0].getText()) &&
            symbol.equals(buttons[1][1].getText()) &&
            symbol.equals(buttons[2][2].getText())) return true;

        if (symbol.equals(buttons[0][2].getText()) &&
            symbol.equals(buttons[1][1].getText()) &&
            symbol.equals(buttons[2][0].getText())) return true;

        return false;
    }

    private void disableAllButtons() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                btn.setEnabled(false);
    }

    private void resetBoard() {
        for (JButton[] row : buttons)
            for (JButton btn : row) {
                btn.setText("");
                btn.setEnabled(true);
            }

        playerTurn = true;
        statusLabel.setText("Your Turn (X)");
        startTurnTimer();
    }

    private void updateScore() {
        scoreLabel.setText("Score - You: " + playerScore + " | Game: " + aiScore);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeGame::new);
    }
}