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
    }

    @Override
    public void update(List<Thing> list) {
        for (Thing t : list){
            if (t instanceof Player){
                xGoal = t.xC();
                yGoal = t.yC();
            }
        }

        // set dx and dy to move to player - will need cos and sin maybe?

    }
}
