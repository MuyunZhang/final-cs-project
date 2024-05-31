import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements KeyListener, ActionListener{
    private BufferedImage background;
    private boolean[] pressedKeys;
    private int boardLen;
    private int boardWid;
    int blockSize = 25;
    private Block food;
    ArrayList<Block> body;
    Block snake;

    private Timer timer;

    private int speedX;
    private int speedY;

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

        public void setX(int n){
            x = n;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public GraphicsPanel(int len, int wid) {
        boardLen = len;
        boardWid = wid;
        setBackground(Color.black);
        setPreferredSize(new Dimension(boardLen, boardWid));
        snake = new Block(5, 5);
        food = new Block(10, 10);
        pressedKeys = new boolean [128];
        speedX = 1;
        speedY = 0;
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
        g.setColor(Color.red);
        g.fill3DRect(food.x * blockSize, food.y * blockSize, blockSize,blockSize, true);

        g.setColor(Color.GREEN);
        g.fill3DRect(snake.x * blockSize, snake.y * blockSize, blockSize,blockSize, true);
    }

    public void move(){





    }

    public void keyTyped(KeyEvent e) { } // unimplemented

    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        if(key == 65){

        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }
}