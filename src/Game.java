import java.util.ArrayList;

public class Game {

    private Cell[][] board;
    private long startTime;

    /**
     * Create a new Game instance.
     *
     * @param boardIn
     */
    public Game(char[][] boardIn) {
        this.board = new Cell[boardIn.length][boardIn[0].length];
        createAllCellsInBoard(boardIn);
    }

    /**
     * Create a cell for each coordinate/place in the map.
     */
    private void createAllCellsInBoard(char[][] boardIn) {
        for (int i = 0; i < boardIn.length; i++) {
            for (int j = 0; j < boardIn[0].length; j++) {
                char value = boardIn[i][j]; // get value.
                this.board[i][j] = new Cell(i, j, value); // Create a new cell at the board's coordinate with the value.
            }
        }
    }

    /**
     * Get board's row size.
     *
     * @return the row size of the board.
     */
    public int getBoardRowSize() {
        return board.length;
    }

    /**
     * Get board's column size.
     *
     * @return the column size of the board.
     */
    public int getBoardColumnSize() {
        return board[0].length;
    }

    /**
     * Get the total number of mines.
     *
     * @return total number of mines.
     */
    public int getTotalNumberMines() {
        int totalNumberMines = 0;

        // iterate through the map.
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                // if cell is a mine, increment the counter.
                if (board[i][j].isMine()) {
                    totalNumberMines++;
                }
            }
        }
        return totalNumberMines;
    }

    /**
     * Get the cells that are blocked.
     *
     * @return an ArrayList containing all the blocked cells.
     */
    public ArrayList<Cell> getBlockedCells() {
        ArrayList<Cell> blockedCells = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                // if cell is a mine, increment the counter.
                if (board[i][j].isBlocked()) {
                    // add cell in the blocked cells ArrayList.
                    blockedCells.add(board[i][j]);
                }
            }
        }

        return blockedCells;
    }

    /**
     * Get the safe centre of the board cell.
     *
     * @return the cell located in the centre of the board.
     */
    public Cell getCentreOfBoardCell() {
        int rowCenter = getBoardRowSize() / 2;
        int columnCenter = getBoardColumnSize() / 2;
        return board[rowCenter][columnCenter];
    }

    /**
     * Get the safe top left-cell (0,0).
     *
     * @return the top-left cell.
     */
    public Cell getTopLeftCell() {
        return board[0][0];
    }

    /**
     * Get the Cell located in the passed in row and column.
     *
     * @param r row passed in.
     * @param c column passed in.
     * @return Cell located at the specified row and column coordinates.
     */
    public Cell getCell(int r, int c) {
        return board[r][c];
    }

    /**
     * Method used to determine if the agent won the game.
     * Game won is determined by the correct number of probed cells.
     *
     * @param numberProbed the number of cells probed/uncovered.
     * @param markedMines  the number of cells marked as mines.
     * @return true if the game is won, false otherwise.
     */
    public boolean isGameWon(int numberProbed, int markedMines, int agentNo) {
        int numberBlocked = getBlockedCells().size();
        int numberCellsInBoard = getBoardRowSize() * getBoardColumnSize();

        // If its the first or fifth agent (NoFlag) then the agent wins by probing the correct number of cells.
        if (agentNo == 1 || agentNo == 5) {
//            System.out.println("MESA dame");
            // if all but M cells are probed without a game over, the agent wins the game.
            if ((numberProbed + numberBlocked) == numberCellsInBoard - getTotalNumberMines()) {
                return true;
            }
        } else if (markedMines == getTotalNumberMines()) {
            // if the number of cells probed plus number of blocked + the total number of mines
            // is equal to the number of cells in the board, then it means that the agent won
            // as they uncovered every cell that is not a mine.
            if ((numberProbed + numberBlocked + markedMines) == numberCellsInBoard) {
                return true;
            }
        }

        return false;
    }

    /**
     * Used to start record time (time flag).
     */
    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Used to stop recording time (time flag).
     */
    public void stopTimer() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("\nElapsed MilliSeconds: " + elapsedTime);
    }

}
