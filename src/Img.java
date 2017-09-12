import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * A set of tools for loading images...not necessary maybe?
 */
public class Img {

       // bg = loadImage("car-road-rug.jpg");
       // player = loadImage("circle.png");

    public static Image loadImage(String name){
        Image image;
        try{
            image = ImageIO.read(ClassLoader.getSystemResource(name));

        }
        catch (IOException e){
            e.printStackTrace();
            image = new BufferedImage(50,50,1);
        }
        return image;
    }
}
