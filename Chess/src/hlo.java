import java.awt.GridLayout;

import javax.swing.JFrame;

public class hlo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Tile Test");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setLayout(new GridLayout(1, 2));
	    
	    Tile whiteTile = new Tile(true, 0, 0, null);
	    Tile blackTile = new Tile(false, 0, 1, null);

	    frame.add(whiteTile);
	    frame.add(blackTile);

	    frame.pack();
	    frame.setSize(200, 100);
	    frame.setVisible(true);

	    // Test highlight
	    whiteTile.showLegalHighlight(true);
	    blackTile.showLegalHighlight(false);

	}

}
