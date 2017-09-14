package Normal;

import java.awt.*;

public abstract class Drawable {


    public double x;
    public double y;
    public double width;
    public double height;
    public Image image;

    boolean remove; // false by default!


    public double x(){return x;}
    public double y() {return y;}

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

    public double width(){return width;}
    public double height(){return height;}

    public Image getImage() {return image;}

    public void remove(){this.remove = true;}
}
