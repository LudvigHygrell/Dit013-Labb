package breakout.model;

import static breakout.model.Breakout.GAME_HEIGHT;
import static breakout.model.Breakout.GAME_WIDTH;

/*
 * A Paddle for the Breakout game
 *
 */
public class Paddle extends Positionable implements IPositionable {

    public static final double PADDLE_WIDTH = 60;  // Default values, use in constructors, not directly
    public static final double PADDLE_HEIGHT = 10;
    public static final double PADDLE_SPEED = 0.2;

    public Paddle(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        width = PADDLE_WIDTH;
        height = PADDLE_HEIGHT;
    }

    public void movePosition (double speed, boolean left, boolean right) {
        if (right && !(posX + width >= 400) && !left){
            posX += speed;
        }
        else if (left && !(posX < 0) && !right) {
            posX -= speed;
        }
    }
    @Override
    public double getX() {
        return posX;
    }

    @Override
    public double getY() {
        return posY;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }
}
