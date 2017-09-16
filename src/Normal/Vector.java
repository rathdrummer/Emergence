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

    public double getAngle(){
        return Math.atan2(y,x);
    }

    public Vector add(Vector other){
        return new Vector(x+other.x, y+other.y);
    }


    public double getLength() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (Double.compare(vector.x, x) != 0) return false;
        return Double.compare(vector.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
