package com.stevewinton.games.snake3;

public class SnakeBlock {
  SnakeDirection d;
  int x;
  int y;

  SnakeBlock(SnakeDirection d, int xPos, int yPos) {
    this.d = d;
    this.x = xPos;
    this.y = yPos;
  }

  SnakeBlock(SnakeBlock sb) {
    this.d = sb.d;
    this.x = sb.x;
    this.y = sb.y;
  }

  public boolean equals(Object o) {
    if (o instanceof SnakeBlock) {
      SnakeBlock sb = (SnakeBlock)o;
      // Ignore direction (theory is this will help us position the food
      // more efficiently).
      return (sb.x == this.x && sb.y == this.y);
    }
    else {
      return false;
    }
  }

  synchronized SnakeDirection getDirection() {
    return d;
  }

  synchronized void setDirection(SnakeDirection direction) {
    d = direction;
  }

  synchronized int getX() {
    return x;
  }

  synchronized void setX(int newX) {
    x = newX;
  }

  synchronized int getY() {
    return y;
  }

  synchronized void setY(int newY) {
    y = newY;
  }

}
