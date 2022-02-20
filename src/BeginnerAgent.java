import java.util.ArrayList;

public class BeginnerAgent extends Agent {

    public BeginnerAgent(char[][] board, int strategy, boolean verbose) {
        super(board, strategy, verbose);
    }

    @Override
    public void probe() {
        // SINGLE POINT STRATEGY.
        uncover(0, 0);
        uncover(getCentreCell().getR(), getCentreCell().getC());

        printAgentKnownWorld(false);
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                // if cell is covered check its adjacent neighbours.
                if (!getUncovered().contains(getKnownWorld()[r][c]) && !getBlocked().contains(getKnownWorld()[r][c])) {
                    action(r, c);
                }

                System.out.println(getUncovered());
                if (getGame().isGameWon(getUncovered().size(),getMarkedMines().size())) {
                    printFinal(1);
                }
            }
        }

        printFinal(0);
    }

    public void action(int r, int c) {
        ArrayList<Cell> adjacent = getAdjacentNeighbours(r, c);
        System.out.println("ACTION: r"+r+" c"+c);
        for (Cell neighbour : adjacent) {
            // You may probe or flag cells proven to be safe or unsafe.
            if (getUncovered().contains(neighbour) && !getMarkedMines().contains(neighbour) && neighbour.getValue() != 'b') {
                // if it is safe, then uncover cell.
                if (allFreeNeighbours(neighbour)) {
                    System.out.println("UNCOVER" +r +" "+ c);
                    uncover(r, c); // uncover cell.
                    printAgentKnownWorld(false);
                    break;
                } else if (allMarkedNeighbours(neighbour)) {
                    markCell(r, c);
                    printAgentKnownWorld(false);
                    break;
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

        return (cell.getValue() == minesCount);
    }


    private boolean allMarkedNeighbours(Cell cell) {
        int minesCount = 0;
        int coveredCount = 0;

        ArrayList<Cell> neighboursOfCell = getAdjacentNeighbours(cell.getR(), cell.getC()); // Get the adjacent neighbours of the cell.

        for (Cell neighbour : neighboursOfCell) {
            if (neighbour.getValue() == '*') {
                minesCount++;
            } else if (neighbour.getValue() == '?') {
                coveredCount++;
            }
        }

        int clue = Integer.parseInt(String.valueOf(cell.getValue()));
//        System.out.println(cell.getR() + " ,, " + cell.getC() + "AMN" + coveredCount + " == " + cell.getValue() + " - " + minesCount);
        return (coveredCount == clue - minesCount);
    }


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
     * Discards any neighbours out of bounds.
     *
     * @param neighbours
     * @return
     */
    public void addIfLegalNeighbours(ArrayList<Cell> neighbours, int r, int c) {

        if (r >= 0 && r < getRowSize() && c >= 0 && c < getColumnSize()) {
            neighbours.add(getKnownWorld()[r][c]);
        }
    }

}
