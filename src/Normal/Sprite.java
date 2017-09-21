package Normal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;


/**
 * Created by oskar on 2016-12-09.
 * This classes has some inputs and outputs
 */
public class Sprite {

    public static final int AT_LOOP = -1;
    private BufferedImage[] img;
    private double imageSpeed = .1;
    private double imageIndex = 0;
    private boolean looped;

    private HashMap<Integer, GenericListener> doOnFrame = new HashMap<>();

    public Sprite(String imagePath) {
        img = new BufferedImage[1];
        img[0] = Library.loadImage(imagePath);
        imageSpeed = 0;
    }

    public Sprite(String imagePath, int width, int height) {
        img = Library.loadSprite(imagePath, width, height);
    }

    public Sprite(String imagePath, int width) {
        img = Library.loadSprite(imagePath, width);
    }

    //does not clone framelisteners!!!
    public Sprite clone() {
        Sprite spr = new Sprite(img.clone());
        spr.setImageSpeed(imageSpeed);
        return spr;
    }

    public void reverse() {
        BufferedImage[] reversed = new BufferedImage[img.length];

        for (int i = 0; i < img.length; i++) {
            reversed[i] = img[img.length - 1 - i];        }

        img = reversed;
    }


    public Sprite(BufferedImage image) {
        img = new BufferedImage[1];
        img[0] = image;
        imageSpeed = 0;
    }

    public Sprite(BufferedImage[] images) {
        this.img = images;
        if (this.img.length == 1) {
            imageSpeed = 0;
        }
    }


    public boolean looped() {
        return looped;
    }

    public void pause() {
        imageIndex = 0;
        imageSpeed = 0;
    }

    public Sprite(BufferedImage image, int width, int height) {
        img = Library.loadSprite(image, width, height);
    }


    public void setImageIndex(int index) {
        imageIndex = index;
        if (imageIndex == 0) looped = false; // we do not looped if we set the image index
    }

    public int getImageIndex() {
        return (int) Math.floor(imageIndex);
    }

    public void setImageSpeed(double imageSpeed) {
        this.imageSpeed = imageSpeed;
    }

    public void update() {

        boolean nowLoop = false;
        if (imageSpeed != 0 && img.length > 1) {
            imageIndex += imageSpeed;

            int floored = (int) Math.floor(imageIndex);

            if (floored >= img.length) {
                imageIndex -= img.length;
                nowLoop = true;
            }

            if (floored < 0) {
                imageIndex += img.length;
                nowLoop = true;
            }
        }

        looped = nowLoop;
        if (imageIndex < 0 || imageIndex >= img.length) throw new AssertionError();

        if (looped()) {
            //if we are on a new loop
            GenericListener fl = doOnFrame.get(Sprite.AT_LOOP);
            if (fl != null) {
                fl.action();
            }
        }

        GenericListener fl = doOnFrame.get(getImageIndex());
        if (fl != null) {
            fl.action();
        }



    }

    public int getWidth() {
        return img[(int) imageIndex].getWidth(null);
    }

    public int getHeight() {
        return img[(int) imageIndex].getHeight(null);
    }

    public Image getImage() {
        return img[(int) imageIndex];
    }

    public Image getImage(double imageIndex) {
        return img[(int) imageIndex];
    }

    public int length() {
        return img.length;
    }

    public Sprite getPartOf(int startFrame, int nrOfFrames) {

        BufferedImage[] images = new BufferedImage[nrOfFrames];

        for (int i = 0; i < nrOfFrames; i++) {
            BufferedImage bim = img[i + startFrame];
            images[i] = bim;
        }

        Sprite spr = new Sprite(images);
        spr.setImageSpeed(imageSpeed);
        return spr;
    }

    public double getImageSpeed() {
        return imageSpeed;
    }

    public void setFrameListenerOn(int imageIndex, GenericListener fl) {
        doOnFrame.put(imageIndex, fl);
    }
}
