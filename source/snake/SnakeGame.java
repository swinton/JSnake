package com.stevewinton.games.snake3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.stevewinton.games.snake3.event.*;

public class SnakeGame implements FoodEatenEventListener, SnakeMovedEventListener {
  int xBoundary = 19;
  int yBoundary = 19;
  int gameWidth = 440;
  int gameHeight = 470;
  int interval = 150;

  Color snakeColor1 = new Color (255, 196, 33); 
  Color snakeColor2 = new Color (217, 135, 28);
  Color foodColor = new Color (34, 166, 255); 
  Color backgroundColor = Color.gray;
  JFrame frame;
  JPanel borderPanel; // An 'outer' panel, used to create a border round the 
                      // SnakePanel
  SnakePanel panel;
  SnakeGameController controller;
  Snake s;
  SnakeFX fx;
  Food f;

  int score;
  int scoreIncrement = 10;
  boolean playing; // Flag to indicate whether game is being played
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

  public void snakeMoved(SnakeMovedEvent e) {
    if (e.isValidMove()) {
      repaint();
    }
    else if (e.isOutOfBounds()) {
      System.out.println("Snake out of bounds!");
      System.out.println("New co-ordinates: [" + e.getNewX() + "," + e.getNewY() + "]");
      System.out.println(s);
      gameOver();
    }
    else if (e.isSelfCollision()) {
      System.out.println("Snake has eaten itself!");
      System.out.println("New co-ordinates: [" + e.getNewX() + "," + e.getNewY() + "]");
      System.out.println(s);
      gameOver();
    }
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
    panel.setPreferredSize(new Dimension(gameWidth - 30, gameHeight - 30));
    panel.addMouseListener(controller);
    borderPanel.add(BorderLayout.CENTER, panel);
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
    reset();
  }

  private void go() {
    s.setVisible(true);
    f.setVisible(true);

    if (!playing) {
      playing = true;

      s.setInterval(interval);

      Thread snakeThread = new Thread(s);
      snakeThread.setName("Snake Thread");
      snakeThread.start();
    }
  }

  private void setupGameObjects() {
    score = 0;
    controller = new SnakeGameController();

    // Create snake
    s = new Snake(SnakeDirection.RIGHT, 3, 0, 0, xBoundary, yBoundary);
    s.setVisible(false);

    // Create food
    f = new Food(s, xBoundary, yBoundary);
    f.setVisible(false);

    // Create sound effects
    fx = new SnakeFX();
    fx.init();

    // Listen for the FoodEaten event
    f.addFoodEatenEventListener(this);

    // The FX instance also listens for the food eaten event
    f.addFoodEatenEventListener(fx);

    // Listen for the SnakeMoved event
    s.addSnakeMovedEventListener(this);

    // The Food also listens for the SnakeMoved event
    s.addSnakeMovedEventListener(f);
  }

  private void reset() {
    setupGameObjects();
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndAddSnakePanel();
      }
    });
  }

  private void init() {
    setupGameObjects();
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

  public static void main (String[] args) {
    SnakeGame game = new SnakeGame();
    game.init();
  }

  class SnakeGameController implements KeyListener, MouseListener, ComponentListener {

    public void keyPressed(KeyEvent e) {
      // Steer the snake!
      switch (e.getKeyCode()) {
        case KeyEvent.VK_KP_UP :
        case KeyEvent.VK_UP :
        case KeyEvent.VK_K :
          s.putTurn(SnakeDirection.UP);
          break;
        case KeyEvent.VK_KP_DOWN :
        case KeyEvent.VK_DOWN :
        case KeyEvent.VK_J :
          s.putTurn(SnakeDirection.DOWN);
          break;
        case KeyEvent.VK_KP_LEFT :
        case KeyEvent.VK_LEFT :
        case KeyEvent.VK_H :
          s.putTurn(SnakeDirection.LEFT);
          break;
        case KeyEvent.VK_KP_RIGHT :
        case KeyEvent.VK_RIGHT :
        case KeyEvent.VK_L :
          s.putTurn(SnakeDirection.RIGHT);
          break;
      }
    }
  
    public void keyReleased(KeyEvent e) {
      // Not implemented
    }

    public void keyTyped(KeyEvent e) {
      if (!playing) {
        switch (e.getKeyChar()) {
          case ' ' :
            // Hit spacebar to get things moving!
            go();
            break;
          case '1' :
            // Easy level
            interval = 275;
            break;
          case '2' :
            interval = 225;
            break;
          case '3' :
            interval = 175;
            break;
          case '4' :
            interval = 125;
            break;
          case '5' :
            // Extreme level
            interval = 75;
            break;
        }
      }
    }
    
    public void mouseClicked(MouseEvent e) {
      if (!playing) {
        // Start the game
        go();
      }
      else {
        // Steer the snake!
        if (e.getButton() == 1) {
          // Left mouse button = always turn snake clockwise
          switch (s.getHead().getDirection()) {
            case UP :
              s.putTurn(SnakeDirection.LEFT);
              break;
            case DOWN :
              s.putTurn(SnakeDirection.RIGHT);
              break;
            case LEFT :
              s.putTurn(SnakeDirection.DOWN);
              break;
            case RIGHT :
              s.putTurn(SnakeDirection.UP);
              break;
          }
        }
        else {
          // Secondary mouse button = always turn snake anti-clockwise
          switch (s.getHead().getDirection()) {
            case UP :
              s.putTurn(SnakeDirection.RIGHT);
              break;
            case DOWN :
              s.putTurn(SnakeDirection.LEFT);
              break;
            case LEFT :
              s.putTurn(SnakeDirection.UP);
              break;
            case RIGHT :
              s.putTurn(SnakeDirection.DOWN);
              break;
          }
        }
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

    public void componentResized(ComponentEvent e) {
      //resized = true;
      //System.out.println(frame.getWidth() + ", " + frame.getHeight());
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
    int xMargin;
    int yMargin;

    private void paintBlock(Graphics g, int x, int y) {
      g.fillRect(xMargin + (x * blockWidth + 1), yMargin + (y * blockHeight + 1), blockWidth - 1, blockHeight - 1);
    }

    private void paintBackground(Graphics g) {
      g.setColor(backgroundColor);
      // Get width, height
      int w = (blockWidth * (xBoundary + 1)); 
      int h = (blockHeight * (yBoundary + 1)); 
      g.fillRect(xMargin, yMargin, w, h);
      // draw vertical gridlines
      g.setColor(Color.black);
      int x = 0;
      while (x <= w) {
        g.drawLine(x + xMargin, yMargin, x + xMargin, h + yMargin);
        x += blockWidth;
      }
      // draw horizontal gridlines
      int y = 0;
      while (y <= h) {
        g.drawLine(xMargin, y + yMargin, w + xMargin, y + yMargin);
        y += blockHeight;
      }
    }

    private void paintSnake(Graphics g) {
      g.setColor(snakeColor1);
      ArrayList<SnakeBlock> blocks = s.getSnakeBlocks();
      int i = 0;
      for (SnakeBlock b : blocks) {
        paintBlock(g, b.getX(), b.getY());
        // Alternate colour every 3 SnakeBlocks
        if (++i % 3 == 0)
          g.setColor((g.getColor().equals(snakeColor1) ? snakeColor2 : snakeColor1));
      }
    }

    private void paintFood(Graphics g) {
      g.setColor(foodColor);
      paintBlock(g, f.getX(), f.getY());
    }

    private void debug() {
      System.out.println("width: "+getWidth());
      System.out.println("height: "+getHeight());
      System.out.println("xBoundary: "+xBoundary);
      System.out.println("yBoundary: "+yBoundary);
      System.out.println("blockWidth: "+blockWidth);
      System.out.println("blockHeight: "+blockHeight);
    }

    public void calcBlockSize() {
      int w = getWidth();
      int h = getHeight();
      // Use same size for blockWidth and blockHeight
      // 'boundary + 1' as our co-ordinate system starts from 0
      blockHeight = blockWidth = 
        Math.min(w / (xBoundary + 1), h / (yBoundary + 1));
      // Reset 'resized' flag
      //resized = false;
      // Calculate x and y margins
      xMargin = (w - (blockWidth * (xBoundary + 1))) / 2;
      yMargin = (h - (blockHeight * (yBoundary + 1))) / 2;
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
      if (s.isVisible())
        paintSnake(g);
    }
  }
}

