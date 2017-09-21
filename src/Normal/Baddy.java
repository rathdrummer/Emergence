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
    private double yOff = height * .8;
    private double throwFriction = 0.05;
    private double normalFriction = 0.4;


    public Baddy(double x, double y) {
        super(new Sprite("baddyAnim", 50,50), x, y);

        //dissappear
        Sprite disappear = new Sprite("baddyDissappear", 50, 50);
        disappear.setImageSpeed(.25);

        disappear.setFrameListenerOn(Sprite.AT_LOOP, () -> {
            invisible = true;
            pauseAnimation();
        });

        //appear
        Sprite appear = disappear.clone(); // does not copy the sprites framelisteners
        appear.reverse();

        appear.setFrameListenerOn(Sprite.AT_LOOP, () -> {
            if (isHealthy()) {
                changeStateTo(State.CHASE);
            }
            else changeStateTo(State.FLEE);

            changeSpriteTo(new AnimationType(AnimationEnum.Normal), true);
        });

        //corpse (dead)
        Sprite dead = new Sprite("baddyCorpse", 50, 50);
        dead.setImageSpeed(0);
        dead.setImageIndex((int) (Math.round( (dead.length() - 1) * (Math.random())))); // random image index


        addSprite(disappear, new AnimationType(AnimationEnum.Dissappear));
        addSprite(appear, new AnimationType(AnimationEnum.Appear));
        addSprite(dead, new AnimationType(AnimationEnum.Dead));
        getSprite().setImageSpeed(.75);


        changeSpriteTo(new AnimationType(AnimationEnum.Appear), true);
        pauseAnimation();

        collision.updateSize(width * 0.75, height * 0.25, true);

        maxSpeed = chaseSpeed;
        acceleration = 1;
        frictionAcc = normalFriction;

        setHP(4);
    }

    private boolean isHealthy() {
        return hp() > 2;
    }

    @Override
    public double yC() {
        return y + yOff;
    }

    @Override
    public List<Drawable> update(List<Thing> things) {

        if (!isAlive()) {
            if (!currentSprite.equals(new AnimationType(AnimationEnum.Dead))) {
                changeSpriteTo(new AnimationType(AnimationEnum.Dead), false); // we do not play the sprite from start
                invisible = false;
                setToCorpseCollisionSize();
                yOff = height * .6;
                setWeight(1);
            }
            else {
                if (owner != null) {
                    x = owner.x();
                    y = owner.y();
                    z = -30;
                    setDrawDepthInFrontOf(owner);
                    collision.updateSize(0,0,false);
                    frictionAcc = throwFriction;
                    dz = 0;
                }
                else {

                    setDrawDepth(0);
                    setToCorpseCollisionSize();
                    updateSpeed();
                    handleCollisions(things, true, true);

                    zGrav();


                    if (z == 0) {
                        frictionAcc = normalFriction;
                    }
                }
            }
        } else {
            behaviour(things);
        }

        updateSpeed();
        handleCollisions(things, true, true);
        return null;
    }

    private void setToCorpseCollisionSize() {
        getCollision().updateSize(width * .75, height * .4, true);
    }

    private void behaviour(List<Thing> things) {
        Thing player = null;
        List<Thing> sight = new ArrayList<>();

        //setting up vision
        for (Thing t : things) {
            if (t instanceof Player) {
                player = t;
            }

            if (!(t instanceof Baddy)) {
                sight.add(t);
            }
        }

        if (player != null) {
            if (canSee(sight, this, player, 250)) {
                if (isHealthy()) {
                    if (state == State.STILL) {
                        changeSpriteTo(new AnimationType(AnimationEnum.Appear), true);
                    }
                } else {
                    if (closestFriend != null && distanceTo(closestFriend) < 400) {
                        changeStateTo(State.GROUP);
                    } else {
                        changeStateTo(State.FLEE);
                    }
                }
                playAnimation();

            } else {
                hide();
            }
        } else {
            hide();
        }

        List<Thing> sameAsMe = new LinkedList<>();
        things.forEach(t -> {
            if (t instanceof Baddy && t != this) {
                Baddy b = (Baddy) t;
                if (b.isHealthy()) {
                    sameAsMe.add(b);
                }
            }
        });

        closestFriend = collision.closestThing(sameAsMe);

        if (state != State.STILL) {
            if (invisible || currentSprite == new AnimationType(AnimationEnum.Dissappear)) {
                invisible = false;
                changeSpriteTo(new AnimationType(AnimationEnum.Appear), true);
            }
        }

        switch (state) {
            case FLEE:
                maxSpeed = fleeSpeed;
                playAnimation();
                if (x > player.x()) {
                    dx += acceleration;
                } else {
                    dx -= acceleration;
                }

                if (y > player.y()) {
                    dy += acceleration;
                } else {
                    dy -= acceleration;
                }
                break;

            case CHASE:
                maxSpeed = chaseSpeed;
                playAnimation();
                if (player != null) {
                    if (x > player.x()) {
                        dx -= acceleration;
                    } else {
                        dx += acceleration;
                    }

                    if (y > player.y()) {
                        dy -= acceleration;
                    } else {
                        dy += acceleration;
                    }
                }

                break;

            case GROUP:

                if (closestFriend != null) {
                    dx += Math.signum(closestFriend.xC() - xC()) * acceleration;
                    dy += Math.signum(closestFriend.yC() - yC()) * acceleration;
                } else {
                    System.err.println("how did we get here");
                }

                break;

            default:
                //no code needed just now
        }
    }

    private State getState() {
        return state;
    }

    private void changeStateTo(State state) {
        if (state != this.state) {
            System.out.println(this.state + " -> " + state);
            this.state = state;
        }
    }


    private void hide() {
        if (state != State.STILL) {
            changeStateTo(State.STILL);
            changeSpriteTo(new AnimationType(AnimationEnum.Dissappear), true);
        }
    }

    protected double distanceTo(Thing thing) {
        return Math.sqrt(Math.pow(xC() - thing.xC(), 2) + Math.pow(yC() - thing.yC(), 2));
    }

    private enum State{
        CHASE, STILL, FLEE, GROUP
    }
}
