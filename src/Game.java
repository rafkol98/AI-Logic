import java.util.ArrayList;

public class Game {

    private Cell[][] map;

    public Game(char[][] board) {
        this.map = new Cell[board.length][board[0].length];
        createAllCellsInBoard(board);
    }

    /**
     * Create a cell for each coordinate/place in the map.
     */
    private void createAllCellsInBoard(char[][] board) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                char value = board[x][y]; // get value.
                map[x][y] = new Cell(x, y, value); // Create a new cell at the board's coordinate with the value.
            }
        }
    }

    public int getBoardRowSize() {
        return map.length;
    }

    public int getBoardColumnSize() {
        return map[0].length;
    }

    public int getTotalNumberMines() {
        int totalNumberMines = 0;

        // iterate through the map.
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                // if cell is a mine, increment the counter.
                if (map[x][y].isMine()) {
                    totalNumberMines++;
                }
            }
        }
        return totalNumberMines;
    }

    public ArrayList<Cell> getBlockedCells() {
        ArrayList<Cell> blockedCells = new ArrayList<>();

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                // if cell is a mine, increment the counter.
                if (map[x][y].isBlocked()) {
                    // add cell in the blocked cells ArrayList.
                    blockedCells.add(map[x][y]);
                }
            }
        }

        return blockedCells;
    }

    // the agent can safely probe the cell in the centre of the board at the start of the game
    public Cell getCentreOfBoardCell() {

        // TODO: get row and col size, divide by 2, store those coordinates, return map[r][c]
        return null;
    }


}
