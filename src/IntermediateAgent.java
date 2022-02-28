import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.util.ArrayList;

public class IntermediateAgent extends BeginnerAgent {

    // Initialise logic variables.
    private final String AND = "&";
    private final String OR = "|";
    private final String NOT = "~";

    boolean logicInference = false;

    public IntermediateAgent(char[][] board, boolean verbose) {
        super(board, verbose);

    }

    @Override
    public void probe() {
        uncover(0, 0);
        uncover(getCentreCell().getR(), getCentreCell().getC());
        printAgentKnownWorld(false); // print the known world by the agent.
//
//        while (!getCovered().isEmpty()) {
//            change = false;
//            logicInference = false;
//
//
//            // if sps did not make any changes (inferences) to the world, call the alternative - CNF technique.
//            if (!change) {
//                alternative();
//            }
//
//            // if both sps and CNF do not infer anything new, then break out of the system.
//            if(!change && !logicInference) {
//                break;
//            }
//        }

        while (!getCovered().isEmpty()) {
            for (int r = 0; r < getKnownWorld().length; r++) {
                for (int c = 0; c < getKnownWorld()[0].length; c++) {
                    change = false;
                    logicInference = false;

                    Cell cell = getKnownWorld()[r][c];

                    if (!getUncovered().contains(cell) && !getBlocked().contains(cell) && !getMarkedMines().contains(cell)) {
                        action(r, c);
                    }

                    if (!change) {
                        alternative(cell);
                    }
                }
            }

            if(!change && !logicInference) {
                printFinal(0);
            }
        }
    }

    //TODO: might have to change getSUITABLE CLESS To return only relevant to r,c.
    /**
     *  This method is called when the sps technique (implemented in the BeginnerAgent) can
     *  make no other decuctions. It uses the DNF encoding technique.
     */
    @Override
    public void alternative(Cell cell) {
        ArrayList<Cell> cells = getSuitableCells();
        String kbu = createKBU(cells);  // create KBU.

        if(proveMineOrFree(cell, kbu, true)) {
            System.out.println("Mine");
        }
        // determine for each covered cell whether they contain a mine or not.)
        else if (proveMineOrFree(cell, kbu, false)) {
            System.out.println("Free");
        }
    }

    /**
     * Get all the cells that are uncovered but have at least one neighbour that is covered.
     * Those are the cells that we will be using inference to explore further.
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

    /**
     * Create a Knowledge base of the unknowns (KBU) based on the suitable cells.
     *
     * @param cells the suitable cells for further exploration.
     * @return the KBU as a string.
     */
    private String createKBU(ArrayList<Cell> cells) {
        String kbu = "";

        // Add the logic options for each cell in the KBU.
        for (int i = 0; i < cells.size(); i++) {
            String logicOptions =  getLogicOptions(cells.get(i));
//            System.out.println(cells.get(i).getR() + "," + cells.get(i).getC() + " logic: "+logicOptions);
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
     * Iterate through the covered cells and determine for each one
     * whether they contain a mine or not.
     *
     */
    private boolean proveMineOrFree(Cell cell, String kbu, boolean proveMine) {
//        Cell cell = getCovered().get(0); // get first covered cell.

        if (getCovered().contains(cell)) {

            // iterate through the covered cells.
//        for (Cell cell : covered) {
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
            System.out.println("\n" + tempKBU);

            try {
                Formula formula = p.parse(tempKBU); // parse temporary KBU (includes entailment) in a formula.
                SATSolver miniSat = MiniSat.miniSat(f); // initialise SAT solver.
                miniSat.add(formula); // add the formula to the miniSAT solver.
                Tristate result = miniSat.sat(); // Get the entailement result.
                if (proveMine) {
                    return markCell(result, cell); // uncover or mark cell depending on the inference made by LogicNG.
                } else {
                    return uncoverCell(result, cell); // uncover or mark cell depending on the inference made by LogicNG.
                }

            } catch (ParserException e) {
                System.out.println(tempKBU);
                e.getCause();
                System.out.println(e.getMessage());
                System.out.println("There was a problem parsing the formula passed in");
            }
//        }
        }
        return false;
    }


    /**
     * Get all the options available from a cell in a logic sentence.
     *
     * @param cell the cell to get the logic options of.
     * @return the options of that cell in a logic sentence.
     */
    private String getLogicOptions(Cell cell) {
        String logicOptions = "";

        ArrayList<Cell> coveredNeighbours = getOnlyCoveredNeighbours(cell.getR(), cell.getC()); // get covered neighbours.
        int numberOfMarkedMinesNeighbours = getNumberOfMinesMarkedNeighbours(cell.getR(), cell.getC()); // get number of marked mines in neighbours.

        // Unmarked mines = clue - numberOfMarkedMines.
        int remainingMines = Integer.parseInt(String.valueOf(cell.getValue())) - numberOfMarkedMinesNeighbours;

        ArrayList<ArrayList<Cell>> minesPosSets = minesPossibleSets(coveredNeighbours, remainingMines);

        if (minesPosSets.size() > 0) {
            // Initialise logic connectors.
            logicOptions += "(";
            String andInner = " " + AND + " ";
            String connectOptions = " " + OR + " (";

            System.out.println(minesPosSets.size());
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
            logicOptions += ")";
        }

        return logicOptions;
    }

    /**
     * Get all the possible sets that contain given mine count.
     * @param coveredNeighbours the neighbours that are covered.
     * @param minesCount the count of mines.
     * @return an ArrayList containing an inner ArrayList with all the possible sets.
     */
    private static ArrayList<ArrayList<Cell>> minesPossibleSets(ArrayList<Cell> coveredNeighbours, int minesCount) {
        ArrayList<ArrayList<Cell>> possibleMinesSets = permutations(coveredNeighbours);

        possibleMinesSets.removeIf(set -> set.size() != minesCount); // remove the sets that do not have the mines count size.

        return possibleMinesSets;
    }

    /**
     * TODO: improve comments
     * Get all the possible permutations for given covered neighbours.
     * @param coveredNeighbours the covered neighbours given.
     * @return all the possible permutations
     */
    private static ArrayList<ArrayList<Cell>> permutations(ArrayList<Cell> coveredNeighbours) {
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
     * TODO: improve comments
     * Fill inner sets with the appropriate elements.
     * @param sets the sets to be filled.
     * @param coveredNeighbours the covered neighbours given.
     */
    private static void fillInnerSets(ArrayList<ArrayList<Cell>> sets, ArrayList<Cell> coveredNeighbours) {
        Cell top = coveredNeighbours.get(0);
        ArrayList<Cell> remaining = new ArrayList<Cell>(coveredNeighbours.subList(1, coveredNeighbours.size()));

        // TODO: explain RECURSIVE CALL.
        for (ArrayList<Cell> set : permutations(remaining)) {
            ArrayList<Cell> innerSet = new ArrayList<Cell>(); // create an inner set.

            innerSet.add(top); // add the top.
            innerSet.addAll(set); // add all elements of the permutations.

            sets.add(innerSet); // add inner set in bigger set.
            sets.add(set); // add set to sets.
        }
    }

    /**
     * TODO: improve comments
     * Uncover or mark cells, depending on the result returned by LogicNG.
     * @param result the result returned by LogicNG.
     * @param cell the cell to be uncovered or maked.
     */
    private boolean uncoverCell(Tristate result, Cell cell) {

        // if result equals FALSE, then the cell is safe, uncover.
        if(result.equals(Tristate.FALSE)) {
            uncover(cell.getR(), cell.getC()); // uncover cell.
            worldChangedOuput();
            logicInference = true;
            return true;
        }
        return false;
    }

    private boolean markCell(Tristate result, Cell cell) {
//        // if result equals FALSE, then mark as danger!
        if (result.equals(Tristate.FALSE)) {
            markCell(cell.getR(), cell.getC()); // mark cell.
            worldChangedOuput();
            logicInference = true;
            return true;
        }
        return false;
    }

}