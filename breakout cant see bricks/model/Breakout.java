package breakout.model;


import java.util.ArrayList;
import java.util.List;

/*
 *  Overall all logic for the Breakout Game
 *  Model class representing the full game
 *  This class should use other objects delegate work.
 *
 *  NOTE: Nothing visual here
 *
 */
public class Breakout {

    public static final double GAME_WIDTH = 400;
    public static final double GAME_HEIGHT = 400;
    public static final double BALL_SPEED_FACTOR = 1.05; // Increase ball speed
    public static final long SEC = 1_000_000_000;  // Nano seconds used by JavaFX

    private final Ball ball;
    private final List<Wall> walls;
    private final List<Brick> bricks;
    private final Paddle paddle;
    private int nBalls = 5;
    int playerPoints;

    private boolean leftKeyPressed;
    private boolean rightKeyPressed;


    // TODO Constructor that accepts all objects needed for the model
    public Breakout (Ball ball, List<Wall> walls, Paddle paddle, List<Brick> bricks) {
        this.ball = ball;
        this.walls = walls;
        this.paddle = paddle;
        this.bricks = bricks;
    }

    // --------  Game Logic -------------

    private long timeForLastHit;         // To avoid multiple collisions

    public void update(long now) {
        // TODO  Main game loop, start functional decomposition from here
        ball.updatePosition();
        paddle.movePosition(paddle.PADDLE_SPEED,leftKeyPressed,rightKeyPressed);
        for (Wall wall : walls) {
            if (wall.detectCollision(ball)) {
                switchDirection(wall.getDir());
            }
        }
        if (paddle.detectCollision(ball)){
            switchDirection(Wall.Dir.HORIZONTAL);
        }
    }

    // ----- Helper methods--------------
    // Used for functional decomposition

    public void switchDirection (Wall.Dir dir) {
        if (dir == Wall.Dir.VERTICAL) {
            ball.setSpeedX(-ball.getSpeedX());
        }
        else if (dir == Wall.Dir.HORIZONTAL) {
            ball.setSpeedY(-ball.getSpeedY());
        }
    }

    // --- Used by GUI  ------------------------

    public List<IPositionable> getPositionables() {
        // TODO return all objects to be rendered by GUI
        List<IPositionable> models = new ArrayList<>(bricks);
        models.add(ball);
        models.add(paddle);
        return models;
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public int getnBalls() {
        return nBalls;
    }
    public void setLeftKeyPressed(boolean left) {
        leftKeyPressed = left;
    }
    public void setRightKeyPressed(boolean right) {
        rightKeyPressed = right;
    }





}
