import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Tile extends JPanel {
    private boolean isWhite;
    private int row, col;
    private Board board;
    private Piece piece;

    private static final Color LIGHT = new Color(240, 217, 181);
    private static final Color DARK = new Color(181, 136, 99);

    public Tile(boolean isWhite, int row, int col, Board board) {
        this.isWhite = isWhite;
        this.row = row;
        this.col = col;
        this.board = board;
        this.piece = null;
        setBackground(isWhite ? LIGHT : DARK);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (board != null) {
                    board.handleClick(Tile.this);
                }
            }
        });
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        repaint();
    }

    public Piece getPiece() {
        return piece;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void highlight(boolean on) {
        setBorder(on ? BorderFactory.createLineBorder(Color.RED, 3) : null);
    }

    public void showLegalHighlight(boolean on) {
        setBackground(on ? Color.GREEN : (isWhite ? LIGHT : DARK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (piece != null) {
            g.setFont(new Font("SansSerif", Font.BOLD, 36));
            FontMetrics fm = g.getFontMetrics();
            String symbol = piece.getSymbol();
            int textWidth = fm.stringWidth(symbol);
            int textHeight = fm.getAscent();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() + textHeight) / 2 - 4;
            g.drawString(symbol, x, y);
        }
    }
}
