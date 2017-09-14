package Tests;

import Normal.Library;
import org.junit.Assert;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * Created by oskar on 2017-09-13.
 * This classes has some inputs and outputs
 */
public class LibraryTest {
    @Test
    public void canWeLoad() throws Exception {
        BufferedImage image = Library.loadImage("box");

        int width = image.getWidth();
        int height = image.getHeight();


        Assert.assertTrue(width == 35);
        Assert.assertTrue(height == 34);
    }

    @Test
    public void doWeOptimizeMultipleLoads() {
        BufferedImage image = Library.loadImage("blob");
        int sizeOfSavedImages = Library.savedImagesSize();
        BufferedImage image2 = Library.loadImage("blob");

        Assert.assertTrue(Library.savedImagesSize() > 0);
        Assert.assertTrue(sizeOfSavedImages == Library.savedImagesSize());
        Assert.assertTrue(image.getWidth() == image2.getWidth() && image.getHeight() == image2.getHeight());
        Assert.assertFalse(image == image2);
    }

    @Test
    public void optimizeWhen() {
        BufferedImage image = Library.loadImage("blob");
        int sizeOfSavedImages = Library.savedImagesSize();

        BufferedImage tile = Library.loadTile("blob", 0,0,20,20);
        Assert.assertTrue(tile.getWidth() == 20);
        Assert.assertTrue(tile.getHeight() == 20);

        Assert.assertTrue(sizeOfSavedImages == Library.savedImagesSize());
    }

}