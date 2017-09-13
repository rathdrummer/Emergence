package Normal;

/**
 * Created by oskar on 2017-09-14.
 * This classes has some inputs and outputs
 */
public class Stepper {


    private int y;
    private int stepSize;
    private int x;

    public Stepper(int x, int y, int stepSize) {
        this.x = x;
        this.y = y;
        this.stepSize = stepSize;
    }

    public int right() {
        return this.x += stepSize;
    }

    public int left() {
        return this.x -= stepSize;
    }

    public int down() {
        return this.y += stepSize;
    }

    public int up() {
        return this.y -= stepSize;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
