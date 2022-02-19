import java.util.ArrayList;

public class BeginnerAgent extends Agent {

    public BeginnerAgent(char[][] board, int strategy, boolean verbose) {
        super(board, strategy, verbose);
    }

    @Override
    public void probe() {
        // SINGLE POINT STRATEGY.
        uncover(0,0);
        printAgentKnownWorld(false);
//        System.out.println("probed: "+getProbed().size());
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                System.out.println("mesa sto probe" + r+","+c);
                // if cell is covered (not probed) check its adjacent neighbours.
                if (!getProbed().contains(getKnownWorld()[r][c])) {
                    action(r, c);
                }
            }
        }
    }

    public void action(int r, int c) {
        System.out.println(r+"---"+c);
        ArrayList<Cell> adjacent = getAdjacentNeighbours(r, c);
        for (Cell neighbour : adjacent) {
            // You may probe or flag cells proven to be safe or unsafe.
//            if (!getProbed().contains(neighbour) || getMarkedMines().contains(neighbour)) {
                System.out.println("neighbour: "+neighbour.getR() + ", "+ neighbour.getC());
                // if it is safe, then uncover cell.
                if(allFreeNeighbours(neighbour)) {
                    uncover(r, c); // uncover cell.
                    printAgentKnownWorld(false);
                } else if (allMarkedNeighbours(neighbour)){
                    markCell(r, c);
                    printAgentKnownWorld(false);
                }
//            }
        }
    }

    private boolean allFreeNeighbours(Cell cell) {
        int minesCount = 0;

//        System.out.println(cell.getR()+" , "+cell.getC());
        ArrayList<Cell> neighboursOfCell = getAdjacentNeighbours(cell.getR(), cell.getC()); // Get the adjacent neighbours of the cell.
        System.out.println("Neighbours: "+neighboursOfCell.size()+"\n");
        for (Cell neighbour : neighboursOfCell) {
            if (neighbour.getValue() == '*') {
                minesCount++;
            }
        }
//        System.out.println("AFN: mines count" + minesCount +"cell value:" +cell.getValue());

        return  (cell.getValue() == minesCount);
    }

    private boolean allMarkedNeighbours(Cell cell) {
        if (cell.getValue() != 'b' && cell.getValue() != '?' && cell.getValue() != '*') {
            int minesCount = 0;
            int unmarkedCount = 0;
            int unprobed = 0;
            //find number of unprobed.

            ArrayList<Cell> neighboursOfCell = getAdjacentNeighbours(cell.getR(), cell.getC()); // Get the adjacent neighbours of the cell.

            for (Cell neighbour : neighboursOfCell) {
                if (neighbour.getValue() == '*') {
                    minesCount++;
                } else if (neighbour.getValue() == '?') {
                    unmarkedCount++;
                }
            }
//
//            try {
//
//            }
            int a = Integer.parseInt(String.valueOf(cell.getValue()));
            System.out.println(cell.getR() + " ,, " + cell.getC() + "AMN" + unmarkedCount + " == " + cell.getValue() + " - " + minesCount);
            return (unmarkedCount + unprobed == a - minesCount);
        }
        return false;
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
