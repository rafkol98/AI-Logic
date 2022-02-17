import java.util.ArrayList;

public class BeginnerAgent extends Agent {

    public BeginnerAgent(char[][] board, int strategy, boolean verbose) {
        super(board, strategy, verbose);
    }

    @Override
    public void probe() {
        // SINGLE POINT STRATEGY.
        printAgentKnownWorld(false);

        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                // if cell is covered check its adjacent neighbours.
                if (!getProbed().contains(getKnownWorld()[r][c])) {
                    action(r, c);
                }

//                if (getGame().isGameWon(getProbed().size())) {
//                    printFinal(true);
//                } else {
//                    printAgentKnownWorld(false);
//                }
            }
        }
    }

    public void action(int r, int c) {
        ArrayList<Cell> adjacent = getAdjacentNeighbours(r, c);
        for (Cell neighbour : adjacent) {
            // You may probe or flag cells proven to be safe or unsafe.
            if (!getProbed().contains(neighbour) || getMarkedMines().contains(neighbour)) {
                System.out.println("MESA SS "+ neighbour.getR() +" .. " +neighbour.getC() );
                // if it is safe, then uncover cell.
                if(allFreeNeighbours(neighbour)) {
//                    System.out.println("M");
                    uncover(r, c); // uncover cell.
                } else if (allMarkedNeighbours(neighbour)){
                    flagCell(r, c);
                }
            }
        }
    }

    private boolean allFreeNeighbours(Cell cell) {
        int minesCount = 0;
        ArrayList<Cell> neighboursOfCell = getAdjacentNeighbours(cell.getR(), cell.getC()); // Get the adjacent neighbours of the cell.
        for (Cell neighbour : neighboursOfCell) {
            if (neighbour.getValue() == '*') {
                minesCount++;
            }
        }
        System.out.println("AFN: mines count" + minesCount +"cell value:" +cell.getValue());

        return  (cell.getValue() == minesCount);
    }

    private boolean allMarkedNeighbours(Cell cell) {
        int minesCount = 0;
        int unmarkedCount = 0;
        ArrayList<Cell> neighboursOfCell = getAdjacentNeighbours(cell.getR(), cell.getC()); // Get the adjacent neighbours of the cell.

        for (Cell neighbour : neighboursOfCell) {
            if (neighbour.getValue() == '*') {
                minesCount++;
            }

            if (neighbour.getValue() == '?') {
                unmarkedCount++;
            }
        }

        System.out.println("AMN"+unmarkedCount + " == "+ cell.getValue() + " - "+ minesCount);
        return (unmarkedCount == cell.getValue() - minesCount);
    }

    public ArrayList<Cell> getAdjacentNeighbours(int r, int c) {
        ArrayList<Cell> neighbours = new ArrayList<>();

        addIfLegalNeighbours(neighbours,r - 1,c - 1); // Top Left
        addIfLegalNeighbours(neighbours,r - 1,c); // Top Center
        addIfLegalNeighbours(neighbours,r - 1,c + 1); // Top Right

        addIfLegalNeighbours(neighbours,r,c - 1); // Left
        addIfLegalNeighbours(neighbours,r,c + 1); // Right

        addIfLegalNeighbours(neighbours,r + 1,c - 1); // Bottom Left
        addIfLegalNeighbours(neighbours,r + 1,c); // Bottom
        addIfLegalNeighbours(neighbours,r + 1,c + 1); // Bottom Right

        return neighbours;
    }

    /**
     * Discards any neighbours out of bounds.
     * @param neighbours
     * @return
     */
    public void addIfLegalNeighbours(ArrayList<Cell> neighbours, int r, int c) {

        if (r >= 0 && r < getRowSize() && c >= 0 && c < getColumnSize()) {
            neighbours.add(getKnownWorld()[r][c]);
        }
    }

}
