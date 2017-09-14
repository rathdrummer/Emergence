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


    public static Collision speedBox(double xCenter, double yCenter, double width, double height, double dx, double dy) {
        double transX = (dx > 0) ? 0 : dx;
        double transY = (dy > 0) ? 0 : dy;

        Collision collision =  new Collision(xCenter + transX, yCenter + transY, width, height);
        //we split this so that the collision knows where the center is based on the original width and height
        collision.updateSize(width + Math.abs(dx), height + Math.abs(dy), false);

        return collision;
    }

    public Collision(int xCenter, int yCenter, int width, int height) {
        this.x = xCenter - width/2;
        this.y = yCenter - height/2;
        this.width = width;
        this.height = height;
    }

    public Collision(double xCenter, double yCenter, double width, double height) {
        this((int) xCenter, (int) yCenter, (int) width, (int) height);
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

    public boolean collides (Thing thing) {

        return this.collides(thing.getCollision());
    }

    public boolean collides(List<Collision> collisions) {
        for (Collision c : collisions) {
            if (this.collides(c)) {
                return true;
            }
        }
        return false;
    }

    public boolean collidesThing(List<Thing> things, Thing me) {

        for (Thing t : things) {
            if ((t != me) && this.collides(t)) {
                return true;
            }
        }

        return false;
    }

    public List<Thing> collidesWithThing(List<Thing> things, Thing me) {
        List<Thing> thoseWhoCollides = new ArrayList<>();

        for (Thing t : things) {

            if (t != me && this.collides(t.getCollision())) {
                thoseWhoCollides.add(t);
            }
        }

        return thoseWhoCollides;
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

    public List<Thing> collidesWithThing(List<Thing> things, Class cl, Thing me) {
        List<Thing> thoseWhoCollides = new ArrayList<>();

        for (Thing t : things) {

            if (this.collides(t.getCollision()) && (t != me)) {

                if (t.getClass() == cl) {
                    thoseWhoCollides.add(t);
                }
            }
        }

        return thoseWhoCollides;
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

    public List<Collision> collidesWith(List<Collision> collisions, Class cl) {
        List<Collision> thoseWhoCollides = new ArrayList<>();

        collisions.forEach(c -> {
            if (collides(c)){
                if (c.getClass() == cl) {
                    thoseWhoCollides.add(c);
                }
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

    public Thing closestThing(List<Thing> things) {
        double closestDistance = 0;
        Thing closest = null;

        for(Thing t : things) {

            double distance = distanceThing(t);

            if (closest == null) {
                closest = t;
                closestDistance = distance;
            }
            else {
                if (distance < closestDistance){
                    closest = t;
                    closestDistance = distance;
                }
            }
        }

        return closest;
    }

    public double distanceThing(Thing t) {
        return distance(t.getCollision());
    }

    public double distance(Collision c) {
        return Math.sqrt( Math.pow(c.x - x, 2) + Math.pow(c.y - y, 2) );
    }

    public void updatePosition(double xCenter, double yCenter) {
        this.x = (int) xCenter - width / 2;
        this.y = (int) yCenter - height / 2;
    }

    public void updateSize(double width, double height, boolean reCenter) {
        int oldWidth = this.width;
        int oldHeight = this.height;

        this.width = (int) width;
        this.height = (int) height;

        if (reCenter) {
            this.x += oldWidth - width;
            this.y += oldHeight - height;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Collision collision = (Collision) o;

        if (getX() != collision.getX()) return false;
        if (getY() != collision.getY()) return false;
        if (getWidth() != collision.getWidth()) return false;
        return getHeight() == collision.getHeight();

    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        result = 31 * result + getWidth();
        result = 31 * result + getHeight();
        return result;
    }
}
