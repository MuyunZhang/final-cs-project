import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GraphicsPanel extends JPanel implements KeyListener, ActionListener, MouseListener{
    private BufferedImage background;
    private boolean[] pressedKeys;

    private Clip sound;

    private Clip music;

    private Blockade n;


    private ArrayList<Blockade> images;
    private int boardLen;
    private int boardWid;

    private double time;

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
        requestFocusInWindow();
        images = new ArrayList<Blockade>();

        snake = new Block(5, 5);
        body = new ArrayList<Block>();

        food = new Block(10, 10);
        r = new Random();
        placeFood();

        moveX = 0;
        moveY = 0;
        time = 0;

        //game timer
        timer = new Timer(100, this);
        timer.start();
        playMusic();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        for(int i = 0; i < boardWid/blockSize + 1; i ++){
            g.drawLine(i * blockSize, 0, i * blockSize, boardLen);
            g.drawLine(0, i * blockSize, boardWid, i * blockSize);
        }

        g.setColor(Color.red);
        g.fill3DRect(food.x * blockSize, food.y * blockSize, blockSize,blockSize, true);

        g.setColor(Color.GREEN);
        g.fill3DRect(snake.x * blockSize, snake.y * blockSize, blockSize,blockSize, true);

        for (int i = 0; i < body.size(); i++) {
            Block snakePart = body.get(i);
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
            playSound();
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

            //collide with snake
            if (collision(snake, snakePart)) {
                gameOver = true;
            }
        }

        if (snake.x*blockSize < 0 || snake.x*blockSize >= boardWid ||
                snake.y*blockSize < 0 || snake.y*blockSize >= boardLen ) {
            gameOver = true;
        }
    }

    public boolean collision(Block b1, Block b2) {
        return b1.x == b2.x && b1.y == b2.y;
    }

    public void placeFood(){
        food.x = r.nextInt(boardWid/blockSize);
        food.y = r.nextInt(boardLen/blockSize);
    }

    public void keyTyped(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            moveX = 0;
            moveY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            moveX = 0;
            moveY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveX = -1;
            moveY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveX = 1 ;
            moveY = 0;
        }
    }

    public void keyReleased(KeyEvent e) {

    }
    public void actionPerformed(ActionEvent e) { //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (e.getSource() instanceof Timer && time > 0) {
            time+= 0.1;
        }
        if (gameOver) {
            timer.stop();
        }
    }
    public void mouseClicked(MouseEvent e) { }  // unimplemented; if you move your mouse while clicking,
    // this method isn't called, so mouseReleased is best

    public void mousePressed(MouseEvent e) { } // unimplemented

    public void mouseReleased(MouseEvent e) {
        // removes blockade on click
        if (e.getButton() == MouseEvent.BUTTON1) {  // left mouse click
            Point mouseClickLocation = e.getPoint();
            for (int i = 0; i < images.size(); i++) {
                Blockade image = images.get(i);
                if (image.imgRect().contains(mouseClickLocation)) {
                    images.remove(image);
                }
            }
        }
    }



    private void playSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/Fruit collect 1 (1).wav").getAbsoluteFile());
            sound = AudioSystem.getClip();
            sound.open(audioInputStream);
            sound.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void playMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/Goblins_Den_(Regular).wav").getAbsoluteFile());
            music = AudioSystem.getClip();
            music.open(audioInputStream);
            music.loop(Clip.LOOP_CONTINUOUSLY);
            music.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void clearLeaves() {
        for (int i = images.size() - 1; i >= 0; i--) {
            images.remove(i);
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
        gameOver = true;
    }
}