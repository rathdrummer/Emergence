package Normal;


import java.util.ArrayList;
import java.util.List;


/**
 * Represents the player
 */
public class Player extends Thing{

    private double acceleration;
    private double maxSpeed;

    private double scaleX;
    private double scaleY;
    private boolean shoot;
    private Vector direction = new Vector(0,0);
    private boolean shootIsPressed = false;

    public Player(double x, double y){
        super(new Collision(x, y, 0, 0));

        /*add sprites to player*/

        //only on image, so we split it up after we load it into one sprite
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
        scaleX = 1;
        scaleY = 1;
        height = getImage().getHeight(null);
        width = getImage().getWidth(null);
        collision.updateSize(width,height, true);
        collision.updatePosition(xC(), yC());
    }


    public void input(boolean right, boolean up, boolean left, boolean down, boolean shoot) {
        if (right) incrementDX(true);
        if (up) incrementDY(false);
        if (left) incrementDX(false);
        if (down) incrementDY(true);

        this.shoot = false;
        if (shoot) {
            if (!this.shootIsPressed){
                this.shootIsPressed = true;
                this.shoot = true;
            }

        }
        else {
            this.shootIsPressed = false;
        }
    }


    @Override
    public List<Thing> update(List<Thing> things){

        double range = 0.1;

        scaleX = (1 - range/2) + range * ( (Math.abs(dx)/maxSpeed) - (Math.abs(dy)/maxSpeed) );
        scaleY = (1 - range/2) + range * ( (Math.abs(dy)/maxSpeed) - (Math.abs(dx)/maxSpeed) );

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
                    changeSpriteTo(new AnimationType(walk, DirectionEnum.Right));
                }else {
                    changeSpriteTo(new AnimationType(walk, DirectionEnum.Left));
                }
            }
        } else {
            if (Math.abs(dy) > 0.25) {

                stopAnimation = false;

                if (dy > 0) {
                    changeSpriteTo(new AnimationType(walk, DirectionEnum.Down));
                }else {
                    changeSpriteTo(new AnimationType(walk, DirectionEnum.Up));
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

        ArrayList<Thing> newThings = new ArrayList<>();

        Projectile orb = new Projectile(xC(), yC(),new Sprite("orb"), direction.getAngle(),10);
        orb.setPosition(orb.centreOnLeftCorner());
        orb.setOwner(this);

        if (shoot){
            newThings.add(orb);
        }

        return newThings;
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
