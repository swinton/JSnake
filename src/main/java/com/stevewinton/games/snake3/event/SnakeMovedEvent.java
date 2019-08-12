package com.stevewinton.games.snake3.event;

import com.stevewinton.games.snake3.Snake;

public class SnakeMovedEvent {
  private Snake s;
  private boolean validMove;
  private boolean outOfBounds;
  private boolean selfCollision;
  private int newX;
  private int newY;

  public SnakeMovedEvent(Snake snake) {
    s = snake;
  }

  public Snake getSnake() {
    return s;
  }

  public boolean isValidMove() {
    return validMove;
  }

  public boolean isOutOfBounds() {
    return outOfBounds;
  }

  public boolean isSelfCollision() {
    return selfCollision;
  }

  public void setValidMove(boolean b) {
    validMove = b;
  }

  public void setOutOfBounds(boolean b) {
    outOfBounds = b;
  }

  public void setSelfCollision(boolean b) {
    selfCollision = b;
  }

  public void setNewX(int x) {
    newX = x;
  }

  public int getNewX() {
    return newX;
  }

  public void setNewY(int y) {
    newY = y;
  }

  public int getNewY() {
    return newY;
  }
}
