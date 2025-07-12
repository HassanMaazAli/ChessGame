import javax.swing.*;

public class ChessGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Java Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        frame.add(new Board());
        frame.setVisible(true);
    }
}
