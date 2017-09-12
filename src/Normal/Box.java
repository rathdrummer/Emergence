package Normal;

import java.awt.*;

/**
 * Created by oskar on 2017-09-12.
 * This classes has some inputs and outputs
 */
public class Box extends Collision implements Drawable{

    private Image img;

    public Box(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.img = Img.loadImage("box.png");
    }

    @Override
    public double x() {
        return super.getX();
    }

    @Override
    public double y() {
        return super.getY();
    }

    @Override
    public double width() {
        return super.getWidth();
    }

    @Override
    public double height() {
        return super.getHeight();
    }

    @Override
    public Image getImage() {
        return img;
    }
}