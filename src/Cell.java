import java.util.Objects;

public class Cell {

    // Initialise variables.
    private int r, c;
    private char value;
    private boolean blocked, mine;

    public Cell(int r, int c) {
        this.r = r;
        this.c = c;
    }

    /**
     * Create a new cell on the board.
     * @param r the x coordinate of the cell.
     * @param c the y coordinate of the cell.
     * @param value the value of the cell.
     */
    public Cell(int r, int c, char value) {
        this.r = r;
        this.c = c;
        this.value = value;
        setCellType(value); // det
    }

    public void setCellType(char value) {
        switch (value) {
            case 'm':
                // Cell contains mine.
                mine = true;
                break;
            case 'b':
                // Blocked cell.
                blocked = true;
                break;
            default:
                // Free cell!
                mine = false;
                blocked = false;
        }
    }

    /**
     * Get if the cell is blocked.
     * @return true if cell is blocked.
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * Get if the cell contains a mine.
     * @return true if the cell contains a mine.
     */
    public boolean isMine() {
        return mine;
    }

    /**
     * Get x coordinate of the cell.
     * @return x coordinate.
     */
    public int getR() {
        return r;
    }

    /**
     * Get y coordinate of the cell.
     * @return y coordinate.
     */
    public int getC() {
        return c;
    }

    /**
     * Get the value of the cell.
     * @return cell's value.
     */
    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return r == cell.r && c == cell.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }
}
