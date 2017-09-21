package Normal;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by oskar on 2017-09-12.
 * This classes has some inputs and outputs
 */
public class Bush extends Thing {


    private final double normalFriction = 8;
    private final double throwFriction = .4;


    public Bush(int x, int y) {
        super(new Collision(x, y, 0, 0));
        BufferedImage img = Library.loadTile("static", 1, 50, 50);
        setSprite(img, new AnimationType(AnimationEnum.Normal));

        this.x = x;
        this.y = y - 20;
        this.width = img.getWidth();
        this.height = img.getHeight();

        setToNormalCollisionSize();
        collision.updateCenter(xC(), yC());

        weight = 1;
        frictionAcc = normalFriction;
        maxSpeed = 100;
    }

    public void setToNormalCollisionSize() {
        collision.updateSize(width*.8, height*.4, true);
    }


    @Override
    public List<Drawable> update(List<Thing> things) {
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
            setToNormalCollisionSize();
            updateSpeed();
            handleCollisions(things, true, true);

            zGrav();


            if (z == 0) {
                frictionAcc = normalFriction;
            }
        }


        x+=dx;
        y+=dy;
        collision.updateCenter(xC(), yC());
        return null;
    }



    @Override
    public double yC() {
        return y + (height * .75);
    }
}
