import java.util.ArrayList;

public class Game {

    private Cell[][] board;

    public Game(char[][] boardIn) {
        this.board = new Cell[boardIn.length][boardIn[0].length];
//        System.out.println("game in" + board.length +" [] "+board[0].length);
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
//        printMap();
    }

//    /**
//     * Prints the agent's world.
//     */
//    public void printMap() {
//        System.out.println();
//        // first line
//        System.out.print("    ");
//        for (int j = 0; j < board[0].length; j++) {
//            System.out.print(j + " "); // x indexes
//        }
//        System.out.println();
//        // second line
//        System.out.print("    ");
//        for (int j = 0; j < board[0].length; j++) {
//            System.out.print("- ");// separator
//        }
//        System.out.println();
//        // the board
//        for (int i = 0; i < board.length; i++) {
//            System.out.print(" "+ i + "| ");// index+separator
//            for (int j = 0; j < board[0].length; j++) {
//                System.out.print(board[i][j].getValue() + " ");// value in the board
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }

    public int getBoardRowSize() {
        return board.length;
    }

    public int getBoardColumnSize() {
        return board[0].length;
    }

    /**
     * Get the total number of mines.
     * @return
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
     * @return
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

    // the agent can safely probe the cell in the centre of the board at the start of the game
    public Cell getCentreOfBoardCell() {
        int rowCenter = getBoardRowSize() / 2;
        int columnCenter = getBoardColumnSize() / 2;
        return board[rowCenter][columnCenter];
    }

    public Cell getTopLeftCell() {
        return board[0][0];
    }

    public Cell getCell(int r, int c) {
        return board[r][c];
    }

    public boolean isGameWon(int numberProbed) {
        int numberBlocked = getBlockedCells().size();
        int numberCellsInBoard = getBoardRowSize() * getBoardColumnSize();

        // if the number of cells probed plus number of blocked + the total number of mines
        // is equal to the number of cells in the board, then it means that the agent won
        // as they uncovered every cell that is not a mine.
        if ((numberProbed + numberBlocked + getTotalNumberMines()) == numberCellsInBoard) {
            return true;
        }
        return false;
    }

    public void foundMine() {
        System.out.println("Result: Agent dead: found mine");
        System.exit(0);
    }

}
