package Normal;

import java.util.ArrayList;
import java.util.List;

/**
 * Projectile. Should be abstract maybe.
 */
public class Projectile extends Thing{


    public Projectile(double x, double y, Sprite sprite){
        super(sprite,x,y);
    }

    public Projectile(double x, double y, Sprite sprite, double angle, double speed){
        this(x,y,sprite, Vector.getComponents(speed, angle));

    }
    
    public Projectile(double x, double y, Sprite sprite, Vector velocity){
        this(x,y,sprite);
        setSpeeds(velocity);
    }

    @Override
    public List<Thing> update(List<Thing> list) {
        x +=dx;
        y +=dy;
        return new ArrayList<>();
    }
}
