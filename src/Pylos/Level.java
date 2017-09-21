package Pylos;

/**
 * Created by oskar on 2017-09-20.
 * This classes has some inputs and outputs
 */
public class Level {

    private Cell[][] cells;
    private Level above;
    private int size;
    private Level below = null;
    private int offset;

    public Level(int size, Level below, int offset) {
        this.size = size;
        this.cells = new Cell[size][size];

        this.below = below;

        System.out.println(above);

        this.offset = offset;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                this.cells[x][y] = new Cell(x, y, this, OwnedBy.NoPlayer);
            }
        }
    }

    public void setAbove(Level level) {
        above = level;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCell(int x, int y, OwnedBy cs) {
        if (cells[x][y].getCellState() == OwnedBy.NoPlayer) {
            cells[x][y].setCellState(cs);
        }
        else {
            System.err.println("taken");
        }
    }


    public boolean colorRow(int rowIndex, OwnedBy cs) {
        for (int c = 0; c < size; c++) {
            if (!isSame(c,rowIndex, cs)) {
                return false;
            }
        }
        return true;
    }


    public boolean colorColumn(int columnIndex, OwnedBy cs) {
        for (int r = 0; r < size; r++) {
            if (!isSame(columnIndex, r, cs)) {
                return false;
            }
        }
        return true;
    }

    public Cell takeCandidate(int mouseX, int mouseY, int scale, OwnedBy ownedBy) {
        mouseX -= offset;
        mouseY -= offset;

        int disX;
        int disY;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                disX = mouseX - x * scale;
                disY = mouseY - y * scale;
                if (disX < scale && disX > 0 && disY < scale && disY > 0) {
                    if (!isBlocked(x, y) && cells[x][y].getCellState() == ownedBy) {
                        return cells[x][y];
                    }
                }
            }
        }

        return null;
    }

    public Cell addCandidate(int mouseX, int mouseY, int scale) {
        mouseX -= offset;
        mouseY -= offset;

        int disX;
        int disY;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                disX = mouseX - x * scale;
                disY = mouseY - y * scale;
                if (disX < scale && disX > 0 && disY < scale && disY > 0) {
                    if (isSolid(x, y) && cells[x][y].getCellState() == OwnedBy.NoPlayer) {
                        return cells[x][y];
                    }
                }
            }
        }

        return null;
    }

    public boolean isBlocked(int x, int y) {

        if (above == null) return false;
        boolean a = above.hasBall(x-1, y-1);
        boolean b = above.hasBall(x-1, y);
        boolean c = above.hasBall(x, y-1);
        boolean d = above.hasBall(x, y);

        return (a || b || c || d);
    }

    public boolean normalSquare(int x, int y) {
        boolean a = square(x,y);
        boolean b = square(x-1,y);
        boolean c = square(x,y-1);
        boolean d = square(x-1,y-1);
        return a || b || c || d;
    }

    public boolean isSolid(int x, int y) {
        if (getBelow() == null) {
            return true;
        } else return getBelow().square(x, y);
    }

    private boolean square(int x, int y) {
        boolean a = hasBall(x,y);
        boolean b = hasBall(x+1,y);
        boolean c = hasBall(x,y+1);
        boolean d = hasBall(x+1,y+1);
        return a && b && c && d;
    }

    public boolean hasBall(int x, int y) {
        if (!inSide(x, y)) return false;
        return !isSame(x, y, OwnedBy.NoPlayer);
    }

    private boolean inSide(int x, int y) {
        return (x >= 0 && x < size && y >= 0 && y < size);
    }

    public boolean colorSquare(int x, int y, OwnedBy cs) {
        boolean a = cSquare(x, y, cs);
        boolean b = cSquare(x - 1, y, cs);
        boolean c = cSquare(x, y -1, cs);
        boolean d = cSquare(x - 1, y -1, cs);
        return a || b || c || d;
    }

    private boolean cSquare(int x, int y, OwnedBy cs) {
        return (isSame(x,y,cs) && isSame(x+1,y, cs) && isSame(x,y+1,cs) && isSame(x+1,y+1,cs));
    }

    private boolean isSame(int x, int y, OwnedBy cs) {
        if (!inSide(x,y)) return false;
        return cells[x][y].getCellState() == cs;
    }

    public Level getBelow() {
        return below;
    }

    public int getZ() {
        return size; // these are correlated
    }

    public int getOffset() {
        return offset;
    }


    public int getSize() {
        return size;
    }
}
