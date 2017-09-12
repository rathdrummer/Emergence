package Normal;

import Normal.Drawable;
import Normal.Img;

import java.awt.*;

/**
 * A static image that can be drawn as a background area. Renamed from Background.
 */
public class Area implements Drawable {

    private double x;
    private double y;
    private Image i;


    public Area(String filename){
        //Load image
        i = Img.loadImage(filename);
        x=0;
        y=0;
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double xC() {
        return x+(double)i.getHeight(null);
    }

    @Override
    public double yC() {
        return y+(double)i.getWidth(null);
    }

    @Override
    public double width() {
        return i.getWidth(null);
    }

    @Override
    public double height() {
        return i.getHeight(null);
    }

    @Override
    public Image getImage() {
        return i;
    }


}
