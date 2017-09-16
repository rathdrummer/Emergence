package Normal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oskar on 2017-09-15.
 * This classes has some inputs and outputs
 */
public class CollisionLine {

    private double x;
    private double y;
    private Vector vector;
    private List<Thing> ignore = new ArrayList<>();

    /*inverse of fidelity, basically how far we jump between collision-check points (imagine we travel the line looking for collisions)
    * high numbers increase the chance of missing a collision but improves performance in magnitude of O(n)*/
    private int jump = 4;

    public CollisionLine(double x, double y, Vector vector) {
        this.x = x;
        this.y = y;
        this.vector = vector;
    }

    public CollisionLine(double x, double y, double goalX, double goalY) {
        this(x, y, new Vector(goalX - x, goalY -y));
    }

    public CollisionLine(double x, double y, double goalX, double goalY, Thing ignore) {
        this(x,y,goalX,goalY);
        addToIgnore(ignore);
    }

    public CollisionLine(double x, double y, Vector vector, Thing ignore) {
        this(x,y,vector);
        addToIgnore(ignore);
    }

    public void addToIgnore(Thing thing) {
        ignore.add(thing);
    }

    public boolean collides(Collision collision) { // TODO: This is a naive implementation, there is probably some linear algebra we could do
        Vector part;
        for (int i = jump; i < vector.getLength(); i += jump) {
            part = Vector.getComponents(i, vector.getAngle()).add(new Vector(x, y));

            if (part.x > collision.getX() && part.x < collision.getX() + collision.getWidth()) {
                if (part.y > collision.getY() && part.y < collision.getY() + collision.getHeight()) {
                    return true;
                }
            }
        }

        //if we have not find anything
        return false;
    }

    public List<Thing> collidesWith(List<Thing> things) {
        List<Thing> collisions = new ArrayList<>();

        things.forEach(t -> {
            if (ignore.contains(t) || !collides(t.getCollision())) {
                collisions.add(t);
            }
        });

        return collisions;
    }

    public List<Thing> collidesWith(List<Thing> things, boolean onlySolids) {
        List<Thing> collisions = new ArrayList<>();

        things.forEach(t -> {
            if (onlySolids || !t.isSolid()) {
                if (ignore.contains(t) || !collides(t.getCollision())) {
                    collisions.add(t);
                }
            }
        });

        return collisions;
    }


    public boolean collides(List<Thing> things) {
        for(Thing t : things) {
            if (!ignore.contains(t) && collides(t.getCollision())) {
                return true;
            }
        }
        return false;
    }

    public void setLength(int length) {
        this.vector = Vector.getComponents(length, vector.getAngle());
    }

    @Override
    public String toString() {
        return "(x,y): " + Integer.toString((int) x) + ", " + Integer.toString((int) y) + ". (dx,dy): " + Integer.toString((int) vector.x) + ", " + Integer.toString((int) vector.y);
    }


    public int getLength() {
        return (int) vector.getLength();
    }
}
