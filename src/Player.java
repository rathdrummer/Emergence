import javax.swing.*;
import java.awt.*;

/**
 * Represents the player
 */
public class Player implements Drawable {
    public double x ;
    public double y ;
    public double dx;
    public double dy;
    public double acceleration;
    public double maxSpeed;

    private double frictionAcc;

    private Image image;
    private double transX;
    private double transY;

    public Player(String filename){
        image = Img.loadImage(filename);
        x = 0;
        y = 0;
        dx = 0;
        dy = 0;
        acceleration = 1;
        maxSpeed = 7;
        frictionAcc = 0.5;
    }

    public void paint(Graphics g){
        //super.paint(g);

        //g.drawImage(Img.loadImage("circle.jpg"), (int) (x - transX / 2), (int) (y - transY / 2), (int) (30 + transX), (int) (30 + transY), null);

        /*
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.fillOval((int)x - (int) (transX/2), (int)y - (int) (transY/2), 30 + (int) transX, 30 + (int) transY);
        */

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

    public void update( ){

        transX =  (20* (Math.abs(dx)/maxSpeed));
        transY = (20* (Math.abs(dy)/maxSpeed));

        if (transX > 0 || transY > 0) {
            if (transX > transY) {
                transY -= transX;
            } else {
                transX -= transY;
            }
        }

        Vector v = clamp2(dx,dy,maxSpeed);
        dx = friction(v.x);
        dy = friction(v.y);

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

    @Override
    public double height() {
        return image.getHeight(null)-(int)transX;
    }

    @Override
    public double width() {
        return image.getWidth(null)-(int)transY;
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
        return x+(double)(image.getHeight(null))/2;
    }

    @Override
    public double yC() {
        return y+(double)(image.getWidth(null))/2;
    }

    @Override
    public Image getImage() {
        return image;
    }
}
