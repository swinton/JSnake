package com.stevewinton.games.snake3;

import java.util.ArrayList;

import com.stevewinton.games.snake3.event.*;

// This class will listen for the FoodEatenEvent
public class Food implements SnakeMovedEventListener {
  ArrayList<FoodEatenEventListener> eventListeners = new ArrayList<FoodEatenEventListener>();
  int x;
  int y;
  int xBoundary;
  int yBoundary;
  boolean visible = true;
  Snake s; // need awareness of snake position

  Food(Snake snake, int xBoundary, int yBoundary) {
    this.s = snake;
    this.xBoundary = xBoundary;
    this.yBoundary = yBoundary;
    position();
  }

  public String toString() {
    return "Food position: " + getX() + ", " + getY();
  }

  void position() {
    int xCandidate;
    int yCandidate;
    // Make sure position of food does not intersect with the snake
    do {
      xCandidate = (int) (Math.random() * xBoundary);
      yCandidate = (int) (Math.random() * yBoundary);
    } while (s.intersects(xCandidate, yCandidate));
    setX(xCandidate);
    setY(yCandidate);
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

  boolean isVisible() {
    return visible;
  }

  void setVisible(boolean visible) {
    this.visible = visible;
  }

  void repositionFood() {
    // reposition the food
    setVisible(false);
    position();
    setVisible(true);
  }

  public synchronized void snakeMoved(SnakeMovedEvent e) {
    if (s.getHead().getX() == x && s.getHead().getY() == y) {
      // food eaten!
      repositionFood();
      // notify listeners
      FoodEatenEvent fe = new FoodEatenEvent(this);
      for (FoodEatenEventListener listener : eventListeners)
        listener.foodEaten(fe);
    }
  }

  void addFoodEatenEventListener(FoodEatenEventListener listener) {
    eventListeners.add(listener);
  }

}

