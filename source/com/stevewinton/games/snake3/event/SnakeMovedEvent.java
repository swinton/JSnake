package com.stevewinton.games.snake3.event;

import com.stevewinton.games.snake3.Snake;

public class SnakeMovedEvent {
  private Snake s;
  public SnakeMovedEvent(Snake snake) {
    s = snake;
  }
  public Snake getSnake() {
    return s;
  }
}
