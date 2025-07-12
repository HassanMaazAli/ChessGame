import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String getSymbol() {
        return isWhite ? "\u2654" : "\u265A";
    }

    @Override
    public List<Point> getLegalMoves(Tile[][] tiles, int row, int col) {
        List<Point> moves = new ArrayList<>();

        int SIZE = tiles.length;

        int[][] directions = {
            {-1, 0}, {1, 0}, {0, 1}, {0, -1},
            {-1, 1}, {-1, -1}, {1, 1}, {1, -1}
        };

        for (int[] d : directions) {
            int r = row + d[0];
            int c = col + d[1];
            if (r >= 0 && r < SIZE && c >= 0 && c < SIZE) {
                Tile tile = tiles[r][c];
                if (tile.getPiece() == null || tile.getPiece().isWhite() != this.isWhite) {
                    moves.add(new Point(r, c));
                }
            }
        }

        return moves;
    }
}
