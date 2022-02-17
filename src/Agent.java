import java.util.ArrayList;

public abstract class Agent {

    //TODO: FIX COMMENTS!

    private Game game;
    private boolean verbose;
    private int strategy;

    private Cell[][] knownWorld;
    private ArrayList<Cell> blocked; // the agent must know the blocked cells.
    private Cell topLeft;
    private Cell centreCell;
    private int numberOfMines;

    private ArrayList<Cell> probed, markedMines;

    private int rowSize, columnSize;

    public Cell[][] getKnownWorld() {
        return knownWorld;
    }

    public ArrayList<Cell> getProbed() {
        return probed;
    }

    public ArrayList<Cell> getMarkedMines() {
        return markedMines;
    }

    public Game getGame() {
        return game;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public Agent(char[][] board, int strategy, boolean verbose) {
        this.strategy = strategy;
        this.verbose = verbose;
        game = new Game(board);
        probed = new ArrayList<>();
        markedMines = new ArrayList<>();
        getKnowledgeBase();
        probe();
    }

    /**
     * Get the knowledge base of the agent.
     */
    private void getKnowledgeBase() {
        blocked = game.getBlockedCells();
        topLeft = game.getTopLeftCell();
        centreCell = game.getCentreOfBoardCell();
        numberOfMines = game.getTotalNumberMines();
        rowSize = game.getBoardRowSize();
        columnSize = game.getBoardColumnSize();
        knownWorld = new Cell[rowSize][columnSize];
        initialiseAgentWorld();
    }

    /**
     * Probe the map.
     */
    public abstract void probe();

    /**
     * Uncover the cell just probed.
     */
    public void uncover(int r, int c) {
        // uncover only cells that are within the board's borders.
        if (r >= 0 && r < rowSize && c >= 0 && c < columnSize) {
            // Ask game about the value of the same coordinates but from real board.
            Cell probedCell = game.getCell(r, c);
            // if the probed cell is not a blocked cell and was not already probed, then proceed to uncover it to the agent.
            if (!blocked.contains(probedCell) && !probed.contains(probedCell)) {
                probed.add(probedCell); // add the cell to the probed list.

                // if the probed cell is a mine, then the agent lost.
                if (probedCell.isMine()) {
                    knownWorld[probedCell.getR()][probedCell.getC()].setValue('-');
                    printFinal(false);
                } else {
                    // if the value of the probed cell is 0, then uncover adjacent cells.
                    if (probedCell.getValue() == '0') {
                        knownWorld[probedCell.getR()][probedCell.getC()].setValue(probedCell.getValue()); // uncover cell.
                        uncoverAdjacentCells(r, c);  // Uncover adjacent cells.
                    }
                    // Uncover the value of the probed cell (agent knows the value).
                    else {
                        knownWorld[probedCell.getR()][probedCell.getC()].setValue(probedCell.getValue());
                    }
                }
            }
        }
    }

    /**
     * If the cell contains a value 0 meaning that no adjacent cells contain mines,
     * all the non- blocked neighbouring cells will also be uncovered.
     *
     * @param r
     * @param c
     */
    public void uncoverAdjacentCells(int r, int c) {
        uncover(r - 1, c - 1); // Top Left
        uncover(r - 1, c); // Top Center
        uncover(r - 1, c + 1); // Top Right

        uncover(r, c - 1); // Left
        uncover(r, c + 1); // Right

        uncover(r + 1, c - 1); // Bottom left
        uncover(r + 1, c); // Bottom
        uncover(r + 1, c + 1); // Bottom Right
    }

    //TODO: flag cell when thinking mine is found.
    private void flagCell(Cell cell) {

    }

    public void initialiseAgentWorld() {
        // Start from top left.
        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < columnSize; c++) {
                // check if the current cell is known to be blocked.
                if (containInList(r,c,blocked)) {
                    // set the value of cell in r,c to b.
                    knownWorld[r][c] = new Cell(r,c,'b');
                } else {
                    // set the value of cell in r,c to ?.
                    knownWorld[r][c] = new Cell(r,c,'?');
                }
            }
        }
    }

    /**
     * Prints the agent's world.
     * @param finalOutput flag to determine if this method was celled for the final output.
     */
    public void printAgentKnownWorld(boolean finalOutput) {
        // if verbose flag is true OR its the final output, then print agent known world.
        if (verbose || finalOutput) {
            System.out.println();
            // first line
            System.out.print("    ");
            for (int j = 0; j < knownWorld[0].length; j++) {
                System.out.print(j + " "); // x indexes
            }
            System.out.println();
            // second line
            System.out.print("    ");
            for (int j = 0; j < knownWorld[0].length; j++) {
                System.out.print("- ");// separator
            }
            System.out.println();
            // the board
            for (int i = 0; i < knownWorld.length; i++) {
                System.out.print(" " + i + "| ");// index+separator
                for (int j = 0; j < knownWorld[0].length; j++) {
                    System.out.print(knownWorld[i][j].getValue() + " ");// value in the board
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /**
     * Returns whether a cell is contained in a given list. Only the cell's row and columns
     * are required. This was created to avoid creating temporary cells repeatedly throughout
     * the code.
     * @param r row of the cell.
     * @param c column of the cell.
     * @param list the list to check.
     * @return
     */
    public boolean containInList(int r, int c, ArrayList<Cell> list) {
        Cell tempCell = new Cell(r, c);
        return list.contains(tempCell);
    }

    /**
     * Used to print the final output.
     * @param won flag to determine whether the agent won or lost.
     */
    public void printFinal(boolean won) {
        System.out.println("Final map");
        printAgentKnownWorld(true);
        if (won) {
            System.out.println("Result: Agent alive: all solved");
        } else {
            System.out.println("Result: Agent dead: found mine");
        }

        System.exit(0);
    }


}
