import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;

//Flappy Lebron inherits Jpanel class
public class FlappyLebron extends JPanel implements ActionListener, KeyListener {
    private static final boolean FlappyLebron = false;
    int boardWidth = 360;
    int boardHeight = 640;
//variable for the images
Image backgroundImg;
Image lebronImg;
Image topPipeImg;
Image bottomPipeImg;

//Lebron James
int lebronX = boardWidth/8;
int lebronY = boardHeight/2;
int lebronWidth = 60;
int lebronHeight= 55;

//Lebron class
class Lebron {
    int x = lebronX;
    int y = lebronY;
    int width = lebronWidth;
    int height = lebronHeight;
    Image img;

    Lebron(Image img) {
        this.img = img;
    }
}
//da pipes
int pipeX = boardWidth;
int pipeY = 0;
//pipes are 6 times smaller than the background
int pipeWidth = 64;
int pipeHeight = 512;

//pipe class
class Pipe {
    int x = pipeX;
    int y = pipeY;
    int width = pipeWidth;
    int height = pipeHeight;
    Image img;
    boolean passed = false;

    Pipe(Image img) {
        this.img = img;
    }
}

//game logic such as gravity and velocity of your movements
Lebron lebron;
int VelocityY = 0;
int velocityX = -3; //rate at which the pipe moves to the left
double gravity = 1;

Timer gameLoop;
Timer placePipeTimer;
boolean gameOver = false;
double score = 0;
boolean beatGame = false;

ArrayList<Pipe> pipes;
Random random = new Random();

//constructor
    FlappyLebron() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        
        //makes sures that the FlappyLebron class is the one that gets the keylistener info
        setFocusable(true);
        //make sure Lebron checks the 3 keylistener functions
        addKeyListener(this);

        //load the images
        //background img
        backgroundImg = new ImageIcon(getClass().getResource("flappybirdbg.png")).getImage();
        //lebronimg
        lebronImg = new ImageIcon(getClass().getResource("download.png")).getImage();
        //top pipe img
        topPipeImg = new ImageIcon(getClass().getResource("toppipe.png")).getImage();
        //bottom pipe img
        bottomPipeImg = new ImageIcon(getClass().getResource("bottompipe.png")).getImage();
        
        //lebron object
        lebron = new Lebron(lebronImg);
        //pipe object
        pipes = new ArrayList<Pipe>();

        //timer object
        gameLoop = new Timer(1000/60, this); // gameLoop for 60 frames per second
        gameLoop.start();

        //place pipe timer
        //place a new pipe every 1.5 secs
        placePipeTimer = new Timer(1500,  new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipe();
            }
        });
        placePipeTimer.start();
    }

public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
    }

public void draw(Graphics g) {
    //draw background
    g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
    //draw lebron
    g.drawImage(lebron.img, lebron.x, lebron.y, lebron.width, lebron.height, null);
    //draw pipes
    for (int i = 0; i < pipes.size(); i++) {
        Pipe pipe = pipes.get(i);
        g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
    }
    //draw score
    g.setColor(Color.orange);
    g.setFont(new Font("Arial", Font.PLAIN, 32));

    if(gameOver) {
        g.drawString("GameOver: " + String.valueOf((int) score), 10, 35);
    }
    else {
        g.drawString(String.valueOf((int)score), 10, 35);
    }
    //draw special message if you beat game
    if(beatGame) {
        g.setColor(Color.black);
        g.drawString("YOU BEAT THE GAME, YOU ARE NOW A TRUE BALLER!", 20, 40);
    }
}

public void move() {
    // Implement gravity to LeBron
    VelocityY += gravity;
    // Moving LeBron upwards at a rate of 6 units per frame!
    lebron.y += VelocityY;
    // Does not allow LeBron to go past 0 of the y-axis
    lebron.y = Math.max(lebron.y, 0);

    for (int i = 0; i < pipes.size(); i++) {
        Pipe pipe = pipes.get(i);
        pipe.x += velocityX;

        // Checks if LeBron passes a pipe
        if (!pipe.passed && lebron.x > pipe.x + pipe.width) {
            pipe.passed = true;
            score += 0.5;
        }

        // Checks if LeBron touches a pipe
        if (collision(lebron, pipe)) {
            gameOver = true;
        }

        //checks if you beat the game
        if(score == 100) {
            beatGame = true;
        }
    }

    // Checks if LeBron goes off the board
    if (lebron.y > boardHeight) {
        gameOver = true;
    }
}
private boolean collision(Lebron lebron, Pipe pipe) {
    // Calculate the boundaries of the objects
    int lebronLeft = lebron.x;
    int lebronRight = lebron.x + lebron.width;
    int lebronTop = lebron.y;
    int lebronBottom = lebron.y + lebron.height;

    int pipeLeft = pipe.x;
    int pipeRight = pipe.x + pipe.width;
    int pipeTop = pipe.y;
    int pipeBottom = pipe.y + pipe.height;

    // Check for collision
    if (lebronRight > pipeLeft && lebronLeft < pipeRight && lebronBottom > pipeTop && lebronTop < pipeBottom) {
        return true; // Collision detected
    }

    return false; // No collision detected
}

@Override
public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub}
    move();
    repaint();
    if (gameOver) {
        placePipeTimer.stop();
        gameLoop.stop();
    }
}

@Override
public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub
    if (e.getKeyCode() == KeyEvent.VK_SPACE);
    VelocityY = -8;
    if (gameOver) {
        //restart game when game over and reset all game condiitons to default values
        lebron.y = lebronY;
        VelocityY = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
        gameLoop.start();
        placePipeTimer.start();
    }
}

@Override
public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub
}

@Override
public void keyReleased(KeyEvent e) {
    // TODO Auto-generated method stub
}

public void placePipe() {
    int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random() * (pipeHeight/2));
    int openingSpace = boardHeight/4;
    //pipe height is now 256 subtracted by a random number
    Pipe topPipe = new Pipe(topPipeImg);
    topPipe.y = randomPipeY;
    pipes.add(topPipe);

    Pipe bottomPipe = new Pipe(bottomPipeImg);
    bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
    pipes.add(bottomPipe);
}
}

