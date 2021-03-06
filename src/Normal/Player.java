package Normal;


import java.util.ArrayList;
import java.util.List;


/**
 * Represents the player
 */
public class Player extends Thing{

    private double acceleration;
    private double maxSpeed;
    private boolean shoot;
    private boolean pickUpOrThrow =false;

    public Player(double x, double y){
        super(new Collision(x, y, 0, 0));

        /*add sprites to player*/

        //here is how you would load and then tint a sprite
        //BufferedImage image = Library.opacity(Library.loadImage("person"), .5);

        Sprite all = new Sprite("person", 50, 50);
        all.setImageSpeed(.25); // this image speed will be set for those who take parts of it


        Sprite down = all.getPartOf(0, 4);
        Sprite right = all.getPartOf(4, 4);
        Sprite up = all.getPartOf(8, 4);
        Sprite left = all.getPartOf(12, 4);

        addSprite(down, new AnimationType(AnimationEnum.Walk, DirectionEnum.Down));
        addSprite(right, new AnimationType(AnimationEnum.Walk, DirectionEnum.Right));
        addSprite(up, new AnimationType(AnimationEnum.Walk, DirectionEnum.Up));
        addSprite(left, new AnimationType(AnimationEnum.Walk, DirectionEnum.Left));

        addSprite(all, new AnimationType(AnimationEnum.Normal));

        playAnimation(); //we do not want to play them from the start

        this.x = x;
        this.y = y;
        dx = 0;
        dy = 0;
        acceleration = 1;
        maxSpeed = 7;
        height = getImage().getHeight(null);
        width = getImage().getWidth(null);
        collision.updateSize(width*0.3,height*.25, true);
        collision.updateCenter(xC(), yC());
    }


    public void input(boolean right, boolean up, boolean left, boolean down, boolean shootPressed, boolean pickUpPressed) {
        if (right) incrementDX(true);
        if (up) incrementDY(false);
        if (left) incrementDX(false);
        if (down) incrementDY(true);

        this.pickUpOrThrow = pickUpPressed;

        this.shoot = shootPressed;

    }

    @Override
    public double xC() {
        return super.xC() - 2;
    }

    //centering sprite and collision on feet
    @Override
    public double yC() {
        return y + (height * .65);
    }

    @Override
    public List<Drawable> update(List<Thing> things){



        //squish and stretch
        //double range = 0.1;
        //scaleX = (1 - range/2) + range * ( (Math.abs(dx)/maxSpeed) - (Math.abs(dy)/maxSpeed) );
        //scaleY = (1 - range/2) + range * ( (Math.abs(dy)/maxSpeed) - (Math.abs(dx)/maxSpeed) );

        AnimationEnum walk = AnimationEnum.Walk;

        /*
            Setting the sprite to be the largest value of speed,
            ex) if we are moving mostly right but a little up we choose right
        */

        boolean stopAnimation = true;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (Math.abs(dx) > 0.25) {

                stopAnimation = false;

                if (dx > 0) {
                    changeSpriteTo(new AnimationType(walk, DirectionEnum.Right), true);
                }else {
                    changeSpriteTo(new AnimationType(walk, DirectionEnum.Left), true);
                }
            }
        } else {
            if (Math.abs(dy) > 0.25) {

                stopAnimation = false;

                if (dy > 0) {
                    changeSpriteTo(new AnimationType(walk, DirectionEnum.Down), true);
                }else {
                    changeSpriteTo(new AnimationType(walk, DirectionEnum.Up), true);
                }
            }
        }


        if (stopAnimation) {
            pauseAnimation();
        }
        else {
            playAnimation();
            getSprite().setImageSpeed( 0.25 * Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)) / maxSpeed );
        }

        updateSpeed();

        if (Math.abs(dx) +  Math.abs(dy) > 0) {
            direction = new Vector(dx, dy);
        }

        handleCollisions(things, false, true);

        ArrayList<Drawable> newThings = new ArrayList<>();


        if (shoot){
            Projectile orb = new Projectile(xC(), yC(),new Sprite("orb"), direction.getAngle(),10);
            orb.setPosition(orb.centerOnLeftCorner());
            orb.setOwner(this);
            newThings.add(orb);
        }

        if (pickUpOrThrow) {
            if (heldItem == null) pickUp(things);
            else {
                throwIt();
            }
        }

        return newThings;
    }

    private void throwIt() {
        heldItem.resetSpeed(); // TODO: do not like this solution, somehow the bush gets to much speed
        heldItem.applyForce(Vector.getComponents(8, direction.getAngle()));

        releaseHeldItem();
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

}
