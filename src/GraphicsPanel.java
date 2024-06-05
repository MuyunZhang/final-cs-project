import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class GraphicsPanel extends JPanel implements KeyListener, ActionListener{
    private BufferedImage background;
    private boolean[] pressedKeys;
    private int boardLen;
    private int boardWid;
    int blockSize = 25;
    private Block food;
    ArrayList<Block> body;
    Block snake;
    boolean gameOver = false;

    private Timer timer;

    private int moveX;
    private int moveY;

    Random r;

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
        this.boardWid = wid;
        this.boardLen = len;
        setPreferredSize(new Dimension(this.boardWid, this.boardLen));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snake = new Block(5, 5);
        body = new ArrayList<Block>();

        food = new Block(10, 10);
        r = new Random();
        placeFood();

        moveX = 1;
        moveY = 0;

        //game timer
        timer = new Timer(100, this); //how long it takes to start timer, milliseconds gone between frames
        timer.start();
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

        for (int i = 0; i < body.size(); i++) {
            Block snakePart = body.get(i);
            // g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x*blockSize, snakePart.y*blockSize, blockSize, blockSize, true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(body.size()), blockSize - 16, blockSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(body.size()), blockSize - 16, blockSize);
        }
    }



    public void move() {
        //eat food
        if (collision(snake, food)) {
            body.add(new Block(food.x, food.y));
            placeFood();
        }

        //move snake body
        for (int i = body.size()-1; i >= 0; i--) {
            Block snakePart = body.get(i);
            if (i == 0) { //right before the head
                snakePart.x = snake.x;
                snakePart.y = snake.y;
            }
            else {
                Block prevSnakePart = body.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        //move snake head
        snake.x += moveX;
        snake.y += moveY;

        //game over conditions
        for (int i = 0; i < body.size(); i++) {
            Block snakePart = body.get(i);

            //collide with snake head
            if (collision(snake, snakePart)) {
                gameOver = true;
            }
        }

        if (snake.x*blockSize < 0 || snake.x*blockSize > boardWid || //passed left border or right border
                snake.y*blockSize < 0 || snake.y*blockSize > boardLen ) { //passed top border or bottom border
            gameOver = true;
        }
    }

    public boolean collision(Block tile1, Block tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void placeFood(){
        food.x = r.nextInt(boardWid/blockSize);
        food.y = r.nextInt(boardLen/blockSize);
    }

    public void keyTyped(KeyEvent e) { } // unimplemented

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && moveY != 1) {
            moveX = 0;
            moveY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && moveY != -1) {
            moveX = 0;
            moveY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && moveX != 1) {
            moveX = -1;
            moveY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && moveX != -1) {
            moveX = 1;
            moveY = 0;
        }
    }

    public void keyReleased(KeyEvent e) {

    }
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameOver) {
            timer.stop();
        }
    }
}