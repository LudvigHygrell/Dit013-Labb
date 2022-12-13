package breakout.model;

/*
 *   A brick for the rows of bricks
 */

public class Brick extends Positionable implements IPositionable {

    public static final double BRICK_WIDTH = 20;  // Default values, use in constructors, not directly
    public static final double BRICK_HEIGHT = 10;
    private int layer;

    public Brick (double posX, double posY, int layer) {
        this.posX = posX;
        this.posY = posY;
        this.width = BRICK_WIDTH;
        this.height = BRICK_HEIGHT;
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }
}

