import java.util.Objects;

public class Cell {

    // Initialise variables.
    private int r, c;
    private char value;
    private boolean blocked, mine;

    /**
     * Create a new cell without value, passing in the row and columns. This is used to perform
     * checks to see if a cell is already contained in a list. This is done by creating a temporary
     * empty cell to then use the equals method to check equality with an existing cell.
     *
     * @param r the x coordinate of the cell.
     * @param c the y coordinate of the cell.
     */
    public Cell(int r, int c) {
        this.r = r;
        this.c = c;
    }

    /**
     * Create a new cell on the board.
     *
     * @param r     the x coordinate of the cell.
     * @param c     the y coordinate of the cell.
     * @param value the value of the cell.
     */
    public Cell(int r, int c, char value) {
        this.r = r;
        this.c = c;
        this.value = value;
        setCellType(value); // det
    }

    /**
     * Set the type (mine, blocked, or normal) of the cell depending on its value.
     *
     * @param value the value of cell.
     */
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
     *
     * @return true if cell is blocked.
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * Get if the cell contains a mine.
     *
     * @return true if the cell contains a mine.
     */
    public boolean isMine() {
        return mine;
    }

    /**
     * Get x coordinate of the cell.
     *
     * @return x coordinate.
     */
    public int getR() {
        return r;
    }

    /**
     * Get y coordinate of the cell.
     *
     * @return y coordinate.
     */
    public int getC() {
        return c;
    }

    /**
     * Get the value of the cell.
     *
     * @return cell's value.
     */
    public char getValue() {
        return value;
    }

    /**
     * Set/update the value of a cell.
     *
     * @param value cell's new value.
     */
    public void setValue(char value) {
        this.value = value;
    }

    /**
     * Get the value of the cell as integer.
     *
     * @return value as integer.
     */
    public int getValueInt() {
        return Integer.parseInt(String.valueOf(getValue()));
    }

    /**
     * Check to test if two cells are the same.
     *
     * @param o the cell being compared to.
     * @return true if the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return r == cell.r && c == cell.c;
    }

    /**
     * Hashcode used to determine equality.
     *
     * @return cell's hash value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }

    /**
     * Return cell information as string. USED FOR DEBUG PURPOSES.
     *
     * @return cell's information as string.
     */
    @Override
    public String toString() {
        return "Cell{" + r + "," + c + '}';
    }
}
