import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Point> getLegalMoves(Tile[][] board, int row, int col) {
        List<Point> moves = new ArrayList<>();
        int direction = isWhite ? -1 : 1; // White moves up, black moves down

        // One step forward
        int nextRow = row + direction;
        if (nextRow >= 0 && nextRow < 8 && board[nextRow][col].getPiece() == null) {
            moves.add(new Point(nextRow, col));

            // Two steps forward if first move
            if ((isWhite && row == 6) || (!isWhite && row == 1)) {
                int twoStepRow = row + 2 * direction;
                if (board[twoStepRow][col].getPiece() == null) {
                    moves.add(new Point(twoStepRow, col));
                }
            }
        }

        // Captures diagonally
        if (col - 1 >= 0 && nextRow >= 0 && nextRow < 8) {
            Piece p = board[nextRow][col - 1].getPiece();
            if (p != null && p.isWhite() != isWhite) {
                moves.add(new Point(nextRow, col - 1));
            }
        }
        if (col + 1 < 8 && nextRow >= 0 && nextRow < 8) {
            Piece p = board[nextRow][col + 1].getPiece();
            if (p != null && p.isWhite() != isWhite) {
                moves.add(new Point(nextRow, col + 1));
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "\u2659" : "\u265F"; // Unicode ♙ and ♟
    }
}
