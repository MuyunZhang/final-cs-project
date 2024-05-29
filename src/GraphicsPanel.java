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
    int blockSize = 25;

    private Block food;

    ArrayList<Block> body;



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
        for(int i = 0; i < boardWid/blockSize; i ++){
            g.drawLine(i * blockSize, 0, i * blockSize, boardLen);
            g.drawLine(0, i * blockSize, boardWid, i * blockSize);
        }

       g.setColor(Color.GREEN);
       g.fillRect(snake.x * blockSize, snake.y * blockSize, blockSize,blockSize);

    }
}