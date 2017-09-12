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
    public void isPositionUpdating() {
        Collision first  = new Collision(0,0,30,30);

        Assert.assertTrue(first.getX() == 0 && first.getY() == 0 && first.getWidth() == 30 && first.getHeight() == 30);

        first.updatePosition(15, 5);

        Assert.assertTrue(first.getX() == 15 && first.getY() == 5 && first.getWidth() == 30 && first.getHeight() == 30);
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
    public void calulatesClosest() {
        Collision first  = new Collision(0,0,30,30);
        Collision second = new Collision(3, 4, 30, 30);

        Assert.assertTrue(first.distance(second) == 5);
    }

}