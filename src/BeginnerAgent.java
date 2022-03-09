import java.util.ArrayList;

public class BeginnerAgent extends Agent {

    /**
     * Create agent instance.
     *
     * @param board   the board passed in.
     * @param verbose whether to print every step
     * @param agentNo the solving agent number.
     * @param inferencesFlag measure inferences required to reach goal.
     */
    public BeginnerAgent(char[][] board, boolean verbose, int agentNo, boolean inferencesFlag) {
        super(board, verbose, agentNo, inferencesFlag);
    }

    //Not used for beginner agent.
    @Override
    public void alternative(Cell cell) {}

    //Not used for beginner agent.
    @Override
    public void markAtTheEnd() {}

    /**
     * Probe cells using the Single Point Strategy.
     */
    @Override
    public void probe() {
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                Cell cell = getKnownWorld()[r][c];

                // if cell is covered check its adjacent neighbours.
                if (getCovered().contains(cell)) {
                    sps(r, c);
                }

                // if the cell is still covered (no changes), then increment counter.
                if (getCovered().contains(cell)) {
                    counter++;
                }
            }
        }
    }

    /**
     * Used to uncover or marka the cell located in the row and columns passed in using the AFN and AMN
     * techniques.
     *
     * @param r the row passed in.
     * @param c the column passed in.
     */
    public void sps(int r, int c) {
        ArrayList<Cell> adjacent = getAdjacentNeighbours(r, c);
        for (Cell neighbour : adjacent) {
            // You may probe or flag cells proven to be safe or unsafe.
            if (getUncovered().contains(neighbour) && !getMarkedMines().contains(neighbour) && !getBlocked().contains(neighbour)) {
                // if it is safe, then uncover cell.
                if (allFreeNeighbours(neighbour)) {
                    uncover(r, c); // uncover cell.
                    worldChangedOuput();
                    break;
                }
                // otherwise try to mark it as a cell. If agentNo == 5 then don't execute it (NoFlag strategy).
                else if (allMarkedNeighbours(neighbour) && getAgentNo() != 5) {
                    markCell(r, c);
                    worldChangedOuput();
                    break;
                }
            }
        }
    }

    //TODO: check comments again!! was not careful the first time writing them.

    /**
     * The AFN technique is used to determine if its safe to uncover the cell
     * based on the neighbours of its neighbour passed in.
     *
     * @param cell the neighbouring cell being currently examined.
     * @return true if all the mines were already found
     */
    private boolean allFreeNeighbours(Cell cell) {
        int minesCount = 0;
        // iterate through the neighbours of the passed in cell and count the
        // neighbours marked as mines.
        ArrayList<Cell> neighboursOfCell = getAdjacentNeighbours(cell.getR(), cell.getC()); // Get the adjacent neighbours of the cell.
        for (Cell neighbour : neighboursOfCell) {
            if (neighbour.getValue() == '*') {
                minesCount++;
            }
        }
        int clue = cell.getValueInt();
        return (clue == minesCount);
    }

    /**
     * The AMN technique is used to mark cells where we think there might
     * be a mine.
     *
     * @param cell the neighbouring cell being currently examined.
     * @return true if
     */
    private boolean allMarkedNeighbours(Cell cell) {
        int minesCount = 0;
        int coveredCount = 0;

        ArrayList<Cell> neighboursOfCell = getAdjacentNeighbours(cell.getR(), cell.getC()); // Get the adjacent neighbours of the cell.

        // iterate through the neighbours of the passed in cell and count the
        // number of covered cells and the ones marked as mines.
        for (Cell neighbour : neighboursOfCell) {
            if (neighbour.getValue() == '*') {
                minesCount++;
            } else if (neighbour.getValue() == '?') {
                coveredCount++;
            }
        }

        int clue = cell.getValueInt(); // make the clue being an integer.
        return (coveredCount == clue - minesCount);
    }

}