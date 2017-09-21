package Pylos;

/**
 * Created by oskar on 2017-09-21.
 * This classes has some inputs and outputs
 */
public class Button {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final String display;

    public Button(int x, int y, int width, int height, String display) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.display = display;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getDisplay() {
        return display;
    }

    public boolean isClicked(int mouseX, int mouseY) {
        return mouseX > getX() && mouseY > getY() && mouseX < getY() + getWidth() && mouseY < getY() + getHeight();
    }
}
