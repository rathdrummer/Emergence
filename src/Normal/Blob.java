package Normal;

import java.util.List;

/**
 * An extremely basic enemy.
 */
public class Blob extends Thing{

    private double xGoal;
    private double yGoal;

    public Blob(double x, double y) {
        super(new Collision(x, y, 50,50));
        setSprite(new Sprite("blobAnimation", 50), new AnimationType(AnimationEnum.Normal));
        this.x = x;
        this.y = y;
        collision.updateSize(getImage().getWidth(null),getImage().getHeight(null), false);
        this.width = getImage().getWidth(null);
        this.height = getImage().getHeight(null);

        this.acceleration = .1;
        this.frictionAcc = 0.02;
        this.maxSpeed = 6;

        collision.updatePosition(xC(), yC());
    }

    @Override
    public List<Thing> update(List<Thing> things) {
        for (Thing t : things){
            if (t instanceof Player){
                xGoal = t.xC();
                yGoal = t.yC();
            }
        }


        double dxDirection = Math.signum(xGoal - xC());
        double dyDirection = Math.signum(yGoal - yC());


        // set dx and dy to move to player - will need cos and sin maybe?

        dx += dxDirection * acceleration;
        dy += dyDirection * acceleration;


        updateSpeed();

        handleCollisions(things, true, false);
        return  null;
    }
}
