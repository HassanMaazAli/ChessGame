import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Point> getLegalMoves(Tile[][] board, int row, int col) {
        List<Point> moves = new ArrayList<>();

        // Up
        for (int r = row - 1; r >= 0; r--) {
            if (!addMove(board, moves, r, col)) break;
        }
        // Down
        for (int r = row + 1; r < 8; r++) {
            if (!addMove(board, moves, r, col)) break;
        }
        // Left
        for (int c = col - 1; c >= 0; c--) {
            if (!addMove(board, moves, row, c)) break;
        }
        // Right
        for (int c = col + 1; c < 8; c++) {
            if (!addMove(board, moves, row, c)) break;
        }

        return moves;
    }

    private boolean addMove(Tile[][] board, List<Point> moves, int r, int c) {
        Piece p = board[r][c].getPiece();
        if (p == null) {
            moves.add(new Point(r, c));
            return true;  // can continue sliding
        } else if (p.isWhite() != this.isWhite) {
            moves.add(new Point(r, c));
            return false; // capture stops sliding
        } else {
            return false; // blocked by own piece
        }
    }

    @Override
    public String getSymbol() {
        return isWhite ? "\u2656" : "\u265C"; // Unicode ♖ and ♜
    }
}
