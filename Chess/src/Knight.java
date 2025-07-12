import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String getSymbol() {
        return isWhite ? "\u2658" : "\u265E";
    }

    @Override
    public List<Point> getLegalMoves(Tile[][] board, int row, int col) {
        List<Point> moves = new ArrayList<>();
        int[][] jumps = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] j : jumps) {
            int r = row + j[0];
            int c = col + j[1];
            if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Piece target = board[r][c].getPiece();
                if (target == null || target.isWhite() != this.isWhite()) {
                    moves.add(new Point(r, c));
                }
            }
        }

        return moves;
    }
}
