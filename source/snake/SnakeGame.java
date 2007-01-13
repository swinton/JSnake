package com.stevewinton.games.snake3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import com.stevewinton.games.snake3.event.*;
import com.stevewinton.games.snake3.exception.*;

public class SnakeGame implements KeyListener, FoodEatenEventListener, SnakeMovedEventListener {
  int xBoundary = 19;
  int yBoundary = 19;
  int gameWidth = 400;
  int gameHeight = 400;
  Color snakeColor = Color.yellow;
  Color foodColor = Color.green;
  Color backgroundColor = Color.gray;
  JFrame frame;
  SnakePanel panel;
  Snake s;
  Food f;
  int score;
  int scoreIncrement = 10;
  boolean playing;

  public void foodEaten(FoodEatenEvent e) {
    score += scoreIncrement;
    s.grow(3);
    if (panel != null)
     panel.repaint();
  }

  public synchronized void snakeMoved(SnakeMovedEvent e) {
    if (panel != null) {
      panel.repaint();
    }
  }

  private void test() {
    SnakeTestDrive testDrive = new SnakeTestDrive(s, f);
    testDrive.go();
  }

  private void createAndAddSnakePanel() {
    if (panel != null) {
      // Remove existing panel
      frame.remove(panel);
    }
    panel = new SnakePanel();
    panel.addKeyListener(this);
    panel.setPreferredSize(new Dimension(gameWidth,gameHeight));
    frame.getContentPane().add(BorderLayout.CENTER, panel);
    panel.repaint();
  }

  private void createAndShowGUI() {
    frame = new JFrame("Snake");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(gameWidth, gameHeight);
    createAndAddSnakePanel();
    frame.setVisible(true);
    panel.requestFocus();
  }

  private void go() {
    if (!playing) {
      playing = true;
      while (playing) {
        try {
          s.move();
          // Slow the snake down!
          try {
            Thread.sleep(75);
          }
          catch (Exception e) {}
        }
        catch (SnakeOutOfBoundsException e) {
          System.out.println("Snake out of bounds!");
          break;
        }
        catch (SnakeIllegalMoveException e) {
          System.out.println("Snake has eaten itself!");
          break;
        }
      }
      System.out.println("Game over! Score: " + score);
      playing = false;
      reset();
    }
  }

  private void setupGameObjects() {
    s = new Snake(SnakeDirection.RIGHT, 5, 0, 0, xBoundary, yBoundary);
    f = new Food(s, xBoundary, yBoundary);
    f.addFoodEatenEventListener(this);
    s.addSnakeMovedEventListener(this);
    s.addSnakeMovedEventListener(f);
  }

  private void reset() {
    setupGameObjects();
    createAndAddSnakePanel();
  }

  private void init() {
    setupGameObjects();
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

  public void keyPressed(KeyEvent e) {
  }

  public synchronized void keyReleased(KeyEvent e) {
    // Steer the snake!
    switch (e.getKeyCode()) {
      case KeyEvent.VK_KP_UP :
      case KeyEvent.VK_UP :
      case KeyEvent.VK_K :
        //System.out.println("Up");
        s.turn(SnakeDirection.UP);
        break;
      case KeyEvent.VK_KP_DOWN :
      case KeyEvent.VK_DOWN :
      case KeyEvent.VK_J :
        //System.out.println("Down");
        s.turn(SnakeDirection.DOWN);
        break;
      case KeyEvent.VK_KP_LEFT :
      case KeyEvent.VK_LEFT :
      case KeyEvent.VK_H :
        //System.out.println("Left");
        s.turn(SnakeDirection.LEFT);
        break;
      case KeyEvent.VK_KP_RIGHT :
      case KeyEvent.VK_RIGHT :
      case KeyEvent.VK_L :
        //System.out.println("Right");
        s.turn(SnakeDirection.RIGHT);
        break;
    }
  }

  public void keyTyped(KeyEvent e) {
    System.out.println("Hellooo!");
    if (e.getKeyChar() == ' ') {
      go();
    }
  }

  class SnakePanel extends JPanel {
    // !!! Ideas !!!
    // - Try moving the Snake only after the SnakePanel has been painted
    // - Add a grid on top of the panel
    // - Only repaint the elements that change - HOW?!!!

    int blockWidth;
    int blockHeight;

    private void paintBlock(Graphics g, int x, int y) {
      g.fillRect(x * blockWidth, y * blockHeight, blockWidth, blockHeight);
    }

    private void paintBackground(Graphics g) {
      g.setColor(backgroundColor);
      g.fillRect(0, 0, getWidth(), getHeight());
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

    public void paintComponent(Graphics g) {
      blockWidth = getWidth() / xBoundary;
      blockHeight = getHeight() / yBoundary;
      paintBackground(g);
      if (f.isVisible())
        paintFood(g);
      paintSnake(g);
    }
  }

}

