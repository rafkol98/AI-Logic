import java.util.ArrayList;

public class BasicAgent {

    //TODO: make private

    private Game game;
    private boolean verbose;
    private int strategy;

    private char[][] knownWorld;
    private ArrayList<Cell> blocked; // the agent must know the blocked cells.
    private Cell topLeft;
    private Cell centreCell;
    private int numberOfMines;

    private ArrayList<Cell> probed;
    private ArrayList<Cell> mines;

    private int rowSize, columnSize;

    //TODO: implement strategy & verbose!
    public BasicAgent(char[][] board, int strategy, boolean verbose) {
        this.strategy = strategy;
        this.verbose = verbose;
        game = new Game(board);
        probed = new ArrayList();
        mines = new ArrayList<>();
        getKnowledgeBase();
        probe();
    }

    private void getKnowledgeBase() {
        blocked = game.getBlockedCells();
        topLeft = game.getTopLeftCell();
        centreCell = game.getCentreOfBoardCell();
        numberOfMines = game.getTotalNumberMines();
        rowSize = game.getBoardRowSize();
        columnSize = game.getBoardColumnSize();
        knownWorld = new char[rowSize][columnSize];
        initialiseAgentWorld();
    }

    private void probe() {
        if (strategy == 1) {
            probeInOrder();
        }
    }

    /**
     * Probe in order.
     */
    private void probeInOrder() {
        printAgentKnownWorld();
        // Iterate agent world.
        for (int i = 0; i < knownWorld.length; i++) {
            for (int j = 0; j < knownWorld[0].length; j++) {
                // Check only NON-BLOCKED cells and those NOT-UNCOVERED yet.
                if (knownWorld[i][j] != 'b' && knownWorld[i][j] == '?') {
                    // Uncover the probed cell.
                    uncover(i, j);

                    if (game.isGameWon(probed.size())) {
                        System.out.println("WON!");
                        printAgentKnownWorld();
                        System.exit(0);
                    } else {
                        //TODO: test3 does not agree! fix the location that you print.
                        printAgentKnownWorld();
                    }

                }
            }
        }
    }

    /**
     * Uncover the cell just probed.
     */
    private void uncover(int i, int j) {
        // uncover only cells that are within the board's borders.
        if (i >= 0 && i < rowSize && j >= 0 && j < rowSize) {
            // Ask game about the value of the same coordinates but from real board.
            Cell probedCell = game.getCell(i, j);
            // if the probed cell is not a blocked cell and was not already probed, then proceed to uncover it to the agent.
            if (!blocked.contains(probedCell) && !probed.contains(probedCell)) {
                probed.add(probedCell);

                // if the probed cell is a mine, then the agent lost.
                if (probedCell.isMine()) {
                    knownWorld[probedCell.getR()][probedCell.getC()] = '-';
                    printAgentKnownWorld();
                    game.foundMine();
                } else {
                    // if the value of the probed cell is 0, then uncover adjacent cells.
                    if (probedCell.getValue() == '0') {
                        knownWorld[probedCell.getR()][probedCell.getC()] = probedCell.getValue(); // uncover cell.
                        uncoverAdjacentCells(i, j);  // Uncover adjacent cells.
                    }
                    // Uncover the value of the probed cell (agent knows the value).
                    else {
                        knownWorld[probedCell.getR()][probedCell.getC()] = probedCell.getValue();
                    }
                }
            }

        }
    }

    /**
     * If the cell contains a value 0 meaning that no adjacent cells contain mines,
     * all the non- blocked neighbouring cells will also be uncovered.
     *
     * @param i
     * @param j
     */
    private void uncoverAdjacentCells(int i, int j) {
        uncover(i - 1, j - 1); // Top Left
        uncover(i - 1, j); // Top Center
        uncover(i - 1, j + 1); // Top Right

        uncover(i, i - 1); // Left
        uncover(i, i + 1); // Right

        uncover(i + 1, i - 1); // Bottom left
        uncover(i + 1, i); // Bottom
        uncover(i + 1, i + 1); // Bottom Right
    }

    //TODO: flag cell when thinking mine is found.
    private void flagCell(Cell cell) {

    }

    private void initialiseAgentWorld() {
        // Start from top left.
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                Cell current = new Cell(i, j);
                // check if the current cell is known to be blocked.
                if (blocked.contains(current)) {
                    // set the value of char in i,j to b.
                    knownWorld[i][j] = 'b';
                } else {
                    // set the value of char in i,j to b.
                    knownWorld[i][j] = '?';
                }
            }
        }
    }

    /**
     * Prints the agent's world.
     */
    public void printAgentKnownWorld() {
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
                System.out.print(knownWorld[i][j] + " ");// value in the board
            }
            System.out.println();
        }
        System.out.println();
    }


}
