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
    private int t;

    int blockSize = 25;
    private Block food;
    ArrayList<Block> body;
    Block snake;
    boolean gameOver = false;

    private Timer timer1;
    private Timer timer2;

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

        public Rectangle imgRect() {
            Rectangle rectangle = new Rectangle(x, y,x + 25, y + 25);
            return rectangle;
        }
    }

    public GraphicsPanel(int len, int wid) {
        this.boardWid = wid;
        this.boardLen = len;
        setPreferredSize(new Dimension(this.boardWid, this.boardLen));
        setBackground(Color.black);
        addKeyListener(this);
        addMouseListener(this);
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
        t = 0;

        //game timer
        timer1 = new Timer(100, this);
        timer2 = new Timer(500, this);
        timer1.start();
        timer2.start();
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

        for (int i = 0; i < images.size(); i++) {
            Blockade image = images.get(i);
            g.drawImage(image.getImage(), image.getxCord(), image.getyCord(), null);
        }
        g.setColor(Color.yellow);
        Point mouseP = getMousePosition();
        if (mouseP != null) {
            g.fillRect(mouseP.x - 10, mouseP.y - 10, 20, 20);
            Rectangle rectangle = new Rectangle(mouseP.x - 10, mouseP.y - 10, 10, 10);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(body.size()), blockSize - 16, blockSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(body.size()), blockSize - 16, blockSize);
            g.drawString("Time: " + time, blockSize - 16, 75 );
            g.drawString("Timer delay: " + timer2.getDelay(), blockSize - 16, 50);
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

        //game over condition
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
        if (e.getSource() == timer1) {
            move();
            repaint();
        }
        if (e.getSource() instanceof Timer && time >= 0) {
            t ++;
            if(t % 10 == 0) {
                time += 1;
            }
        }
        if (e.getSource() == timer2) {
            int newDelay = 500 - (int) (body.size() * 7.5);
            if (newDelay > 0) {
                timer2.setDelay(newDelay);
            }
            double random = Math.random();
            if (random > 0.65 && time > 10) {
                int randomX = (int) (Math.random() * 600);
                int randomY = (int) (Math.random() * 560);
                double random2 = Math.random();
                if (random2 > 0.8) {
                    Blockade newImage = new Blockade(randomX, randomY, "src/img.jpg");
                    images.add(newImage);
                }
            }
        }
        if (gameOver) {
            timer1.stop();
            timer2.stop();
        }
    }
    public void mouseClicked(MouseEvent e) {
        Point mouseClickLocation = e.getPoint();

        // Check if the mouse click occurred on the snake's head
        if (isCollision(snake, mouseClickLocation)) {
            gameOver = true;
        } else {
            // Check if the mouse click occurred on any segment of the snake's body
            for (int i = 0; i < body.size(); i++) {
                Block snakeBody = body.get(i);
                if (isCollision(snakeBody, mouseClickLocation)) {
                    gameOver = true;
                    break; // No need to continue checking once we find a collision
                }
            }
        }
    }

    // Helper method to check for collision between a block and a point
    private boolean isCollision(Block block, Point point) {
        int blockSize = 25; // Assuming blockSize is defined somewhere in your class
        Rectangle blockRect = new Rectangle(block.x * blockSize, block.y * blockSize, blockSize, blockSize);
        return blockRect.contains(point);
    }



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
    }
}