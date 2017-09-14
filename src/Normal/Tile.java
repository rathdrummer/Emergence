package Normal;

import java.awt.*;

/**
 * Created by oskar on 2017-09-14.
 * This classes has some inputs and outputs
 */
public class Tile extends Drawable {
    Image image;
    private int x;
    private int y;
    private double width;
    private double height;

    public Tile(String fileName, int x, int y, int width, int height, int row, int column) {
        initTile(fileName, x, y, width, height, row, column);
    }

    public Tile(String fileName, int x, int y, int width, int height, int nr) {
        int wholeWidth = Library.loadImage(fileName).getWidth();

        int column = nr % (wholeWidth/width);
        int row = nr / (wholeWidth/width);

        initTile(fileName, x, y, width, height, row, column);
    }

    private void initTile(String fileName, int x, int y, int width, int height, int row, int column) {
        //@FutureProof, this only works if all tiles in a sheet is the same size
        image = Library.loadTile(fileName, column * width, row * height, width, height);

        this.x = x;
        this.y = y;
        this.width = (double) width;
        this.height = (double) height;
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

    public Image getImage() {
        return image;
    }
}
