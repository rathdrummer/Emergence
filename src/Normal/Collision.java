package Normal;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by oskar on 2017-09-12.
 * This classes is meant to represent hit boxes, in this implementation you CANNOT change the size of the collision, my
 * idea is that if you want collisions of different sizes we simply have several objects.
 */
public class Collision {
    private int x;
    private int y;
    private int width;
    private int height;

    public Collision() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public Collision(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Collision(double x, double y, double width, double height) {
        this((int) x, (int) y, (int) width, (int) height);
    }

    public void updatePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean collides(Collision other) {

        if (other == this) return false;

        boolean xOverlap = (Math.max(x + width, other.x + other.width) - Math.min(x, other.x) < width + other.width);
        boolean yOverlap = (Math.max(y + height, other.y + other.height) - Math.min(y, other.y) < height + other.height);

        return (xOverlap && yOverlap);
    }


    public boolean collides(List<Collision> collisions) {

        for (Collision c : collisions) {
            if (collides(c)) {
                return true;
            }
        }

        return false;
    }

    /***
     * Sometimes we want to know who we are colliding with
     * @param collisions: All possible collisions
     * @return a list of the collisions that collides with this collision
     */
    public List<Collision> collidesWith(List<Collision> collisions) {
        List<Collision> thoseWhoCollides = new ArrayList<>();

        collisions.forEach(c -> {
            if (collides(c)){
                thoseWhoCollides.add(c);
            }
        });

        return thoseWhoCollides;
    }

    public Collision closest(List<Collision> collisions) {
        double closestDistance = 0;
        Collision closest = null;

        for(Collision c : collisions) {

            double distance = distance(c);

            if (closest == null) {
                closest = c;
                closestDistance = distance;
            }
            else {
                if (distance < closestDistance){
                    closest = c;
                    closestDistance = distance;
                }
            }
        }

        return closest;
    }

    public double distance(Collision c) {
        return Math.sqrt( Math.pow(c.x - x, 2) + Math.pow(c.y - y, 2) );
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void updatePosition(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
