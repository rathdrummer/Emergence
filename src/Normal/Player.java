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
    }



    public double clamp(double val, double range){
        return Math.max(-range, Math.min(range, val));
    }

    public Vector clamp2(double x, double y, double max){
        double hyp = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        if (hyp > max) {
            double ratio = hyp / max;
            x /= ratio;
            y /= ratio;
        }
        return new Vector(x,y);
    }

    public double friction(double val){
        if (Math.abs(val) <= frictionAcc){
            val = 0;
        } else {
            val-=Math.signum(val)* frictionAcc;
        }

        return val;
    }

    @Override
    public void update(List<Thing> things){

        double range = 0.1;

        scaleX = (1 - range/2) + range * ( (Math.abs(dx)/maxSpeed) - (Math.abs(dy)/maxSpeed) );
        scaleY = (1 - range/2) + range * ( (Math.abs(dy)/maxSpeed) - (Math.abs(dx)/maxSpeed) );

        Vector v = clamp2(dx,dy,maxSpeed);
        dx = friction(v.x);
        dy = friction(v.y);

        Collision c = Collision.speedBox(x,y,width, height, dx, dy);


        if (dy > 0) {
            System.out.println("hi");
        }

        List<Thing> allPossibleCollisions = c.collidesWithThing(things, this);

        //x direction
        int newDX = (int) dx;
        int oldSign = (int) Math.signum(dx);

        c = Collision.speedBox(x, y, width, height, newDX, 0);
        List<Thing> allXCollisions = c.collidesWithThing(allPossibleCollisions, this);


        while(!allXCollisions.isEmpty() && newDX != 0) {
            newDX -= Math.signum(dx);
            if (Math.signum(newDX) != oldSign) {
                newDX = 0;
            }
            c = Collision.speedBox(x,y,width,height, newDX, 0);
            allXCollisions = c.collidesWithThing(allXCollisions, this);
        }

        x += newDX;

        //y direction
        int newDY = (int) dy;
        oldSign = (int) Math.signum(dy);

        c = Collision.speedBox(x, y, width, height, 0, newDY);
        List<Thing> allYCollisions = c.collidesWithThing(allPossibleCollisions, this);

        while(!allYCollisions.isEmpty() && newDY != 0) {
            newDY -= Math.signum(dy);
            if (Math.signum(newDX) != oldSign) {
                newDX = 0;
            }
            c = Collision.speedBox(x,y,width,height, 0, newDY);
            allYCollisions = c.collidesWithThing(allYCollisions, this);
        }

        y += newDY;
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

    private class Vector {

        public final double x;
        public final double y;

        Vector(double x, double y){
            this.x = x;
            this.y = y;
        }
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
