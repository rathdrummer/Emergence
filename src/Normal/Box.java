package Normal;

import java.awt.*;
import java.util.List;

/**
 * Created by oskar on 2017-09-12.
 * This classes has some inputs and outputs
 */
public class Box extends Thing {

    public Box(int x, int y, int width, int height) {
        super(new Collision(x, y, width, height));
        this.image = Img.loadImage("box");

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void update(List<Thing> list) {

    }
}
