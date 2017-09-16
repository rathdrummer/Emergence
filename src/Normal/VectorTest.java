package Normal;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by oskar on 2017-09-15.
 * This classes has some inputs and outputs
 */
public class VectorTest {

    @Test
    public void testComponents() {
        Vector v = new Vector(3,4);
        Vector same = Vector.getComponents(v.getLength(), v.getAngle());
        Assert.assertTrue(v.x - same.x < 0.01);
        Assert.assertTrue(v.y - same.y < 0.01);
    }

    @Test
    public void testNewLenght() {
        Vector v = new Vector(3,4);
        Vector doubleUp = Vector.getComponents(v.getLength() * 2, v.getAngle());

        Assert.assertTrue(v.getLength() - 5 < 0.01);
        Assert.assertTrue(v.getLength() - 10 < 0.01);
    }

}