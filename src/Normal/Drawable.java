package Normal;

import java.awt.*;

public interface Drawable {

    // What if a common update() method was put in every drawable....

    double x();
    double y();

    /**
     * Get X in center of object
     */
    default double xC(){
        return x()+width()/2;
    };

    /**
     * Get Y in center of object
     */
    default double yC(){
        return y()+height()/2;
    };

    double width();
    double height();

    Image getImage();

}