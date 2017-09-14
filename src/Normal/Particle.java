package Normal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Particles - a lasting (or not) Drawable with minor animations (velocity and size change).
 * Right now it creates a cloud of ya boi. Maybe move that to a subclass later.
 */
public class Particle extends Drawable{

    public double x;
    public double y;
    public double dx;
    public double dy;
    public double width;
    public double height;
    public BufferedImage image;
    public float opacity;

    public Particle(double x, double y, BufferedImage image){
        this.x = x;
        this.y = y;
        this.image = image;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        Vector v = Vector.getComponents(5, Math.random()*2*Math.PI);
        dx = v.x;
        dy = v.y;
        this.opacity = 1;
    }

    public static List<Drawable> spawn(int nr, int x , int y, BufferedImage img) {
        List<Drawable> particles = new ArrayList<>();
        for (int i = 0; i < nr; i++) {
            particles.add(new Particle(x,y, img));
        }
        return particles;
    }

    public void update(){
        x += dx;
        y += dy;

        dx *= 0.95;
        dy *= 0.95;
        width--;
        height--;


        //image = Library.tint(image, 255, 255, 255, opacity);
        opacity *= .95;


        if (width <= 0 || height <= 0){
            remove();
        }

    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double width() {
        return width;
    }

    @Override
    public double height() {
        return height;
    }

    @Override
    public Image getImage() {
        return image;
    }


}
