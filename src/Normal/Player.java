package Normal;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;


/**
 * Represents the player
 */
public class Player extends JPanel {
    private double x ;
    private double y ;
    private double dx;
    private double dy;
    private double acceleration;
    private double maxSpeed;
    private double size = 30;

    private String name ;
    private double frictionAcc;

    public Player(String name){
        this.name = name;
        x = 100;
        y = 200;
        dx = 0;
        dy = 0;
        acceleration = 1;
        maxSpeed = 7;
        frictionAcc = 0.5;
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform savedForm = g2d.getTransform();

        draw(g2d);

        g2d.setTransform(savedForm);

    }

    public void draw(Graphics2D g2d) {
        double range = 0.1;

        double scaleX = (1 - range/2) + range * ( (Math.abs(dx)/maxSpeed) - (Math.abs(dy)/maxSpeed) );
        double scaleY = (1 - range/2) + range * ( (Math.abs(dy)/maxSpeed) - (Math.abs(dx)/maxSpeed) );

        //sorry for the elm syntax, needed to view the function in a clear way
        g2d.fillOval
                ( (int)(x - (size * 0.5 * scaleX))
                        , (int)(y - (size * 0.5 * scaleY))
                        , (int) (size * scaleX)
                        , (int) (size * scaleY));
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

    public void update(List<Collision> boxes){

        Vector v = clamp2(dx,dy,maxSpeed);
        dx = friction(v.x);
        dy = friction(v.y);

        Collision c = new Collision(x + dx, y + dy, size, size);

        if (c.collides(boxes)) {
            //Check horizontal
            c.updatePosition(x+dx, y);

            if (c.collides(boxes)){
                dx=0;
                //Check vertical
                c.updatePosition(x, y+dy);
                if (c.collides(boxes)) {
                    dy = 0;
                }
            }
            else {
                dy = 0;
            }
        }

        y += dy;
        x += dx;
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
}
