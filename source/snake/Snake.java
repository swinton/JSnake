package com.stevewinton.games.snake3;

import java.util.ArrayList;
import java.util.ListIterator;

//import com.stevewinton.games.snake3.exception.*;
import com.stevewinton.games.snake3.event.*;

public class Snake implements Runnable {
  ArrayList<SnakeMovedEventListener> eventListeners = new ArrayList<SnakeMovedEventListener>();
  ArrayList<SnakeBlock> blocks;
  int xBoundary;
  int yBoundary;
  boolean visible;
  boolean isOutOfBounds;
  boolean hasCollided;

  Snake(SnakeDirection initialDirection, int initialLength, int initialXPos, int initialYPos, int xBoundary, int yBoundary) {
    blocks = new ArrayList<SnakeBlock>(initialLength);
    for (int i = 0; i < initialLength; i++) {
      blocks.add(i, new SnakeBlock(initialDirection, initialXPos, initialYPos));
    }
    this.xBoundary = xBoundary;
    this.yBoundary = yBoundary;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer("Snake length: " + blocks.size());
    sb.append("\nSnake direction: ");
    sb.append(getHead().getDirection());
    sb.append("\nSnake position: ");
    for (SnakeBlock b : blocks) {
      sb.append("[");
      sb.append(b.getX());
      sb.append(",");
      sb.append(b.getY());
      sb.append("]");
    }
    return sb.toString();
  }

  public void run() {
    while (!isOutOfBounds && !hasCollided) {
      move();
      // Slow the snake down!
      try {
        Thread.sleep(75);
      }
      catch (Exception ex) {}
    }
  }

  // This method will be the source of the FoodEatenEvent
  void move() {
    // Keep moving in head direction
    SnakeBlock head = getHead();
    SnakeDirection direction = head.getDirection();

    // Calculate newX and newY
    int newY = head.getY();
    int newX = head.getX();

    switch (direction) {
      case UP : 
        newY--;
        break;
      case DOWN :
        newY++;
        break;
      case LEFT : 
        newX--;
        break;
      case RIGHT :
        newX++;
        break;
    }
    
    SnakeMovedEvent e = new SnakeMovedEvent(this);

    // Validate move
    if (newX > xBoundary || newX < 0 || newY > yBoundary || newY < 0) {
      // Out of bounds
      isOutOfBounds = true;
      e.setOutOfBounds(true);
    }
    else if (intersects(newX, newY)) {
      // Self collision
      hasCollided = true;
      e.setSelfCollision(true);
    }
    else {
      // Move was valid
      e.setValidMove(true);
      // Now, set new co-ordinates/direction on each block
      int previousX;
      int previousY;

      for (SnakeBlock b : blocks) {
        // Each block gets the position of the next block up in the ArrayList
        previousX = b.getX();
        previousY = b.getY();
        b.setX(newX);
        b.setY(newY); 
        b.setDirection(direction);
        newX = previousX;
        newY = previousY;
        direction = b.getDirection();
      }
    }
    // Finally, notify listeners of SnakeMovedEvent
    for (SnakeMovedEventListener listener : eventListeners)
      listener.snakeMoved(e);
  }

  void grow() {
    // Add a clone of the tail
    blocks.add(new SnakeBlock(getTail()));
  }

  void grow(int numOfBlocks) {
    for (int i = 0; i < numOfBlocks; i++) {
      grow();
    }
  }

  synchronized void turn(SnakeDirection direction) {
    // Validate and set new direction
    if (validDirection(direction)) 
      getHead().setDirection(direction);
  }

  boolean contains(SnakeBlock b) {
    return blocks.contains(b);
  }

  boolean intersects(int x, int y) {
    // Create a temporary SnakeBlock, any old direction will do, since the 
    // implementation of SnakeBlock.equals() ignores the direction
    SnakeBlock sb = new SnakeBlock(SnakeDirection.RIGHT, x, y);
    return contains(sb); // This should work because ArrayList.contains() calls .equals() on each of its elements
  }

  SnakeBlock getHead() {
    return blocks.get(0);
  }

  SnakeBlock getTail() {
    return blocks.get(blocks.size() - 1);
  }

  boolean isHead(SnakeBlock b) {
    return (blocks.indexOf(b) == 0);
  }

  boolean isTail(SnakeBlock b) {
    return (blocks.indexOf(b) == blocks.size() - 1);
  }

  /*
  void setFood(Food food) {
    f = food;
  }
  */

  SnakeDirection getDirection() {
    return getHead().getDirection();
  }

  ArrayList<SnakeBlock> getSnakeBlocks() {
    return blocks;
  }

  void addSnakeMovedEventListener(SnakeMovedEventListener listener) {
    eventListeners.add(listener);
  }

  private boolean validDirection(SnakeDirection direction) {
    SnakeDirection headDirection = getHead().getDirection();
    // Either current head direction is UP/DOWN and new direction is LEFT/RIGHT
    // Or current head direction is LEFT/RIGHT and new direction is UP/DOWN
    return
    (((headDirection == SnakeDirection.UP || headDirection == SnakeDirection.DOWN) &&
      (direction == SnakeDirection.LEFT || direction == SnakeDirection.RIGHT)) || 
     ((headDirection == SnakeDirection.LEFT || headDirection == SnakeDirection.RIGHT) &&
      (direction == SnakeDirection.UP || direction == SnakeDirection.DOWN)));
  }

  boolean isVisible() {
    return visible;
  }

  void setVisible(boolean b) {
    visible = b;
  }
}
