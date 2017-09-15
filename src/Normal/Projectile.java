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
    public List<Drawable> update(List<Thing> list) {
        x +=dx;
        y +=dy;

        List<Drawable> newList = new ArrayList<>();

        for (Thing other : list){
            if (this.collision.collides(other) && other != this.owner && (this.getClass() != other.getClass())){
                other.harm(3, new Vector(dx, dy));
                Camera.shake(15);
                Sound.play("bom");
                this.remove();

                // Add particles
                newList.addAll(Particle.spawn(20, (int) x, (int) y, Library.loadImage("circle")));

            }
        }

        getCollision().updatePosition(x,y);

        return newList;
    }
}
