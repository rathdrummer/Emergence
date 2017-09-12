package Normal;

import java.util.List;

/**
 * An extremely basic enemy.
 */
public class Blob extends Thing{

    private double xGoal;
    private double yGoal;

    public Blob(double x, double y) {
        super(new Collision(x, y, 0,0));
        this.setImage(Img.loadImage("blob"));
        this.x = x;
        this.y = y;
        collision.updateSize(image.getWidth(null),image.getHeight(null));
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);

        this.acceleration = .25;
        this.frictionAcc = .1;
        this.maxSpeed = 12;
    }

    @Override
    public void update(List<Thing> things) {
        for (Thing t : things){
            if (t instanceof Player){
                xGoal = t.xC();
                yGoal = t.yC();
            }
        }


        // set dx and dy to move to player - will need cos and sin maybe?

        dx += Math.signum(xGoal - xC()) * acceleration;
        dy += Math.signum(yGoal - yC()) * acceleration;



        updateSpeed();

        handleCollisions(things);
    }
}
