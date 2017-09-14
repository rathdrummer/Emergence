package Tests;

import Normal.Library;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;

import Normal.Player;


/**
 * Created by oskar on 2017-09-14.
 * This classes has some inputs and outputs
 */
public class PlayerTest {

    @Test
    public void collisionBoxInRightPlace() {
        Player player = new Player(100, 200);

        System.out.println("collision x: " + player.getCollision().getX() + ", player x: " + player.x());

        Assert.assertTrue(player.getCollision().getX() == player.x());

    }

}
