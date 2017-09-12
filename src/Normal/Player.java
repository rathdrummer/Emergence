package Normal;

import java.awt.*;
import java.util.List;


/**
 * Represents the player
 */
public class Player extends Thing{

    private double acceleration;
    private double maxSpeed;
    private Image img;
    private double frictionAcc;
    private double scaleX;
    private double scaleY;

    public Player(String name){
        super(new Collision(100, 200, 0, 0));
        img = Img.loadImage(name);
        x = 100;
        y = 200;
        dx = 0;
        dy = 0;
        acceleration = 1;
        maxSpeed = 7;
        frictionAcc = 0.5;
        scaleX = 1;
        scaleY = 1;
        height = img.getHeight(null);
        width = img.getWidth(null);
        collision.updateSize(width,height);
    }



    @Override
    public void update(List<Thing> things){

        double range = 0.1;

        scaleX = (1 - range/2) + range * ( (Math.abs(dx)/maxSpeed) - (Math.abs(dy)/maxSpeed) );
        scaleY = (1 - range/2) + range * ( (Math.abs(dy)/maxSpeed) - (Math.abs(dx)/maxSpeed) );

        updateSpeed();

        handleCollisions(things);
    }



    public void incrementX(boolean direction){
        if (direction) x++;
        else x--;
    }

    public void incrementY(boolean direction){
        if (direction) y++;
        else y--;
    }

    public void incrementX() {
        incrementX(true);
    }

    public void incrementY() {
        incrementY(true);
    }

    public void incrementDY(boolean down) {
        int direction;
        if (down) direction = 1;
        else direction = -1;
        dy += direction*acceleration;

    }

    public void incrementDX(boolean right) {
        int direction;
        if (right) direction = 1;
        else direction = -1;
        dx += direction*acceleration;

    }



    @Override
    public double height() { return height * scaleY; }

    @Override
    public double width() {
        return width *scaleX;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }


    @Override
    public double xC() {
        return (int)(x + (height * 0.5 * scaleX));
    }

    @Override
    public double yC() {
        return (int)(y + (width * 0.5 * scaleY));
    }

    @Override
    public Image getImage() {
        return img;
    }
}
