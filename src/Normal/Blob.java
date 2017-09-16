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
        Sprite spr = new Sprite("blobAnimation", 50,50);
        setSprite(spr, new AnimationType(AnimationEnum.Normal));
        this.x = x;
        this.y = y;
        collision.updateSize(getImage().getWidth(null),getImage().getHeight(null), false);
        this.width = getImage().getWidth(null);
        this.height = getImage().getHeight(null);

        this.acceleration = .1;
        this.frictionAcc = 0.02;
        this.maxSpeed = 9;

        collision.updateCenter(xC(), yC());
        collision.updateSize(width*.65, height*.25, true);
    }

    @Override
    public double yC() {
        return y + (height * 0.7);
    }

    @Override
    public List<Drawable> update(List<Thing> things) {
        for (Thing t : things){
            if (t instanceof Player){
                xGoal = t.xC();
                yGoal = t.yC();
                break;
            }
        }


        double dxDirection = Math.signum(xGoal - xC());
        double dyDirection = Math.signum(yGoal - yC());


        // set dx and dy to move to player - will need cos and sin maybe?

        dx += dxDirection * acceleration;
        dy += dyDirection * acceleration;


        updateSpeed();

        handleCollisions(things, true, true);
        return  null;
    }
}
