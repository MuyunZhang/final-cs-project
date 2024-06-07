import javax.swing.*;

public class MainFrame {

    private GraphicsPanel panel;

    public MainFrame() {
        int boardLen = 600;
        int boardWid = 600;

        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardWid, boardLen); // 540 height of image + 40 for window frame thingy
        frame.setLocationRelativeTo(null);


        // create and add panel
        panel = new GraphicsPanel(boardLen, boardWid);
        frame.add(panel);

        frame.setVisible(true);
    }
}