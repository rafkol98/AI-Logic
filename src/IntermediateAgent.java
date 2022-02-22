import org.logicng.formulas.FormulaFactory;

import java.util.ArrayList;

public class IntermediateAgent extends BasicAgent {
//    public class IntermediateAgent extends BeginnerAgent {

    protected final FormulaFactory f = new FormulaFactory();

    public IntermediateAgent(char[][] board, int strategy, boolean verbose) {
        super(board, strategy, verbose);
        solve();
    }

    public void solve() {
        //WHILE LOOP

        // Try solving with SPS

        // if cannot make more inferences -> try with DNF.
    }

    public void dnf() {
        // Get cells that are already uncovered and have at least one covered neighbour.
        // They will be used to find their logical options and build our KB.
        ArrayList<Cell> cells = getSuitableCells();


    }


    public void createKBU(ArrayList<Cell> cells) {
        ArrayList<String> logicOptions = new ArrayList<>();

        for (Cell cell : cells) {
            logicOptions.add(getLogicOptions(cell));
        }

    }

    //MAYBE ARRAYLIST OF ARRAYLISTS.

    public String getLogicOptions(Cell cell) {
        String logicOptions = "";

        ArrayList<Cell> coveredNeighbours = getOnlyCoveredNeighbours(cell.getR(),cell.getC());
        int numberOfMarkedMinesNeighbours = getNumberOfMinesMarkedNeighbours(cell.getR(), cell.getC());

        // Unmarked mines = clue - numberOfMarkedMines.
        int remainingMines = cell.getValue() - numberOfMarkedMinesNeighbours;

        ArrayList<ArrayList<Cell>> minesPosSets = minesPossibleSets(coveredNeighbours, remainingMines);

        if (minesPosSets.size() > 0) {
            logicOptions += "(";
            String andInner = " & ";
            String connectOptions = " | (";

            for (int i = 0; i < minesPosSets.size(); i++) {
                if (i != 0) {
                    logicOptions += connectOptions;
                }
                for (int x = 0; x < coveredNeighbours.size(); x++) {
                    Cell neighbour = coveredNeighbours.get(x);
                    if (x != 0) {
                        logicOptions += andInner;
                    }
                    if (!minesPosSets.get(i).contains(neighbour)) {
                        logicOptions += "~";
                    }
                    logicOptions += "M" + neighbour.getR() + neighbour.getC();
                }
                logicOptions += ")";
            }
        }

        return logicOptions;
    }


    public static ArrayList<ArrayList<Cell>> minesPossibleSets(ArrayList<Cell> coveredNeighbours, int minesCount) {
        ArrayList<ArrayList<Cell>> possibleMinesSets = permutations(coveredNeighbours);

        possibleMinesSets.removeIf(set -> set.size() != minesCount); // remove the sets that do not have the mines count size.

        return possibleMinesSets;
    }

    public static ArrayList<ArrayList<Cell>> permutations(ArrayList<Cell> coveredNeighbours) {
        ArrayList<ArrayList<Cell>> sets = new ArrayList<ArrayList<Cell>>();
        if (coveredNeighbours.isEmpty()) {
            System.out.println("mesa");
            sets.add(new ArrayList<Cell>());
            return sets;
        }

        fillInnerSets(sets,coveredNeighbours);

        return sets;
    }

    public static void fillInnerSets(ArrayList<ArrayList<Cell>> sets, ArrayList<Cell> coveredNeighbours) {
        Cell top = coveredNeighbours.get(0);
        ArrayList<Cell> remaining = new ArrayList<Cell>(coveredNeighbours.subList(1, coveredNeighbours.size()));

        for (ArrayList<Cell> set : permutations(remaining)) {
            ArrayList<Cell> innerSet = new ArrayList<Cell>(); // create an inner set.

            innerSet.add(top); // add the top.
            innerSet.addAll(set); // add all elements of the permutations.

            sets.add(innerSet); // add
            sets.add(set);
        }
    }

    public void proveMine() {

    }

    public void proveNotMine() {

    }

    //TODO: think of better name.

    /**
     * Get all the cells that are uncovered but have at least one neighbour that is covered.
     */
    private ArrayList<Cell> getSuitableCells() {
        ArrayList<Cell> cells = new ArrayList<>();
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                // check if the cell has at least one covered neighbour and is uncovered.
                if(getOnlyCoveredNeighbours(r,c).size() >= 1 && getUncovered().contains(getKnownWorld()[r][c])) {
                    cells.add(getKnownWorld()[r][c]); // cell is suitable.
                }
            }
        }

        return cells;
    }


    //TODO: move them to AGENT class.
    public ArrayList<Cell> getOnlyCoveredNeighbours(int r, int c) {
        ArrayList<Cell> coveredNeighbours = new ArrayList<>();

        ArrayList<Cell> neighbours = getAdjacentNeighbours(r,c);
        for (Cell neighbour : neighbours) {
            if (isCellCovered(neighbour)) {
                coveredNeighbours.add(neighbour);
            }
        }

        return coveredNeighbours;
    }

    /**
     * Get the number of mines that are marked in the neighbouring cells.
     * @param r
     * @param c
     * @return
     */
    public int getNumberOfMinesMarkedNeighbours(int r, int c) {
        int minesMarked = 0;

        ArrayList<Cell> neighbours = getAdjacentNeighbours(r,c);
        for (Cell neighbour : neighbours) {
            if (getKnownWorld()[neighbour.getC()][neighbour.getR()].getValue() == '*') {
                minesMarked++;
            }
        }

        return minesMarked;
    }




}
