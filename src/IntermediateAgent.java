import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.util.ArrayList;

public class IntermediateAgent extends BeginnerAgent {


    private final String AND = "&";
    private final String OR = "|";
    private final String NOT = "~";


    public IntermediateAgent(char[][] board, boolean verbose) {
        super(board, verbose);
    }

    public static ArrayList<ArrayList<Cell>> minesPossibleSets(ArrayList<Cell> coveredNeighbours, int minesCount) {
        ArrayList<ArrayList<Cell>> possibleMinesSets = permutations(coveredNeighbours);

        possibleMinesSets.removeIf(set -> set.size() != minesCount); // remove the sets that do not have the mines count size.

        return possibleMinesSets;
    }

    public static ArrayList<ArrayList<Cell>> permutations(ArrayList<Cell> coveredNeighbours) {
        ArrayList<ArrayList<Cell>> sets = new ArrayList<ArrayList<Cell>>();
        if (coveredNeighbours.isEmpty()) {
            sets.add(new ArrayList<Cell>());
            return sets;
        }

        fillInnerSets(sets, coveredNeighbours);

        return sets;
    }

    public static void fillInnerSets(ArrayList<ArrayList<Cell>> sets, ArrayList<Cell> coveredNeighbours) {
        Cell top = coveredNeighbours.get(0);
        ArrayList<Cell> remaining = new ArrayList<Cell>(coveredNeighbours.subList(1, coveredNeighbours.size()));

        for (ArrayList<Cell> set : permutations(remaining)) {
            ArrayList<Cell> innerSet = new ArrayList<Cell>(); // create an inner set.

            innerSet.add(top); // add the top.
            innerSet.addAll(set); // add all elements of the permutations.

            sets.add(innerSet); // add inner set in bigger set.
            sets.add(set); // add set to sets.
        }
    }

    // If SPS fails, then use alternative approach, DNF.
    @Override
    public void alternative() {
        super.alternative();

        // Get cells that are already uncovered and have at least one covered neighbour.
        // They will be used to find their logical options and build our KB.
        ArrayList<Cell> cells = getSuitableCells();

        String kbu = createKBU(cells); // create KBU.

        prove(kbu);

    }

    public void prove(String kbu) {

        ArrayList<Cell> covered = getCovered(); // get covered cells.

        for (Cell cell : covered) {
            String tempKBU = "";
            FormulaFactory f = new FormulaFactory();
            PropositionalParser p = new PropositionalParser(f);

            System.out.println("MESA ");
            String entailment = " " + AND + " M" + cell.getR() + cell.getC();
            tempKBU = kbu + entailment;
            System.out.println(tempKBU);
            try {
                Formula formula = p.parse(tempKBU);
                SATSolver miniSat = MiniSat.miniSat(f);
                miniSat.add(formula);

                Tristate result = miniSat.sat();

                System.out.println(result);

                // if result equals TRUE, then mark as danger!
                if (result.equals(Tristate.TRUE)) {
                    markCell(cell.getR(), cell.getC()); // mark cell.
                    printAgentKnownWorld(false);

                }
                // if result equals FALSE, then the cell is safe, uncover.
                else if(result.equals(Tristate.FALSE)) {
                    uncover(cell.getR(), cell.getC()); // uncover cell.
                    printAgentKnownWorld(false);
                }

            } catch (ParserException e) {
                System.out.println("There was a problem parsing the formula passed in");
            }

        }


    }

    /**
     * Create a Knowledge base of the unknowns (KBU).
     *
     * @param cells
     * @return
     */
    public String createKBU(ArrayList<Cell> cells) {
        String kbu = "";

        // Add the logic options for each cell in the KBU.
        for (int i = 0; i < cells.size(); i++) {
            kbu += getLogicOptions(cells.get(i));

            // Connect the logic options. If its the last element, then don't add an
            // AND sign.
            if (i != cells.size() - 1) {
                kbu += " " + AND + " ";
            }
        }

        return kbu;
    }

    // TODO: improve comments.
    public String getLogicOptions(Cell cell) {
        // Initialise logic connectors.
        String logicOptions = "(";
        String andInner = " " + AND + " ";
        String connectOptions = " " + OR + " (";

        ArrayList<Cell> coveredNeighbours = getOnlyCoveredNeighbours(cell.getR(), cell.getC()); // get covered neighbours.
        int numberOfMarkedMinesNeighbours = getNumberOfMinesMarkedNeighbours(cell.getR(), cell.getC()); // get number of marked mines in neighbours.

        // Unmarked mines = clue - numberOfMarkedMines.
        int remainingMines = Integer.parseInt(String.valueOf(cell.getValue())) - numberOfMarkedMinesNeighbours;

        ArrayList<ArrayList<Cell>> minesPosSets = minesPossibleSets(coveredNeighbours, remainingMines);

        if (minesPosSets.size() > 0) {
            logicOptions += "(";

            // iterate through the sets.
            for (int i = 0; i < minesPosSets.size(); i++) {
                // Connect the different options with an OR.
                if (i != 0) {
                    logicOptions += connectOptions;
                }

                // Iterate through the covered neighbours, to add inner elements.
                for (int x = 0; x < coveredNeighbours.size(); x++) {
                    Cell neighbour = coveredNeighbours.get(x); // get neighbour.
                    // Connect the different options with an AND.
                    if (x != 0) {
                        logicOptions += andInner;
                    }
                    // Check the bigger set if it does not contain neighbour. If not then append the NOT symbol.
                    if (!minesPosSets.get(i).contains(neighbour)) {
                        logicOptions += NOT;
                    }
                    // Add cell in the logic options string.
                    logicOptions += "M" + neighbour.getR() + neighbour.getC();
                }
                logicOptions += ")";
            }
        }
        logicOptions += ")";
        return logicOptions;
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
                if (getOnlyCoveredNeighbours(r, c).size() >= 1 && getUncovered().contains(getKnownWorld()[r][c])) {
                    cells.add(getKnownWorld()[r][c]); // cell is suitable.
                }
            }
        }

        return cells;
    }

    //TODO: move them to AGENT class.
    public ArrayList<Cell> getOnlyCoveredNeighbours(int r, int c) {
        ArrayList<Cell> coveredNeighbours = new ArrayList<>();

        ArrayList<Cell> neighbours = getAdjacentNeighbours(r, c);
        for (Cell neighbour : neighbours) {
            if (isCellCovered(neighbour)) {
                coveredNeighbours.add(neighbour);
            }
        }

        return coveredNeighbours;
    }

    /**
     * Get the number of mines that are marked in the neighbouring cells.
     *
     * @param r
     * @param c
     * @return
     */
    public int getNumberOfMinesMarkedNeighbours(int r, int c) {
        int minesMarked = 0;

        ArrayList<Cell> neighbours = getAdjacentNeighbours(r, c);
        for (Cell neighbour : neighbours) {
            if (getKnownWorld()[neighbour.getC()][neighbour.getR()].getValue() == '*') {
                minesMarked++;
            }
        }

        return minesMarked;
    }

}
