package Tests;

import Normal.Sprite;
import org.junit.Assert;
import org.junit.Test;


/**
 * Created by oskar on 2017-09-13.
 * This classes has some inputs and outputs
 */
public class SpriteTest {

    @Test
    public void updateWorks() {
        Sprite spr = new Sprite("person", 50, 50);
        spr.setImageSpeed(.5);
        Sprite down = spr.getPartOf(0, 4);
        down.setImageSpeed(.25);

        for(int i = 0; i < 4; i++){
            spr.update();
            down.update();
        }

        Assert.assertTrue(spr.getImageIndex() == 2);
        Assert.assertTrue(down.getImageIndex() == 1);
    }

    @Test
    public void isItReversing() {
        Sprite spr = new Sprite("baddyDissappear", 50, 50);
        Sprite clone = spr.clone();
        clone.reverse();

        Assert.assertFalse(spr.getImage(0) == clone.getImage(0));
        Assert.assertTrue(spr.getImage(0) == clone.getImage(clone.length()-1));
    }

}