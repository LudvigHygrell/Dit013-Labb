package breakout.model;

public abstract class Positionable {
    public double posX;
    public double posY;
    public double width;
    public double height;

    public boolean detectCollision (Positionable model) {
        if (posX < model.posX + model.width && posX + width > model.posX &&
                posY < model.posY + model.height && posY + height > model.posY
        ) {
            return true;
        }
        else {
            return false;
        }
    }

}
