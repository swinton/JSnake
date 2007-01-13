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

  SnakeDirection getDirection() {
    return d;
  }

  void setDirection(SnakeDirection direction) {
    d = direction;
  }

  int getX() {
    return x;
  }

  void setX(int newX) {
    x = newX;
  }

  int getY() {
    return y;
  }

  void setY(int newY) {
    y = newY;
  }

}
