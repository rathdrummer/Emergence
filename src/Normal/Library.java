package Normal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Oskar on 2016-12-07.
 * This classes has some inputs and outputs
 */
public class Library {


    private static Map<ImageRes, BufferedImage> savedImages = new HashMap<>();

    public static int savedImagesSize() {
        return savedImages.size();
    }



    /**
     * Loads an image by providing the name
     * @param string: Filename
     * @return Image to be used
     */
    public static BufferedImage loadImage(String string) {
        ImageRes res = new ImageRes(string);
        return loadImage(res);
    }

    //load an image by using a ImageRes
    public static BufferedImage loadImage(ImageRes res) {
        BufferedImage img = null;

        BufferedImage find = savedImages.get(res);
        if (find != null) {
            img =  cloneImage(find);
        } else {
            try {
                if (!res.isValid()) throw new IOException();
                else img = ImageIO.read(res.getPath());
            } catch (IOException e) {
                System.err.println("Could not find file at " + res.toString());
            }
            savedImages.put(res, img);
        }

        return img;
    }



    static BufferedImage cloneImage(BufferedImage bi) {

        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /*tiles (sub-image really), takes one part of a sprite sheet and makes it a standAlone image
    this makes it faster because we import the big picture only once */
    public static BufferedImage loadTile(BufferedImage image, int x, int y, int width, int height) {
        return image.getSubimage(x, y, width, height);
    }

    public static BufferedImage loadTile(BufferedImage image, int i, int width, int height) {
        final int cols = image.getWidth() / width;

        int x = i % cols;
        int y = i / cols;
        return loadTile(image, x * width, y * height, width, height);
    }

    public static BufferedImage loadTile(String path, int x, int y, int width, int height) {
        BufferedImage sheet = Library.loadImage(path);
        return loadTile(sheet, x, y, width, height);
    }

    public static BufferedImage loadTile(String path, int i, int width, int height) {
        BufferedImage sheet = Library.loadImage(path);
        return loadTile(sheet, i, width, height);
    }

    //sprites as image arrays
    public static BufferedImage[] loadSprite(String path, int width) {
        BufferedImage img = null;
        ImageRes res = new ImageRes(path);
        try {
            if (!res.isValid()) throw new IOException();
            else img = ImageIO.read(res.getPath());
        } catch (IOException e) {
            System.err.println("Could not find file at " + res.toString());
        }

        return loadSprite(img, width);
    }

    public static BufferedImage[] loadSprite(BufferedImage image, int width) {
        final int cols = image.getWidth() / width;
        int height = image.getHeight();
        BufferedImage[] sprites = new BufferedImage[cols];

        for (int i = 0; i < cols; i++) {
            sprites[i] = image.getSubimage(i * width, 0, width, height);
        }

        return sprites;
    }

    public static BufferedImage[] loadSprite(BufferedImage image, int width, int height) {
        final int cols = image.getWidth() / width;
        final int rows = image.getHeight() / height;
        BufferedImage[] sprites = new BufferedImage[rows * cols];

        for (int yy = 0; yy < rows; yy++) {
            for (int xx = 0; xx < cols; xx++) {
                sprites[(yy * cols) + xx] = image.getSubimage(xx * width, yy * height, width, height);
            }
        }
        return sprites;
    }

    public static BufferedImage[] loadSprite(String string, int width, int height) {
        BufferedImage sheet = Library.loadImage(string);
        return loadSprite(sheet, width, height);
    }

    public static BufferedImage tint(BufferedImage img, Color color) {
        return tint(img, color, 1);
    }

    /**
     * @param img: The image to tint
     * @param color: the color you want
     * @param alpha: the transparency you want
     * @return
     */
    public static BufferedImage tint(BufferedImage img, Color color, double alpha) {
        float r = color.getRed()    / 255f;
        float g = color.getGreen()  / 255f;
        float b = color.getBlue()   / 255f;
        float a = (float) alpha;
        return tint(img, r, g, b, a);
    }

    public static BufferedImage opacity(BufferedImage img, double a) {
        return opacity(img, (float) a);
    }

    /**
     * Return a colorized version of the sprite
     * @param img, the image to colorize
     * @param r: 0-255. Red value
     * @param g: 0-255. Green value
     * @param b: 0-255. Blue Value
     * @param a: 0.0-1.0 the alpha of the new sprite, 0 is invisble, 1 is opaque
     * @return A new image tintet the color
     */
    public static BufferedImage opacity(BufferedImage img, float a) {

        if (a > 1) {
            System.err.println("alpha to big!! (supposed to be 0-1), dividing by 255");
            a /= 255;
        }

        BufferedImage tintedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D graphics = tintedImage.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        for (int i = 0; i < tintedImage.getWidth(); i++) {
            for (int j = 0; j < tintedImage.getHeight(); j++) {
                int ax = tintedImage.getColorModel().getAlpha(tintedImage.getRaster().
                        getDataElements(i, j, null));
                int rx = tintedImage.getColorModel().getRed(tintedImage.getRaster().
                        getDataElements(i, j, null));
                int gx = tintedImage.getColorModel().getGreen(tintedImage.getRaster().
                        getDataElements(i, j, null));
                int bx = tintedImage.getColorModel().getBlue(tintedImage.getRaster().
                        getDataElements(i, j, null));

                ax *= a;
                tintedImage.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx));
            }
        }
        return tintedImage;
    }


    /**
     * Return a colorized version of the sprite
     * @param img, the image to colorize
     * @param r: 0-255. Red value
     * @param g: 0-255. Green value
     * @param b: 0-255. Blue Value
     * @param a: 0.0-1.0 the alpha of the new sprite, 0 is invisble, 1 is opaque
     * @return A new image tintet the color
     */
    public static BufferedImage tint(BufferedImage img, float r, float g, float b, float a) {

        if (a > 1) {
            System.err.println("alpha to big!! (supposed to be 0-1), dividing by 255");
            a /= 255;
        }

        BufferedImage tintedImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D graphics = tintedImage.createGraphics();
        graphics.drawImage(img, 0, 0, null);
        graphics.dispose();

        for (int i = 0; i < tintedImage.getWidth(); i++) {
            for (int j = 0; j < tintedImage.getHeight(); j++) {
                int ax = tintedImage.getColorModel().getAlpha(tintedImage.getRaster().
                        getDataElements(i, j, null));
                int rx = tintedImage.getColorModel().getRed(tintedImage.getRaster().
                        getDataElements(i, j, null));
                int gx = tintedImage.getColorModel().getGreen(tintedImage.getRaster().
                        getDataElements(i, j, null));
                int bx = tintedImage.getColorModel().getBlue(tintedImage.getRaster().
                        getDataElements(i, j, null));
                rx *= r;
                gx *= g;
                bx *= b;
                ax *= a;
                tintedImage.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx));
            }
        }
        return tintedImage;
    }


    /**
     * Returns a color, based on hue, saturation, value.
     * This is a more intuitive way of creating colors (than rgb) because it is more closely correlated to
     * the human perception of color
     * @param hIn: 0-255. Basically the light spectrum. Red to orange to yellow, green, blue, pink, purple, red (again).
     * @param sIn: 0-255. How sharp the color is, more mellow colors like pastel are lower, and neon colors are higher.
     * @param vIn: 0-255. Dark to light, 0 is black, 255 is full color.
     * @return the Color
     */
    public static Color hsvColor(int hIn, int sIn, int vIn) {

        double h = (hIn * 360 / 255f);
        double s = (sIn / 255f);
        double v = (vIn /255f);

        double c = s * v;
        double x = c * (1 - Math.abs((h / 60) % 2 - 1));
        double m = v - c;

        double r = 0;
        double g = 0;
        double b = 0;

        int decider = (int) (h / 60f);
        switch(decider) {
            case 0:
                r = c; g = x;
                break;
            case 1:
                r = x; g = c;
                break;
            case 2:
                g = c; b = x;
                break;
            case 3:
                g = x; b = c;
                break;
            case 4:
                r = x; b = c;
                break;
            case 5:
                r = c; b = x;
                break;
            default: System.err.println("H not in range 0-255");
        }
        return new Color((int)((r + m) * 255), (int)((g + m) * 255), (int)((b + m) * 255));
    }

}
