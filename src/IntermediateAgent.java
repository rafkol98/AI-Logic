import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.util.ArrayList;

/**
 * The IntemediateAgent class. Uses logical inference in DNF.
 *
 * @author: 210017984
 */
public class IntermediateAgent extends BeginnerAgent {

    // Initialise logic variables.
    public final String AND = "&";
    public final String OR = "|";
    public final String NOT = "~";

    /**
     * Create agent instance.
     *
     * @param board   the board passed in.
     * @param verbose whether to print every step
     * @param agentNo the solving agent number.
     * @param inferencesFlag measure inferences required to reach goal.
     */
    public IntermediateAgent(char[][] board, boolean verbose, int agentNo, boolean inferencesFlag) {
        super(board, verbose, agentNo, inferencesFlag);
    }

    /**
     * Probe cells first using SPS and then if it cannot make an inference about a cell use SAT.
     */
    @Override
    public void probe() {
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                Cell cell = getKnownWorld()[r][c];

                // first try SPS.
                if (getCovered().contains(cell)) {
                    sps(r, c);
                }

                // If SPS does not work, try alternative - DNF (or CNF if in IntermediateAgentCNF).
                if (getCovered().contains(cell)) {
                    alternative(cell);
                }

                // if the cell is still covered (no changes), then increment counter.
                if (getCovered().contains(cell)) {
                    counter++;
                }
            }
        }
    }

    /**
     * This method is called when the sps technique (implemented in the BeginnerAgent) can
     * make no other deductions. It uses the DNF encoding technique.
     */
    @Override
    public void alternative(Cell cell) {
        // DNF Encoding Technique.
        ArrayList<Cell> cells = getSuitableCells();

        String kbu = createKBU(cells);  // create KBU.

        // determine if passed in cell is a mine or not.
        if (proveMineOrFree(cell, kbu, true));
        // Otherwise try to prove that cell is free.
        else {
            proveMineOrFree(cell, kbu, false);
        }
    }

    /**
     * Get all the cells that are uncovered but have at least one neighbour that is covered.
     * Those are the cells that we will be using inference to explore further.
     *
     * Used for Logical inference and building the KBU in both intermediate agents.
     * @return all the suitable cells in an ArrayList.
     */
    public ArrayList<Cell> getSuitableCells() {
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

    /**
     * Create a Knowledge base of the unknowns (KBU) based on the suitable cells.
     *
     * @param cells the suitable cells for further exploration.
     * @return the KBU as a string.
     */
    public String createKBU(ArrayList<Cell> cells) {
        String kbu = "";

        // Add the logic options for each cell in the KBU.
        for (int i = 0; i < cells.size(); i++) {
            String logicOptions = getLogic(cells.get(i));
            kbu += logicOptions; // add logic option in kbu for current cell.

            // Connect the logic options.
            // If its the last element, then don't add an AND sign.
            if (i != cells.size() - 1 && logicOptions.length() >= 1) {
                kbu += " " + AND + " ";
            }
        }

        return kbu;
    }

    /**
     * Determine whether the passed in cell should be uncovered or marked as mine - using Logical Inference.
     * @return true if there was a change.
     */
    public boolean proveMineOrFree(Cell cell, String kbu, boolean proveMine) {
        String entailment;
        String tempKBU = "";

        FormulaFactory f = new FormulaFactory();
        PropositionalParser p = new PropositionalParser(f);

        // if the proveMine flag is true, then we want to entail whether the cell is a mine.
        if (proveMine) {
            entailment = " " + AND + " " + NOT + "M" + cell.getR() + cell.getC();
        } else {
            entailment = " " + AND + " M" + cell.getR() + cell.getC();
        }

        tempKBU = kbu + entailment;

        try {
            Formula formula = p.parse(tempKBU); // parse temporary KBU (includes entailment) in a formula.
            SATSolver miniSat = MiniSat.miniSat(f); // initialise SAT solver.
            miniSat.add(formula); // add the formula to the miniSAT solver.
            Tristate result = miniSat.sat(); // Get the entailement result.

            boolean entailed = result.equals(Tristate.TRUE) ? true : false; // create a boolean variable to determine entailment.

            // if prove mine flag is true, then try
            if (proveMine) {
                return markCell(entailed, cell); // try to mark cell - depending on the inference made by LogicNG.
            } else {
                return uncoverCell(entailed, cell); // try to uncover  cell - depending on the inference made by LogicNG.
            }

        } catch (ParserException e) {
            e.getMessage();
        }

        return false;
    }

    /**
     * Get all the possible sets of size equal to the remaining mines count.
     *
     * @param coveredNeighbours the neighbours that are covered.
     * @param setSize           if DNF the set size is equal to the count of mines. If CNF then the set size is equal to 2.
     * @return an ArrayList containing an inner ArrayList with all the possible sets.
     */
    public ArrayList<ArrayList<Cell>> minesPossibleSets(ArrayList<Cell> coveredNeighbours, int setSize) {
        ArrayList<ArrayList<Cell>> possibleMinesSets = permutations(coveredNeighbours);

        possibleMinesSets.removeIf(set -> set.size() != setSize); // remove the sets that do not have the mines count size.

        return possibleMinesSets;
    }

    /**
     * Get all the possible permutations for given covered neighbours.
     *
     * @param coveredNeighbours the covered neighbours given.
     * @return all the possible permutations
     */
    private ArrayList<ArrayList<Cell>> permutations(ArrayList<Cell> coveredNeighbours) {
        ArrayList<ArrayList<Cell>> sets = new ArrayList<ArrayList<Cell>>();

        // When empty, return sets.
        if (coveredNeighbours.isEmpty()) {
            sets.add(new ArrayList<Cell>());
            return sets;
        }

        fillInnerSets(sets, coveredNeighbours); // fill the inner sets

        return sets;
    }

    /**
     * Fill inner sets with the appropriate elements.
     *
     * @param sets              the sets to be filled.
     * @param coveredNeighbours the covered neighbours given.
     */
    private void fillInnerSets(ArrayList<ArrayList<Cell>> sets, ArrayList<Cell> coveredNeighbours) {
        Cell top = coveredNeighbours.get(0);
        ArrayList<Cell> remaining = new ArrayList<Cell>(coveredNeighbours.subList(1, coveredNeighbours.size()));

        // recursive call.
        for (ArrayList<Cell> set : permutations(remaining)) {
            ArrayList<Cell> innerSet = new ArrayList<Cell>(); // create an inner set.

            innerSet.add(top); // add the top.
            innerSet.addAll(set); // add all elements of the permutations.

            sets.add(innerSet); // add inner set in bigger set.
            sets.add(set); // add set to sets.
        }
    }

    /**
     * Get all the options available from a cell in a logic sentence.
     *
     * @param cell the cell to get the logic options of.
     * @return the options of that cell in a logic sentence.
     */
    public String getLogic(Cell cell) {
        String logicOptions = "";
        ArrayList<Cell> coveredNeighbours = getOnlyCoveredNeighbours(cell.getR(), cell.getC()); // get covered neighbours.
        int numberOfMarkedMinesNeighbours = getNumberOfMinesMarkedNeighbours(cell.getR(), cell.getC()); // get number of marked mines in neighbours.

        // Unmarked mines = clue - numberOfMarkedMines.
        // get number of mines not marked yet.
        int remainingMines = cell.getValueInt() - numberOfMarkedMinesNeighbours;
        ArrayList<ArrayList<Cell>> minesPosSets = minesPossibleSets(coveredNeighbours, remainingMines);

        if (minesPosSets.size() > 0) {
            // Initialise logic connectors.
            logicOptions += "(";
            String andInner = " " + AND + " ";
            String connectOptions = " " + OR + " (";

            logicOptions += "(";

            // iterate through the inner sets.
            for (int i = 0; i < minesPosSets.size(); i++) {
                // Connect the different options with an OR.
                if (i != 0) {
                    logicOptions += connectOptions;
                }

                // Iterate through the covered neighbours, to add inner element combinations.
                for (int x = 0; x < coveredNeighbours.size(); x++) {
                    Cell neighbour = coveredNeighbours.get(x); // get neighbour.
                    // Connect the different options with an AND (if not the beginning).
                    if (x != 0) {
                        logicOptions += andInner;
                    }
                    // Check if the current set does not contain neighbour. If not then append the NOT symbol.
                    if (!minesPosSets.get(i).contains(neighbour)) {
                        logicOptions += NOT;
                    }
                    // Add cell in the logic options string.
                    logicOptions += "M" + neighbour.getR() + neighbour.getC();
                }
                logicOptions += ")";
            }
            logicOptions += ")";
        }
        return logicOptions;
    }

    /**
     * Uncover or mark cells, depending on the result returned by LogicNG.
     *
     * @param entailment the result returned by LogicNG.
     * @param cell       the cell to be uncovered or marked.
     * @return true if cell was uncovered, false otherwise.
     */
    public boolean uncoverCell(boolean entailment, Cell cell) {
        // if result equals FALSE, then the cell is safe, uncover.
        if (!entailment) {
            uncover(cell.getR(), cell.getC()); // uncover cell.
            worldChangedOuput();
            return true;
        }
        return false;
    }

    /**
     * Marks a cell as a mine.
     *
     * @param entailment the result returned by LogicNG.
     * @param cell       the cell to be uncovered or marked.
     * @return true if cell was marked, false otherwise.
     */
    public boolean markCell(boolean entailment, Cell cell) {
        // if result equals FALSE, then mark as danger!
        if (!entailment) {
            markCell(cell.getR(), cell.getC()); // mark cell.
            worldChangedOuput();
            return true;
        }
        return false;
    }

}