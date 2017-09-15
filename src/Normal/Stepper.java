package Normal;

/**
 * Created by oskar on 2017-09-14.
 * This classes has some inputs and outputs
 */
public class Stepper {


    private int y;
    private int stepSizeX;
    private int stepSizeY;
    private int x;

    public Stepper(int x, int y, int stepSizeX, int stepSizeY) {
        this.x = x;
        this.y = y;
        this.stepSizeX = stepSizeX;
        this.stepSizeY = stepSizeY;
    }

    public int right() {
        return this.x += stepSizeX;
    }

    public int left() {
        return this.x -= stepSizeX;
    }

    public int down() {
        return this.y += stepSizeY;
    }

    public int up() {
        return this.y -= stepSizeY;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
