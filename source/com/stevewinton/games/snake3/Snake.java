package com.stevewinton.games.snake3;

import java.util.ArrayList;

import com.stevewinton.games.snake3.exception.*;
import com.stevewinton.games.snake3.event.*;

public class Snake {
  ArrayList<SnakeMovedEventListener> eventListeners = new ArrayList<SnakeMovedEventListener>();
  ArrayList<SnakeBlock> blocks;
  //Food f; // need awareness of food position, to raise FoodEatenEvents
  int xBoundary;
  int yBoundary;

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

  // This method will be the source of the FoodEatenEvent
  synchronized void move() throws SnakeOutOfBoundsException, SnakeIllegalMoveException {

    // Keep moving in head direction

    SnakeBlock head = getHead();
    SnakeDirection headDirection = head.getDirection();

    // Calculate newX and newY
    int newY = head.getY();
    int newX = head.getX();

    switch (headDirection) {
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
    
    // Validate move
    if (newX > xBoundary || newX < 0 || newY > yBoundary || newY < 0)
      throw new SnakeOutOfBoundsException();

    if (intersects(newX, newY))
      throw new SnakeIllegalMoveException();
    
    /*
    // Move is valid, test to see if the food has been eaten
    if (newX == f.getX() && newY == f.getY()) {
      // Food has been eaten, notify all FoodEatenEventListeners!
      FoodEatenEvent e = new FoodEatenEvent(f);
      for (FoodEatenEventListener listener : eventListeners)
        listener.foodEaten(e);
    }
    */

    // Now, set new co-ordinates on each block
    int previousX;
    int previousY;

    for (SnakeBlock b : blocks) {
      // Each block gets the position of the next block up in the ArrayList
      previousX = b.getX();
      previousY = b.getY();
      b.setX(newX);
      b.setY(newY); 
      newX = previousX;
      newY = previousY;
    }

    // Finally, notify listeners of SnakeMovedEvent
    SnakeMovedEvent e = new SnakeMovedEvent(this);
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

  void turn(SnakeDirection direction) {
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
}
