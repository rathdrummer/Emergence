package Normal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by oskar on 2017-09-15.
 * This classes has some inputs and outputs
 */
public class Baddy extends Thing{
    private State state = State.STILL;

    private final int fleeSpeed = 6;
    private final int chaseSpeed = 4;
    private Thing closestFriend = null;


    public Baddy(double x, double y) {
        super(new Sprite("baddyAnim", 28,43), x, y);
        getSprite().setImageSpeed(.75);

        getCollision().updateSize(width, height * .25, true);

        maxSpeed = chaseSpeed;
        acceleration = 1;
        frictionAcc = .5;
    }

    @Override
    public double yC() {
        return y + height * .75;
    }

    @Override
    public List<Drawable> update(List<Thing> things) {
        Thing player = null;

        for (Thing t : things) {
            if (t instanceof Player) {
                player = t;
                break;
            }
        }

        if (player != null) {
            if (canSee(things, this, player, 250)) {
                if (hp() > 2) {
                    state = State.CHASE;
                }
                else {
                    if (closestFriend != null && distanceTo(closestFriend) < 400) {
                        state = State.GROUP;
                    }
                    else {
                        state = State.FLEE;
                    }
                }
            }
            else state = State.STILL;
        }
        else {
            state = State.STILL;
        }

        List<Thing> sameAsMe = new LinkedList<>();
        things.forEach(t -> {
            if (t instanceof Baddy) {
                sameAsMe.add(t);
            }
        });

        closestFriend = collision.closestThing(sameAsMe);


        switch (state) {
            case FLEE:
                maxSpeed = fleeSpeed;
                playAnimation();
                if (x > player.x()) {
                    dx += acceleration;
                }
                else {
                    dx -= acceleration;
                }

                if (y > player.y()) {
                    dy += acceleration;
                }
                else {
                    dy -= acceleration;
                }
                break;

            case CHASE:
                maxSpeed = chaseSpeed;
                playAnimation();
                if (x > player.x()) {
                    dx -= acceleration;
                }
                else {
                    dx += acceleration;
                }

                if (y > player.y()) {
                    dy -= acceleration;
                }
                else {
                    dy += acceleration;
                }
                break;

            case GROUP:

                if (closestFriend != null) {
                    dx += Math.signum(closestFriend.xC() - xC()) * acceleration;
                    dy += Math.signum(closestFriend.yC() - yC()) * acceleration;
                }
                else {
                    System.err.println("how did we get here");
                }

                break;

            case STILL: default:
                pauseAnimation();
                resetAnimation();
            /* Otherwise we do not do anything and the character will stop by itselfs */
        }

        updateSpeed();
        handleCollisions(things, true, true);
        return null;
    }

    protected double distanceTo(Thing thing) {
        return Math.sqrt(Math.pow(xC() - thing.xC(), 2) + Math.pow(yC() - thing.yC(), 2));
    }

    private enum State{
        CHASE, STILL, FLEE, GROUP
    }
}
