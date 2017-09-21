package Pylos;

import Pylos.OwnedBy;
import org.junit.Assert;

/**
 * Created by oskar on 2017-09-21.
 * This classes has some inputs and outputs
 */
public class Pile {

    private OwnedBy owner;
    private final int x;
    private final int y;
    private final int pileSizeMax = 15;
    private int pileSize = pileSizeMax;

    public Pile(OwnedBy owner, int x, int y) { // player
        this.owner = owner;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public OwnedBy getOwner() {
        return owner;
    }

    public int size() {
        return pileSize;
    }

    public void addBack() {
        Assert.assertTrue(pileSize < pileSizeMax);
        pileSize ++;
    }

    public void take() {
        Assert.assertTrue(pileSize > 0);
        pileSize --;
    }

    public boolean hasLeft() {
        return size() > 0;
    }
}
