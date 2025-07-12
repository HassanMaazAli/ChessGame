import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String getSymbol() {
        return isWhite ? "\u2657" : "\u265D";
    }

    @Override
    public List<Point> getLegalMoves(Tile[][] tiles, int row, int col) {
        List<Point> moves = new ArrayList<>();

        int SIZE = tiles.length;

        // Directions: diagonal only
        int[][] directions = {
            {-1, 1},  // up-right
            {-1, -1}, // up-left
            {1, 1},   // down-right
            {1, -1}   // down-left
        };

        for (int[] d : directions) {
            int r = row + d[0];
            int c = col + d[1];
            while (r >= 0 && r < SIZE && c >= 0 && c < SIZE) {
                Tile tile = tiles[r][c];
                if (tile.getPiece() == null) {
                    moves.add(new Point(r, c));
                } else {
                    if (tile.getPiece().isWhite() != this.isWhite) {
                        moves.add(new Point(r, c)); // capture enemy piece
                    }
                    break; // blocked by any piece
                }
                r += d[0];
                c += d[1];
            }
        }
        return moves;
    }
}
