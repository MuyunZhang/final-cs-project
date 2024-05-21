import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel {
    private BufferedImage background;
    private boolean[] pressedKeys;
    private int boardLen;
    private int boardWid;
    private Timer timer;
    private int time;
    private JButton pause;

    private JButton reset;

    private boolean paused = false;
    int blockSize = 25;
    private boolean set = false;

    Block snake;

    private class Block{
        int x;
        int y;

        Block(int x, int y){
            this.x = x;
            this.y =y;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }

    }

    public GraphicsPanel(int len, int wid) {
        boardLen = len;
        boardWid = wid;
        setBackground(Color.black);
        setPreferredSize(new Dimension(boardLen, boardWid));
        snake = new Block(5, 5);

    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);  // just do this
        draw(g);
    }
    public void draw(Graphics g){
       g.setColor(Color.GREEN);
       g.fillRect(snake.x * blockSize, snake.y * blockSize, blockSize,blockSize);

    }
}