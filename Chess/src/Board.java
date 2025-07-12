import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

public class Board extends JPanel {
    private final int SIZE = 8;
    private Tile[][] tiles = new Tile[SIZE][SIZE];
    private Tile selectedTile = null;
    private boolean whiteTurn = true; // true = white's turn, false = black's turn
    private JLabel statusLabel;
    private JPanel boardPanel;
    private boolean flip = false; // Used to flip the board after each move

    // Castling flags
    private boolean whiteKingMoved = false;
    private boolean blackKingMoved = false;
    private boolean whiteQueenRookMoved = false;
    private boolean blackQueenRookMoved = false;
    private boolean whiteKingRookMoved = false;
    private boolean blackKingRookMoved = false;

    public Board() {
        setLayout(new BorderLayout());

        boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        add(boardPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("White to move");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        initializeBoard(boardPanel);
        addPieces();
        renderBoard();
    }

    private void initializeBoard(JPanel boardPanel) {
        boolean white = false;
        for (int row = 0; row < SIZE; row++) {
            white = !white;
            for (int col = 0; col < SIZE; col++) {
                Tile tile = new Tile(white, row, col, this);
                tiles[row][col] = tile;
                white = !white;
            }
        }
    }

    private void addPieces() {
        tiles[0][0].setPiece(new Rook(false));
        tiles[0][1].setPiece(new Knight(false));
        tiles[0][2].setPiece(new Bishop(false));
        tiles[0][3].setPiece(new Queen(false));
        tiles[0][4].setPiece(new King(false));
        tiles[0][5].setPiece(new Bishop(false));
        tiles[0][6].setPiece(new Knight(false));
        tiles[0][7].setPiece(new Rook(false));
        for (int col = 0; col < SIZE; col++) {
            tiles[1][col].setPiece(new Pawn(false));
        }

        tiles[7][0].setPiece(new Rook(true));
        tiles[7][1].setPiece(new Knight(true));
        tiles[7][2].setPiece(new Bishop(true));
        tiles[7][3].setPiece(new Queen(true));
        tiles[7][4].setPiece(new King(true));
        tiles[7][5].setPiece(new Bishop(true));
        tiles[7][6].setPiece(new Knight(true));
        tiles[7][7].setPiece(new Rook(true));
        for (int col = 0; col < SIZE; col++) {
            tiles[6][col].setPiece(new Pawn(true));
        }
    }

    public void handleClick(Tile tile) {
        if (selectedTile == null) {
            if (tile.getPiece() != null && tile.getPiece().isWhite() == whiteTurn) {
                selectedTile = tile;
                tile.highlight(true);
                List<Point> moves = tile.getPiece().getLegalMoves(tiles, tile.getRow(), tile.getCol());
                List<Point> legalMoves = filterLegalMovesAvoidingCheck(selectedTile.getPiece(), tile.getRow(), tile.getCol(), moves);
                highlightMoves(legalMoves);
            }
        } else {
            if (tile != selectedTile) {
                List<Point> originalMoves = selectedTile.getPiece().getLegalMoves(tiles, selectedTile.getRow(), selectedTile.getCol());
                List<Point> legalMoves = filterLegalMovesAvoidingCheck(selectedTile.getPiece(), selectedTile.getRow(), selectedTile.getCol(), originalMoves);
                Point clicked = new Point(tile.getRow(), tile.getCol());
                if (legalMoves.contains(clicked)) {
                    Piece movingPiece = selectedTile.getPiece();
                    int fromRow = selectedTile.getRow();
                    int fromCol = selectedTile.getCol();
                    int toRow = clicked.x;
                    int toCol = clicked.y;

                    // Castling logic
                    if (movingPiece instanceof King && Math.abs(toCol - fromCol) == 2 && !isInCheck(movingPiece.isWhite())) {
                        if (toCol == 6) { // Kingside
                            if (movingPiece.isWhite() && !whiteKingMoved && !whiteKingRookMoved &&
                                tiles[7][5].getPiece() == null &&
                                tiles[7][6].getPiece() == null &&
                                !isInCheckPath(7, 4, 6, true)) {
                                tiles[7][5].setPiece(tiles[7][7].getPiece());
                                tiles[7][7].setPiece(null);
                            } else if (!movingPiece.isWhite() && !blackKingMoved && !blackKingRookMoved &&
                                       tiles[0][5].getPiece() == null &&
                                       tiles[0][6].getPiece() == null &&
                                       !isInCheckPath(0, 4, 6, false)) {
                                tiles[0][5].setPiece(tiles[0][7].getPiece());
                                tiles[0][7].setPiece(null);
                            }
                        } else if (toCol == 2) { // Queenside
                            if (movingPiece.isWhite() && !whiteKingMoved && !whiteQueenRookMoved &&
                                tiles[7][1].getPiece() == null &&
                                tiles[7][2].getPiece() == null &&
                                tiles[7][3].getPiece() == null &&
                                !isInCheckPath(7, 4, 2, true)) {
                                tiles[7][3].setPiece(tiles[7][0].getPiece());
                                tiles[7][0].setPiece(null);
                            } else if (!movingPiece.isWhite() && !blackKingMoved && !blackQueenRookMoved &&
                                       tiles[0][1].getPiece() == null &&
                                       tiles[0][2].getPiece() == null &&
                                       tiles[0][3].getPiece() == null &&
                                       !isInCheckPath(0, 4, 2, false)) {
                                tiles[0][3].setPiece(tiles[0][0].getPiece());
                                tiles[0][0].setPiece(null);
                            }
                        }
                    }

                    tile.setPiece(movingPiece);
                    selectedTile.setPiece(null);

                    // Update castling flags
                    if (movingPiece instanceof King) {
                        if (movingPiece.isWhite()) whiteKingMoved = true;
                        else blackKingMoved = true;
                    } else if (movingPiece instanceof Rook) {
                        if (movingPiece.isWhite()) {
                            if (fromRow == 7 && fromCol == 0) whiteQueenRookMoved = true;
                            else if (fromRow == 7 && fromCol == 7) whiteKingRookMoved = true;
                        } else {
                            if (fromRow == 0 && fromCol == 0) blackQueenRookMoved = true;
                            else if (fromRow == 0 && fromCol == 7) blackKingRookMoved = true;
                        }
                    }

                    if (isInCheck(!whiteTurn)) {
                        if (isCheckmate(!whiteTurn)) {
                            JOptionPane.showMessageDialog(this, (whiteTurn ? "Black" : "White") + " is checkmated. Game over!");
                        } else {
                            JOptionPane.showMessageDialog(this, (whiteTurn ? "Black" : "White") + " is in check!");
                        }
                    }

                    whiteTurn = !whiteTurn;
                    flip = !whiteTurn; // Flip only when it's black's turn
                    statusLabel.setText((whiteTurn ? "White (bottom)" : "Black (bottom)") + " to move");
                }
            }
            selectedTile.highlight(false);
            clearHighlights();
            selectedTile = null;
            renderBoard();
        }
    }

    private boolean isInCheckPath(int row, int fromCol, int toCol, boolean isWhite) {
        int step = (toCol > fromCol) ? 1 : -1;
        for (int col = fromCol; col != toCol + step; col += step) {
            Piece original = tiles[row][col].getPiece();
            tiles[row][col].setPiece(new King(isWhite));
            boolean check = isInCheck(isWhite);
            tiles[row][col].setPiece(original);
            if (check) return true;
        }
        return false;
    }

    private List<Point> filterLegalMovesAvoidingCheck(Piece piece, int fromRow, int fromCol, List<Point> moves) {
        List<Point> safeMoves = new ArrayList<>();
        for (Point move : moves) {
            Piece original = tiles[move.x][move.y].getPiece();
            tiles[move.x][move.y].setPiece(piece);
            tiles[fromRow][fromCol].setPiece(null);

            boolean causesCheck = isInCheck(piece.isWhite());

            tiles[fromRow][fromCol].setPiece(piece);
            tiles[move.x][move.y].setPiece(original);

            if (!causesCheck) safeMoves.add(move);
        }
        return safeMoves;
    }

    private void highlightMoves(List<Point> moves) {
        for (Point p : moves) {
            tiles[p.x][p.y].showLegalHighlight(true);
        }
    }

    private void clearHighlights() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                tiles[r][c].showLegalHighlight(false);
            }
        }
    }

    private void renderBoard() {
        boardPanel.removeAll();
        if (flip) {
            for (int row = SIZE - 1; row >= 0; row--) {
                for (int col = SIZE - 1; col >= 0; col--) {
                    boardPanel.add(tiles[row][col]);
                }
            }
        } else {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    boardPanel.add(tiles[row][col]);
                }
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private boolean isInCheck(boolean isWhite) {
        Point kingPos = findKing(isWhite);
        if (kingPos == null) return false;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = tiles[r][c].getPiece();
                if (p != null && p.isWhite() != isWhite) {
                    List<Point> moves = p.getLegalMoves(tiles, r, c);
                    if (moves.contains(kingPos)) return true;
                }
            }
        }
        return false;
    }

    private boolean isCheckmate(boolean isWhite) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = tiles[r][c].getPiece();
                if (p != null && p.isWhite() == isWhite) {
                    List<Point> moves = p.getLegalMoves(tiles, r, c);
                    for (Point move : moves) {
                        Piece original = tiles[move.x][move.y].getPiece();
                        tiles[move.x][move.y].setPiece(p);
                        tiles[r][c].setPiece(null);

                        boolean stillInCheck = isInCheck(isWhite);

                        tiles[r][c].setPiece(p);
                        tiles[move.x][move.y].setPiece(original);

                        if (!stillInCheck) return false;
                    }
                }
            }
        }
        return true;
    }

    private Point findKing(boolean isWhite) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Piece p = tiles[r][c].getPiece();
                if (p instanceof King && p.isWhite() == isWhite) {
                    return new Point(r, c);
                }
            }
        }
        return null;
    }
}
