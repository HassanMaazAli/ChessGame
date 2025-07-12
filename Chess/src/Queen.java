import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String getSymbol() {
        // Unicode symbols for white queen ♕ and black queen ♛
        return isWhite ? "\u2655" : "\u265B";
    }

    @Override
    public List<Point> getLegalMoves(Tile[][] tiles, int row, int col) {
        List<Point> moves = new ArrayList<>();

        int SIZE = tiles.length;

        // Queen moves like Rook + Bishop

        // Directions: N, S, E, W, NE, NW, SE, SW
        int[][] directions = {
            {-1, 0}, {1, 0}, {0, 1}, {0, -1},
            {-1, 1}, {-1, -1}, {1, 1}, {1, -1}
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
                        moves.add(new Point(r, c)); // Can capture enemy piece
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
