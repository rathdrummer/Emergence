package Normal;

import java.awt.*;

/**
 * Created by oskar on 2017-09-12.
 * This classes has some inputs and outputs
 */
public class Box extends Collision{


    public Box(int x, int y, int width, int height) {
        super(x, y, width, height);
    }


    public void draw(Graphics2D g2d) {
        g2d.fillRect(getX() - getWidth()/2, getY() - getHeight()/2, getWidth(), getHeight());
    }

}
