import java.util.ArrayList;

public class BeginnerAgent extends Agent {

    public BeginnerAgent(char[][] board, boolean verbose) {
        super(board, verbose);
    }

//    boolean change = false;


    @Override
    public void alternative(Cell cell) {}

    /**
     * Probe cells using the Single Point Strategy.
     */
    @Override
    public void probe() {
        uncover(0, 0);
        uncover(getCentreCell().getR(), getCentreCell().getC());
        printAgentKnownWorld(false); // print the known world by the agent.
//        sps();
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                // if cell is covered check its adjacent neighbours.
                if (getCovered().contains(getKnownWorld()[r][c])) {
                    sps(r, c);
                }
            }
        }
    }

    /**
     * Used to uncover or marka the cell located in the row and columns passed in using the AFN and AMN
     * techniques.
     * @param r the row passed in.
     * @param c the column passed in.
     */
    public void sps(int r, int c) {
        ArrayList<Cell> adjacent = getAdjacentNeighbours(r, c);
        for (Cell neighbour : adjacent) {
            // You may probe or flag cells proven to be safe or unsafe.
            //TODO: be consistent with getBlocked!!tc
            if (getUncovered().contains(neighbour) && !getMarkedMines().contains(neighbour) && neighbour.getValue() != 'b') {
                System.out.println(neighbour.getR() + " xx "+neighbour.getC());
                // if it is safe, then uncover cell.
                if (allFreeNeighbours(neighbour)) {
                    System.out.println("AFN "+r+" , "+c);
                    uncover(r, c); // uncover cell.
                    worldChangedOuput();
//                    change = true;
                    break;
                } else if (allMarkedNeighbours(neighbour)) {
                    System.out.println("AMN");
                    markCell(r, c);
                    worldChangedOuput();
//                    change = true;
                    break;
                }
            }
        }
    }

    //TODO: check comments again!! was not careful the first time writing them.
    /**
     * The AFN technique is used to determine if its safe to uncover the cell
     * based on the neighbours of its neighbour passed in.
     * @param cell the neighbouring cell being currently examined.
     * @return true if all the mines were already found
     */
    private boolean allFreeNeighbours(Cell cell) {
        int minesCount = 0;

        ArrayList<Cell> neighboursOfCell = getAdjacentNeighbours(cell.getR(), cell.getC()); // Get the adjacent neighbours of the cell.
        for (Cell neighbour : neighboursOfCell) {
            if (neighbour.getValue() == '*') {
                minesCount++;
            }
        }
        int clue = Integer.parseInt(String.valueOf(cell.getValue()));
        return (clue == minesCount);
    }

    /**
     * The AMN technique is used to mark cells where we think there might
     * be a mine.
     * @param cell the neighbouring cell being currently examined.
     * @return true if
     */
    private boolean allMarkedNeighbours(Cell cell) {
        int minesCount = 0;
        int coveredCount = 0;

        ArrayList<Cell> neighboursOfCell = getAdjacentNeighbours(cell.getR(), cell.getC()); // Get the adjacent neighbours of the cell.

        // iterate through the neighbours of the passed in cell and count the
        // number of covered cells and the ones containing mines.
        for (Cell neighbour : neighboursOfCell) {
            if (neighbour.getValue() == '*') {
                minesCount++;
            } else if (neighbour.getValue() == '?') {
                coveredCount++;
            }
        }

        int clue = Integer.parseInt(String.valueOf(cell.getValue())); // make the clue being an integer.
        return (coveredCount == clue - minesCount);
    }

}