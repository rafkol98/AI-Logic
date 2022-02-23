import java.util.ArrayList;

public abstract class Agent {

    //TODO: FIX COMMENTS!

    private Game game;
    private boolean verbose;

    private Cell[][] knownWorld;
    private ArrayList<Cell> blocked; // the agent must know the blocked cells.
    private Cell topLeft;
    private Cell centreCell;
    private int numberOfMines;

    private ArrayList<Cell> uncovered, markedMines;

    private int rowSize, columnSize;

    public boolean solutionFound = false;


    public Agent(char[][] board, boolean verbose) {
        this.verbose = verbose;
        game = new Game(board);
        uncovered = new ArrayList<>();
        markedMines = new ArrayList<>();
        getKnowledgeBase();
        solve();
    }

    public void solve() {
        probe(); // call abstract class.

        alternative();

        // if solution is not found, then print not terminated output.
        if (!solutionFound) {
            printFinal(0);
        }
    }

    public abstract void alternative();

    /**
     * Get known world of the Agent.
     * @return the known world in a 2D array.
     */
    public Cell[][] getKnownWorld() {
        return knownWorld;
    }

    /**
     * Get all the cells that were uncovered.
     * @return the uncovered cells.
     */
    public ArrayList<Cell> getUncovered() {
        return uncovered;
    }

    /**
     * Get all the covered cells.
     * @return
     */
    public ArrayList<Cell> getCovered() {
        ArrayList<Cell> covered = new ArrayList<>();
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                if(!getUncovered().contains(getKnownWorld()[r][c]) && !getBlocked().contains(getKnownWorld()[r][c]) && !getMarkedMines().contains(getKnownWorld()[r][c])) {
                    covered.add(getKnownWorld()[r][c]);
                }
            }
        }
        return covered;
    }

    /**
     * Get all the cells that were marked as mines.
     * @return
     */
    public ArrayList<Cell> getMarkedMines() {
        return markedMines;
    }

    /**
     * Get the game.
     * @return the game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the row size of the board.
     * @return board's row size.
     */
    public int getRowSize() {
        return rowSize;
    }

    /**
     * Ge the column size of the board.
     * @return board's column size.
     */
    public int getColumnSize() {
        return columnSize;
    }

    /**
     * Get all the cells that are blocked.
     * @return blocked cells.
     */
    public ArrayList<Cell> getBlocked() {
        return blocked;
    }

    /**
     * Get the cell in the centre of the board.
     * @return centre cell.
     */
    public Cell getCentreCell() {
        return centreCell;
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
        // Ask game about the value of the same coordinates but from real board.
        Cell probedCell = game.getCell(r, c);

        // if the probed cell is not a blocked cell and was not already probed, then proceed to uncover it to the agent.
        if (!blocked.contains(probedCell) && !uncovered.contains(probedCell)) {
            uncovered.add(probedCell); // add the cell to the probed list.

            // if the probed cell is a mine, then the agent lost.
            //TODO: change this!!! has to ask the GAME CLASS if its a bomb!!
            if (probedCell.isMine()) {
                knownWorld[probedCell.getR()][probedCell.getC()].setValue('-');
                printFinal(-1);
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

    /**
     * If the cell contains a value 0 meaning that no adjacent cells contain mines,
     * all the non- blocked neighbouring cells will also be uncovered.
     *
     * @param r the row of the cell.
     * @param c the column of the cell.
     */
    public void uncoverAdjacentCells(int r, int c) {
        ArrayList<Cell> adjacentNeighbours = getAdjacentNeighbours(r, c);

        for (Cell neighbour : adjacentNeighbours) {
            uncover(neighbour.getR(), neighbour.getC());
        }
    }

    /**
     * Get all the adjacent neighbours of a cell and put them in
     * an ArrayList. Only adds neighbours that are legal (within boundaries).
     *
     * @param r the row of the cell.
     * @param c the column of the cell.
     * @return the adjacent neighbours of a cell.
     */
    public ArrayList<Cell> getAdjacentNeighbours(int r, int c) {
        ArrayList<Cell> neighbours = new ArrayList<>();

        addIfLegalNeighbours(neighbours, r - 1, c - 1); // Top Left
        addIfLegalNeighbours(neighbours, r - 1, c); // Top Center
        addIfLegalNeighbours(neighbours, r - 1, c + 1); // Top Right

        addIfLegalNeighbours(neighbours, r, c - 1); // Left
        addIfLegalNeighbours(neighbours, r, c + 1); // Right

        addIfLegalNeighbours(neighbours, r + 1, c - 1); // Bottom Left
        addIfLegalNeighbours(neighbours, r + 1, c); // Bottom
        addIfLegalNeighbours(neighbours, r + 1, c + 1); // Bottom Right

        return neighbours;
    }

    /**
     * Add to the neighbours list only if the neighbour is within boundaries.
     *
     * @param neighbours the neighbours arraylist
     * @param r          the row of the cell.
     * @param c          the column of the cell.
     * @return the legal neighbours of a cell.
     */
    public void addIfLegalNeighbours(ArrayList<Cell> neighbours, int r, int c) {
        if (r >= 0 && r < getRowSize() && c >= 0 && c < getColumnSize()) {
            neighbours.add(getKnownWorld()[r][c]);
        }
    }

    /**
     * Mark a cell as a Mine. Set value of cell as '*' and add the cell to the
     * marked mines ArrayList.
     * @param r the row of the cell.
     * @param c the column of the cell.
     */
    public void markCell(int r, int c) {
        knownWorld[r][c].setValue('*');
        markedMines.add(knownWorld[r][c]);
    }

    /**
     * Initialise agent's world. Agent's world with the Game's map is different.
     */
    public void initialiseAgentWorld() {
        // Start from top left.
        for (int r = 0; r < rowSize; r++) {
            for (int c = 0; c < columnSize; c++) {
                // check if the current cell is known to be blocked.
                if (containInList(r, c, blocked)) {
                    // set the value of cell in r,c to b.
                    knownWorld[r][c] = new Cell(r, c, 'b');
                } else {
                    // set the value of cell in r,c to ?.
                    knownWorld[r][c] = new Cell(r, c, '?');
                }
            }
        }
    }

    /**
     * Prints the agent's world.
     *
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
     *
     * @param r    row of the cell.
     * @param c    column of the cell.
     * @param list the list to check.
     * @return
     */
    public boolean containInList(int r, int c, ArrayList<Cell> list) {
        Cell tempCell = new Cell(r, c);
        return list.contains(tempCell);
    }

    /**
     * Returns true if the cell is covered (not uncovered, not blocked, and not a marked mine).
     * @param cell
     * @return
     */
    public boolean isCellCovered(Cell cell) {
        return !getUncovered().contains(cell) && !getBlocked().contains(cell) && !getMarkedMines().contains(cell);
    }

    /**
     * Used to print the final output.
     *
     * @param status flag to determine whether the agent won, lost or terminated.
     */
    public void printFinal(int status) {
        System.out.println("Final map");
        printAgentKnownWorld(true);
        if (status == 1) {
            System.out.println("Result: Agent alive: all solved");
        } else if (status == -1) {
            System.out.println("Result: Agent dead: found mine");
        } else if (status == 0) {
            System.out.println("Result: Agent not terminated");
        }

        System.exit(0);
    }

}
