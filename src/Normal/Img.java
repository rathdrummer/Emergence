package Normal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * A set of tools for loading images...not necessary maybe?
 */
public class Img {


    public static Image loadImage(String name){
        Image image;
        try{
            String dir = "images\\";
            URL u = ClassLoader.getSystemResource(dir + name);
            if (u != null) {
                image = ImageIO.read(u);
            } else {
                u = ClassLoader.getSystemResource(dir+name+".png");
                if (u != null) {
                    image= ImageIO.read(u);
                } else {

                    image = ImageIO.read(ClassLoader.getSystemResource(dir+name+".jpg"));
                }
            }

        }
        catch (IOException e){
            e.printStackTrace();
            image = new BufferedImage(50,50,1);
        }
        return image;
    }
}
