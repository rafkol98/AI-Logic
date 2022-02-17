import java.util.ArrayList;

public class BeginnerAgent extends Agent {

    public BeginnerAgent(char[][] board, int strategy, boolean verbose) {
        super(board, strategy, verbose);
    }

    @Override
    public void probe() {
        // SINGLE POINT STRATEGY.
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                if (!getProbed().contains(getKnownWorld()[r][c])) {
                    action(r, c);
                }
            }
        }
    }

    public void action(int r, int c) {
        ArrayList<Cell> adjacent = getAdjacentNeighbours(r, c);
        for (Cell neighbour : adjacent) {
            if (!getProbed().contains(neighbour) || getMarkedMines().contains(neighbour)) {

            }
        }
    }

//    private boolean allFree(Cell cell) {
//
//    }

    public ArrayList<Cell> getAdjacentNeighbours(int r, int c) {
        ArrayList<Cell> neighbours = new ArrayList<>();

        neighbours.add(getKnownWorld()[r - 1][c - 1]); // Top Left
        neighbours.add(getKnownWorld()[r - 1][c]); // Top Center
        neighbours.add(getKnownWorld()[r - 1][c + 1]); // Top Right

        neighbours.add(getKnownWorld()[r][c - 1]); // Left
        neighbours.add(getKnownWorld()[r][c + 1]); // Right

        neighbours.add(getKnownWorld()[r + 1][c - 1]); // Bottom Left
        neighbours.add(getKnownWorld()[r + 1][c]); // Bottom
        neighbours.add(getKnownWorld()[r + 1][c + 1]); // Bottom Right

        return keepOnlyLegalNeighbours(neighbours);
    }

    /**
     * Discards any neighbours out of bounds.
     * @param neighbours
     * @return
     */
    public ArrayList<Cell> keepOnlyLegalNeighbours(ArrayList<Cell> neighbours) {
        ArrayList<Cell> legalNeighbours = new ArrayList<>();

        // Iterate through the neighbours and check if they are in bounds of the map.
        for (Cell neighbour : neighbours) {
            if (neighbour.getR() >= 0 && neighbour.getR() < getRowSize() && neighbour.getC() >= 0 && neighbour.getC() < getColumnSize()) {
                legalNeighbours.add(neighbour);
            }
        }

        return legalNeighbours;
    }


}
