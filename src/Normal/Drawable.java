package Normal;

import java.awt.*;

public abstract class Drawable {


    protected double x;
    protected double y;
    protected double z = 0;
    protected double width;
    protected double height;
    protected Image image;
    private int drawDepth = 0;

    boolean remove; // false by default!


    public double x(){return x;}
    public double y() {return y;}
    public double z() {return z;}



    public void setDrawDepth(int drawDepth) {
        this.drawDepth = drawDepth;
    }

    public int getDrawDepth() {
        return drawDepth;
    }

    public void setDrawDepthBehind(Thing thing) {
        this.drawDepth = thing.drawDepth + 1;
    }

    public void setDrawDepthInFrontOf(Thing thing) {
        this.drawDepth = thing.drawDepth - 1;
    }

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
