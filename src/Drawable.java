import java.awt.*;

public interface Drawable {

    double x();
    double y();

    /**
     * Get X in center of object
     */
    double xC();

    /**
     * Get Y in center of object
     */
    double yC();

    double width();
    double height();

    Image getImage();

}
