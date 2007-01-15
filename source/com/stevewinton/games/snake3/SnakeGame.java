package com.stevewinton.games.snake3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.stevewinton.games.snake3.event.*;
import com.stevewinton.games.snake3.exception.*;

public class SnakeGame implements FoodEatenEventListener, SnakeMovedEventListener {
  int xBoundary = 19;
  int yBoundary = 19;
  int gameWidth = 480;
  int gameHeight = 480;
  Color snakeColor = Color.yellow;
  Color foodColor = Color.green;
  Color backgroundColor = Color.gray;
  JFrame frame;
  JPanel borderPanel; // An 'outer' panel, used to create a border round the 
                      // SnakePanel
  SnakePanel panel;
  SnakeGameController controller;
  Snake s;
  Food f;
  int score;
  int scoreIncrement = 10;
  boolean playing;
  boolean resized = true; // Flag to indicate whether playing area has been resized

  private void repaint() {
    if (panel != null) {
      panel.repaint();
    }
  }

  public void foodEaten(FoodEatenEvent e) {
    score += scoreIncrement;
    s.grow(3);
    repaint();
  }

  public synchronized void snakeMoved(SnakeMovedEvent e) {
    System.out.println("Snake moved!");
    repaint();
  }

  private void test() {
    SnakeTestDrive testDrive = new SnakeTestDrive(s, f);
    testDrive.testCoordSystem();
  }

  private void createAndAddSnakePanel() {
    if (panel != null) {
      // Remove existing panel
      borderPanel.remove(panel);
    }
    panel = new SnakePanel();
    panel.setPreferredSize(new Dimension(gameWidth,gameHeight));
    panel.addMouseListener(controller);
    panel.addComponentListener(controller);
    borderPanel.add(BorderLayout.CENTER, panel);
    panel.setVisible(true);
    borderPanel.revalidate();
  }

  private void createAndShowGUI() {
    // Create 'Border' panel
    borderPanel = new JPanel();
    borderPanel.setBackground(Color.lightGray);
    borderPanel.setLayout(new BorderLayout());
    borderPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Create 'Snake' panel, add it to the 'Border' panel
    createAndAddSnakePanel();

    // Create 'Container' frame
    frame = new JFrame("Snake");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.addKeyListener(controller);
    frame.add(BorderLayout.CENTER, borderPanel);
    frame.setSize(gameWidth, gameHeight);
    frame.setLocation(100, 100);
    frame.setVisible(true);
  }

  private void gameOver() {
    playing = false;
    System.out.println("Game over! Score: " + score);
    //reset();
  }

  private void moveSnake() {
    try {
      s.move();
    }
    catch (SnakeOutOfBoundsException e) {
      System.out.println("Snake out of bounds!");
      System.out.println(s);
      gameOver();
    }
    catch (SnakeIllegalMoveException e) {
      System.out.println("Snake has eaten itself!");
      System.out.println(s);
      gameOver();
    }
  }

  private void go() {
    if (SwingUtilities.isEventDispatchThread()) {
      System.out.println("IS EDT!");
    }
    else {
      System.out.println("IS *NOT* EDT!");
    }
    if (!playing) {
      playing = true;
      while (playing) {
        moveSnake();
      }
    }
  }

  private void setupGameObjects() {
    controller = new SnakeGameController();
    s = new Snake(SnakeDirection.RIGHT, 5, 0, 0, xBoundary, yBoundary);
    f = new Food(s, xBoundary, yBoundary);
    f.addFoodEatenEventListener(this);
    s.addSnakeMovedEventListener(this);
    s.addSnakeMovedEventListener(f);
  }

  private void reset() {
    setupGameObjects();
    //createAndAddSnakePanel();
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndAddSnakePanel();
      }
    });
  }

  private void init() {
    setupGameObjects();
    //createAndShowGUI();
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
    //test();
    go();
  }

  public static void main (String[] args) {
    SnakeGame game = new SnakeGame();
    game.init();
  }

  class SnakeGameController implements KeyListener, MouseListener, ComponentListener {
    public synchronized void keyPressed(KeyEvent e) {
      // Steer the snake!
      switch (e.getKeyCode()) {
        case KeyEvent.VK_KP_UP :
        case KeyEvent.VK_UP :
        case KeyEvent.VK_K :
          s.turn(SnakeDirection.UP);
          break;
        case KeyEvent.VK_KP_DOWN :
        case KeyEvent.VK_DOWN :
        case KeyEvent.VK_J :
          s.turn(SnakeDirection.DOWN);
          break;
        case KeyEvent.VK_KP_LEFT :
        case KeyEvent.VK_LEFT :
        case KeyEvent.VK_H :
          s.turn(SnakeDirection.LEFT);
          break;
        case KeyEvent.VK_KP_RIGHT :
        case KeyEvent.VK_RIGHT :
        case KeyEvent.VK_L :
          s.turn(SnakeDirection.RIGHT);
          break;
      }
    }
  
    public void keyReleased(KeyEvent e) {
      // Not implemented
    }

    public void keyTyped(KeyEvent e) {
      // Hit spacebar to get things moving!
      if (e.getKeyChar() == ' ') {
        go();
      }
    }
    
    public void mouseClicked(MouseEvent e) {
      /*
      if (e.getClickCount() > 1) {
        // Start the game
        go();
      }
      else {
      */
      // Steer the snake!
      if (e.getButton() == 1) {
        // Left mouse button
        switch (s.getHead().getDirection()) {
          case UP :
            s.turn(SnakeDirection.LEFT);
            break;
          case DOWN :
            s.turn(SnakeDirection.RIGHT);
            break;
          case LEFT :
            s.turn(SnakeDirection.DOWN);
            break;
          case RIGHT :
            s.turn(SnakeDirection.UP);
            break;
        }
      }
      else {
        // Secondary mouse button
        switch (s.getHead().getDirection()) {
          case UP :
            s.turn(SnakeDirection.RIGHT);
            break;
          case DOWN :
            s.turn(SnakeDirection.LEFT);
            break;
          case LEFT :
            s.turn(SnakeDirection.UP);
            break;
          case RIGHT :
            s.turn(SnakeDirection.DOWN);
            break;
        }
      //}
      }
    }

    public void mouseEntered(MouseEvent e) {
      // Not implemented
    }

    public void mouseExited(MouseEvent e) {
      // Not implemented
    }

    public void mousePressed(MouseEvent e) {
      // Not implemented
    }

    public void mouseReleased(MouseEvent e) {
      // Not implemented
    }

    public synchronized void componentResized(ComponentEvent e) {
      //resized = true;
    }

    public void componentHidden(ComponentEvent e) {
      // Not implemented
    }

    public void componentShown(ComponentEvent e) {
      // Not implemented
    }

    public void componentMoved(ComponentEvent e) {
      // Not implemented
    }

  }

  class SnakePanel extends JPanel {
    // !!! Ideas !!!
    // - Only recalculate blockWidth/blockHeight after the panel has been resized
    // - Only repaint the elements that change - HOW?!!!

    int blockWidth;
    int blockHeight;

    private void paintBlock(Graphics g, int x, int y) {
      g.fillRect(x * blockWidth + 1, y * blockHeight + 1, blockWidth - 1, blockHeight - 1);
    }

    private void paintBackground(Graphics g) {
      g.setColor(backgroundColor);
      // Get width, height
      int w = blockWidth * (xBoundary + 1); 
      int h = blockHeight * (yBoundary + 1); 
      g.fillRect(0, 0, w, h);
      // draw vertical gridlines
      g.setColor(Color.black);
      int x = 0;
      while (x <= w) {
        g.drawLine(x, 0, x, h);
        x += blockWidth;
      }
      // draw horizontal gridlines
      int y = 0;
      while (y <= h) {
        g.drawLine(0, y, w, y);
        y += blockHeight;
      }
    }

    private void paintSnake(Graphics g) {
      g.setColor(snakeColor);
      ArrayList<SnakeBlock> blocks = s.getSnakeBlocks();
      for (SnakeBlock b : blocks) {
        paintBlock(g, b.getX(), b.getY());
      }
    }

    private void paintFood(Graphics g) {
      g.setColor(foodColor);
      paintBlock(g, f.getX(), f.getY());
    }

    private void debug(Graphics g) {
      System.out.println("width: "+getWidth());
      System.out.println("height: "+getHeight());
      System.out.println("xBoundary: "+xBoundary);
      System.out.println("yBoundary: "+yBoundary);
      System.out.println("blockWidth: "+blockWidth);
      System.out.println("blockHeight: "+blockHeight);
    }

    public void calcBlockSize() {
      // Use same size for blockWidth and blockHeight
      // 'boundary + 1' as our co-ordinate system starts from 0
      blockHeight = blockWidth = 
        Math.min(getWidth() / (xBoundary + 1), getHeight() / (yBoundary + 1));
      // Reset 'resized' flag
      resized = false;
    }

    public void paintComponent(Graphics g) {
      //if (resized)
      calcBlockSize();

      // Paint background
      paintBackground(g);

      // Paint food
      if (f.isVisible())
        paintFood(g);

      // Paint snake
      paintSnake(g);
    }
  }

}

