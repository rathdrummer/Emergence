package Normal;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oskar on 2017-09-12.
 * This classes has some inputs and outputs
 */
public class Box extends Thing {

    public Box(int x, int y) {
        super(new Collision(x, y, 0, 0));
        BufferedImage img = Library.loadTile("static", 0, 0, 50, 50);
        setSprite(img, new AnimationType(AnimationEnum.Normal));

        this.x = x;
        this.y = y - 20;
        this.width = img.getWidth();
        this.height = img.getHeight();
        collision.updateSize(width, height, false);
        collision.updatePosition(xC(), yC());
    }

    @Override
    public List<Thing> update(List<Thing> list) {
        return null;
    }
}
