package modified_TickTack;
import javax.swing.JButton;

public class MinimaxAI {
    public static int[] findBestMove(JButton[][] board) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = {-1, -1};

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j].getText().equals("")) {
                    board[i][j].setText("O");
                    int score = minimax(board, 0, false);
                    board[i][j].setText("");
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }

        return bestMove;
    }


    private static int minimax(JButton[][] board, int depth, boolean isMax) {
        if (checkWin(board, "O")) return 10 - depth;
        if (checkWin(board, "X")) return depth - 10;
        if (isDraw(board)) return 0;

        if (isMax) {
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (board[i][j].getText().equals("")) {
                        board[i][j].setText("O");
                        maxEval = Math.max(maxEval, minimax(board, depth + 1, false));
                        board[i][j].setText("");
                    }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (board[i][j].getText().equals("")) {
                        board[i][j].setText("X");
                        minEval = Math.min(minEval, minimax(board, depth + 1, true));
                        board[i][j].setText("");
                    }
            return minEval;
        }
    }

    private static boolean checkWin(JButton[][] board, String symbol) {
        for (int i = 0; i < 3; i++) {
            if (symbol.equals(board[i][0].getText()) &&
                symbol.equals(board[i][1].getText()) &&
                symbol.equals(board[i][2].getText())) return true;

            if (symbol.equals(board[0][i].getText()) &&
                symbol.equals(board[1][i].getText()) &&
                symbol.equals(board[2][i].getText())) return true;
        }

        if (symbol.equals(board[0][0].getText()) &&
            symbol.equals(board[1][1].getText()) &&
            symbol.equals(board[2][2].getText())) return true;

        if (symbol.equals(board[0][2].getText()) &&
            symbol.equals(board[1][1].getText()) &&
            symbol.equals(board[2][0].getText())) return true;

        return false;
    }

    private static boolean isDraw(JButton[][] board) {
        for (JButton[] row : board)
            for (JButton btn : row)
                if (btn.getText().equals("")) return false;
        return true;
    }
}