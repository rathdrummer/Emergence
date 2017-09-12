package Normal;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by oskar on 2017-09-12.
 * This classes has some inputs and outputs
 */
public class Box extends Thing implements Drawable{

    private Image img;

    public Box(int x, int y, int width, int height) {
        super(new Collision(x, y, width, height));
        this.img = Img.loadImage("box.png");
    }


    @Override
    public Image getImage() {
        return img;
    }

    @Override
    public void update(List<Thing> list) {

    }
}
