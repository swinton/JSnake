package com.stevewinton.games.snake3.event;

import com.stevewinton.games.snake3.Food;

public class FoodEatenEvent {
  private Food f;
  public FoodEatenEvent(Food food) {
    f = food;
  }
  public Food getFoodEaten() {
    return f;
  }
}
