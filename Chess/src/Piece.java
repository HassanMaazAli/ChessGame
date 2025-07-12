import java.awt.Point;
import java.util.List;

public abstract class Piece {
    protected boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    // Returns list of legal moves for this piece from (row, col)
    public abstract List<Point> getLegalMoves(Tile[][] board, int row, int col);

    // Returns a string symbol for displaying on Tile
    public abstract String getSymbol();
}
