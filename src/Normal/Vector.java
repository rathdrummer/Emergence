package Normal;

public class Vector {


    public final double x;
    public final double y;

    Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }


    public static Vector getComponents(double magnitude, double radians) {
        return new Vector(magnitude * Math.cos(radians), magnitude * Math.sin(radians));
    }

    public static double getAngle{
        return 
    }


}
