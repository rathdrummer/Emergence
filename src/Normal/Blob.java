package Normal;

import java.util.List;

/**
 * An extremely basic enemy.
 */
public class Blob extends Thing{

    private double xGoal;
    private double yGoal;

    public Blob() {
        this.setImage(Img.loadImage("blob"));
        this.x = 500;
        this.y = 500;
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
