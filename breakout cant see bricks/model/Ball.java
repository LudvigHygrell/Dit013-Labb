package breakout.model;

import java.util.Random;

import static breakout.model.Breakout.GAME_HEIGHT;
import static breakout.model.Breakout.GAME_WIDTH;

/*
 *    A Ball for the Breakout game
 */

public class Ball extends Positionable implements IPositionable {
    private double speedX;
    private double speedY;
    public Ball (double posX, double posY, double width, double height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.speedX = 0.08;
        this.speedY = -0.08;
    }
    public void updatePosition (){
        posX = posX + speedX;
        posY = posY + speedY;
    }
    //Might delete
    public void setSpeedX (double speedX){
        this.speedX = speedX;
    }
    public void setSpeedY (double speedY){
        this.speedY = speedY;
    }
    public double getSpeedX() {
        return speedX;
    }
    public double getSpeedY() {
        return speedY;
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
