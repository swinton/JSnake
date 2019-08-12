# JSnake

> :snake: The classic [Snake game](https://en.wikipedia.org/wiki/Snake_(video_game_genre)), written in Java!

![snake](https://user-images.githubusercontent.com/27806/62838926-53234b80-bc48-11e9-8b8f-1cbe1cd573af.gif)

## Build

With Maven:

```shell
mvn package
```

## Run

After building, you should have a `.jar` file in the `target` folder. Run the game with:

```shell
java -jar target/snake3-1.0-SNAPSHOT.jar
```

## Play

There are 5 levels of play, each of which incrementally increase the snake's speed. Press <kbd>1</kbd>, <kbd>2</kbd>, <kbd>3</kbd>, <kbd>4</kbd>, or <kbd>5</kbd> to activate each level.

Press <kbd>spacebar</kbd> to begin play.

Use cursor keys <kbd>UP</kbd>, <kbd>DOWN</kbd>, <kbd>LEFT</kbd>, <kbd>RIGHT</kbd> to steer the snake towards the food. Avoid steering the snake into a wall or into itself to survive!
