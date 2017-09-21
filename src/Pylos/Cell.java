package Pylos;

/**
 * Created by oskar on 2017-09-20.
 * This classes has some inputs and outputs
 */
public class Cell {
    private final int x;
    private final int y;
    private OwnedBy cellState;
    private Level level;

    Cell(int x, int y, Level level, OwnedBy cellState) {
        this.x = x;
        this.y = y;
        this.level = level;
        this.cellState = cellState;

    }

    public OwnedBy getCellState() {
        return cellState;
    }

    public void setCellState(OwnedBy cellState) {
        this.cellState = cellState;
    }

    public Level getLevel() {
        return level;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return level.getZ();
    }


    public boolean samePlace(Cell other) {
        return getX() == other.getX() && getY() == other.getY() && getLevel() == other.getLevel();
    }
}
