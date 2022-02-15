import java.util.Objects;

public class Cell {

    // Initialise variables.
    private int x, y;
    private char value;
    private boolean blocked, mine;

    /**
     * Create a new cell on the board.
     * @param x the x coordinate of the cell.
     * @param y the y coordinate of the cell.
     * @param value the value of the cell.
     */
    public Cell(int x, int y, char value) {
        this.x = x;
        this.y = y;
        this.value = value;
        setCellType(value); // det
    }

    private void setCellType(char value) {
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
    public int getX() {
        return x;
    }

    /**
     * Get y coordinate of the cell.
     * @return y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Get the value of the cell.
     * @return cell's value.
     */
    public char getValue() {
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
