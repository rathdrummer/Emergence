package Tests;

import Normal.Collision;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oskar on 2017-09-12.
 * This classes has some inputs and outputs
 */
public class CollisionTest {

     @Test
     public void normalCollisions() {
         Collision first  = new Collision(0,0,30,30);
         Collision second = new Collision(20, 20, 30, 30);
         Collision third  = new Collision(40, 40, 30, 30);

         Assert.assertTrue(first.collides(second));
         Assert.assertTrue(second.collides(third));
         Assert.assertFalse(first.collides(third));
     }

    @Test
    public void centeredCorrectly() {
        Collision test = new Collision(10, 10, 20, 20);

        Assert.assertTrue(test.getX() == 0);
        Assert.assertTrue(test.getY() == 0);
        Assert.assertTrue(test.getWidth() == 20);
        Assert.assertTrue(test.getHeight() == 20);
    }

    @Test
    public void constructorsWorksTheSame() {
        Collision doubleConstructor = new Collision((double) 10, (double) 10, (double) 20, (double) 20);
        Collision intConstructor = new Collision(10, 10, 20, 20);

        Assert.assertTrue(doubleConstructor.equals(intConstructor));
    }

    @Test
    public void speedBoxTest() {
        Collision speedBox = Collision.speedBox(10, 12, 20, 24, 2, 4);
        Assert.assertTrue(speedBox.getX() == 0);
        Assert.assertTrue(speedBox.getY() == 0);
        Assert.assertTrue(speedBox.getWidth() == 22);
        Assert.assertTrue(speedBox.getHeight() == 28);
    }

    @Test
    public void negativeSpeedBoxTest() {
        Collision speedBox = Collision.speedBox(10, 12, 20, 24, -2, -4);
        Assert.assertTrue(speedBox.getX() == -2);
        Assert.assertTrue(speedBox.getY() == -4);
        Assert.assertTrue(speedBox.getWidth() == 22);
        Assert.assertTrue(speedBox.getHeight() == 28);
    }

    @Test
    public void stackedItemsDoNotCollide() {
        Collision first  = new Collision(0,0,30,30);
        Collision second = new Collision(30, 0, 30, 30);
        Collision third  = new Collision(0, 30, 30, 30);
        Collision forth  = new Collision(30, 30, 30, 30);

        Assert.assertFalse(first.collides(second));
        Assert.assertFalse(first.collides(third));
        Assert.assertFalse(first.collides(forth));
    }

    @Test
    public void findsClass() {
        Collision first = new Collision(0,0,30,30);
        Collision second = new Collision(15,15,30,30);

        List<Collision> list = new ArrayList<>();
        list.add(second);

        Assert.assertFalse(first.collidesWith(list, Collision.class).isEmpty());

    }



    @Test
    public void collidesWith() {
        Collision first  = new Collision(0,0,30,30);
        Collision shouldCollide = new Collision(20, 20, 30, 30);
        Collision shouldCollide2 = new Collision(0, 20, 30, 30);
        Collision shouldCollide3 = new Collision(20, 0, 30, 30);
        Collision shouldNOT  = new Collision(40, 40, 30, 30);

        List<Collision> asList = new ArrayList<>();

        asList.add(shouldCollide);
        asList.add(shouldCollide2);
        asList.add(shouldCollide3);
        asList.add(shouldNOT);

        List<Collision> overlaps = first.collidesWith(asList);

        Assert.assertTrue(overlaps.contains(shouldCollide));
        Assert.assertTrue(overlaps.contains(shouldCollide2));
        Assert.assertTrue(overlaps.contains(shouldCollide3));
        Assert.assertFalse(overlaps.contains(shouldNOT));

    }

    @Test
    public void correctlyRealizesClosest() {
        Collision first  = new Collision(0,0,30,30);
        Collision second = new Collision(16, 16, 30, 30);
        Collision closest = new Collision(0, 17, 30, 30);
        Collision third = new Collision(18, 0, 30, 30);
        Collision forth  = new Collision(40, 40, 30, 30);

        List<Collision> asList = new ArrayList<>();

        asList.add(second);
        asList.add(closest);
        asList.add(third);
        asList.add(forth);

        Collision regardedAsClosest = first.closest(asList);

        Assert.assertTrue(regardedAsClosest == closest);
    }

    @Test
    public void calculatesClosest() {
        Collision first  = new Collision(0,0,30,30);
        Collision second = new Collision(3, 4, 30, 30);

        Assert.assertTrue(first.distance(second) == 5);
    }

}