package com.stevewinton.games.snake3;

import java.util.ArrayList;

import com.stevewinton.games.snake3.event.*;
//import com.stevewinton.games.snake3.exception.*;

public class SnakeTestDrive implements FoodEatenEventListener {
  Snake s;
  Food f;

  public SnakeTestDrive() {
    s = new Snake(SnakeDirection.RIGHT, 5, 0, 0, 19, 19);
    f = new Food(s, 19, 19);
  }

  public SnakeTestDrive(Snake snake, Food food) {
    s = snake;
    f = food;
  }

  public void foodEaten(FoodEatenEvent e) {
    System.out.println("Food eaten!");
    f.setX(0);
    f.setY(5);
  }

  void testCoordSystem() {
    int[][] coords = {{19,19},{19,18},{19,17},{19,16},{19,15}};
    ArrayList<SnakeBlock> blocks = s.getSnakeBlocks();
    for (int i = 0; i < coords.length; i++) {
      SnakeBlock b = blocks.get(i);
      b.setX(coords[i][0]);
      b.setY(coords[i][1]);
    }
  }

  /*
  void go() {
    f.addFoodEatenEventListener(this);
    // Explicitly position food, so it gets eaten
    f.setX(0);
    f.setY(5);
    System.out.println(f);
    int i = 0;
    while (true) {
      try {
        s.move();
        // Slow the snake down!
        try {
          Thread.sleep(50);
        }
        catch (Exception e) {}
      }
      catch (SnakeOutOfBoundsException e) {
        System.out.println("Game over! Snake out of bounds!");
        break;
      }
      catch (SnakeIllegalMoveException e) {
        System.out.println("Game over! Snake has eaten itself!");
        break;
      }
      // Turn the snake occassionally
      i++;
      if ((i % 10) == 0) {
        switch (s.getDirection()) {
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
        // Grow by 5 blocks after each turn
        s.grow();
        System.out.println(s);
      }
    }
  }
  */

  public static void main (String[] args) {
    SnakeTestDrive testDrive = new SnakeTestDrive();
    //testDrive.go();
  }
}
