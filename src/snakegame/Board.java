package snakegame;

/**
 * @author Jeremy Stark
 * @version 1.0
 * @since 2017-10-11
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener 
{
    /**
     * Constants used within the program.
     */
    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    /**
     * Integer arrays used to hold the position
     * of the snake.
     * The array is the size of the board.
     */
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    /**
     * number of addition sections
     * that the snake has
     */
    private int dots;
    
    /**
     * Position of the "apple" on the board.
     */
    private int apple_x;
    private int apple_y;

    /**
     * Boolean variables that represent the 
     * direction that the snake is going.
     */
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    /**
     * Game timer.
     */
    private Timer timer;

    /**
     * Board constructor method.
     */
    public Board() 
    {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        initGame();
    }

    /**
     * Init method for the game.
     */
    private void initGame() 
    {
        //Sets starting size of snake
        dots = 3;

        //Places the snake's tail at the wall
        //Places snake 50 spaces down the wall
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        //Places the apple in a random location.
        locateApple();

        //Starts the timer for the game.
        timer = new Timer(DELAY, this);
        timer.start();
    }

    /**
     * Paint function. 
     * Handles drawing graphics.
     * @param g 
     */
    @Override
    public void paint(Graphics g) 
    {
        super.paintComponent(g);

        //Calls doDrawing function.
        doDrawing(g);
    }
   
    /**
     * Draws all the different parts of the game
     * @param g 
     */
    private void doDrawing(Graphics g) 
    {
        //If you are in the frame then run this stuff.
        if (inGame) 
        {
            //Set color to green and draw's the apple at it's location.
            g.setColor(Color.GREEN);
            g.fillOval(apple_x, apple_y, 10, 10);

            //Goes through and draw each of the dots
            for (int z = 0; z < dots; z++) 
            {
                /*
                    if it's the first dot (head) 
                    then make it white and print it.
                    Otherwise just color it blue.
                */
                if (z == 0)
                {
                    g.setColor(Color.WHITE);
                    g.fillOval(x[z], y[z], 10, 10);
                } 
                else 
                {
                    g.setColor(Color.BLUE);
                    g.fillOval(x[z], y[z], 10, 10);
                }
            }
            //Just syncs the graphics and make sure everything is right.
            Toolkit.getDefaultToolkit().sync();

        } 
        else 
        {
            //If we're in "in game" then show the game over screen.
            gameOver(g);
        }        
    }

    /**
     * Shows the end game screen.
     * @param g 
     */
    private void gameOver(Graphics g)
    {
        g.setColor(Color.RED);
        g.drawString("Dang bro, you just done died",70,20); 
    }

    /**
     * If the apple is at (0,0), then 
     * move it to (1,1), cause it would
     * be impossible to get it at (0,0).
     */
    private void checkApple() 
    {
        if ((x[0] == apple_x) && (y[0] == apple_y)) 
        {
            dots++;
            
            //Moves the apple somewhere random
            locateApple();
        }
    }

    /**
     * All the stuff for moving the snake.
     * Aka the juicy stuff.
     */
    private void move() 
    {

        /*
            decreases the position so that it can 
            draw new ones without changing the length.
        */
        for (int z = dots; z > 0; z--) 
        {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        /*
            Because the head of the snake is the real thing
            we're controlling, we only need to move the 
            object in the array and the previous for loop
            will move everything else in the proper direction.
        
            They just move the head + or - one dot size (10).
        */
        if (leftDirection) 
        {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) 
        {
            x[0] += DOT_SIZE;
        }

        if (upDirection) 
        {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) 
        {
            y[0] += DOT_SIZE;
        }
    }

    /**
     * Checks collision of the snake.
     */
    private void checkCollision() 
    {
        /*
            Goes through each dot and checks if it's in the frame.
        */
        for (int z = dots; z > 0; z--) 
        {
            //If you're longer than 4 and cross over yourself then you ded bro
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) 
            {
                inGame = false;
            }
        }
        
        //All of these just check for the frame bounds.
        if (y[0] >= B_HEIGHT) 
        {
            inGame = false;
        }

        if (y[0] < 0)
        {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) 
        {
            inGame = false;
        }

        if (x[0] < 0) 
        {
            inGame = false;
        }
        
        //If you're no longer in the frame then stop the timer.
        if(!inGame) 
        {
            timer.stop();
        }
    }

    /**
     * Randomly places the apple somewhere on the screen.
     */
    private void locateApple() 
    {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    /**
     * Every time an action occurs, 
     * check if the apple is in a good spot
     * otherwise it will move it, checks the collision
     * of the snake, and will move the snake based on that action.
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (inGame) 
        {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }
    
    /**
     * Our KeyListener
     */
    private class TAdapter extends KeyAdapter 
    {

        @Override
        public void keyPressed(KeyEvent e)
        {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) 
            {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) 
            {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) 
            {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) 
            {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            
            if(key == KeyEvent.VK_Q)
            {
                System.exit(0);
            }
        }
    }
}