package breakout.model;

/*
        A wall for the ball to bounce
 */
public class Wall extends Positionable implements IPositionable {
    private Dir dir;
    public enum Dir {
        HORIZONTAL, VERTICAL;
    }
    public Wall (double posX, double posY, double width, double height, Dir dir){
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.dir = dir;
    }
    public Dir getDir() {
        return dir;
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
