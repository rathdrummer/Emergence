package Normal;

import java.awt.*;

public abstract class Drawable {



    boolean remove; // false by default!


    public abstract double x();
    public abstract double y();

    public boolean toRemove() {return remove;}

    /**
     * Get X in center of object
     */
    public double xC(){
        return x()+width()/2;
    };

    /**
     * Get Y in center of object
     */
    public double yC(){
        return y()+height()/2;
    };

    public abstract double width();
    public abstract double height();

    public abstract Image getImage();

    public void remove(){this.remove = true;}
}
