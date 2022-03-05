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
    public final String AND = "&";
    public final String OR = "|";
    public final String NOT = "~";

    boolean logicInference = false;

    public IntermediateAgent(char[][] board, boolean verbose) {
        super(board, verbose);
    }


    @Override
    public void probe() {
        uncover(0, 0);
        uncover(getCentreCell().getR(), getCentreCell().getC());
        printAgentKnownWorld(false); // print the known world by the agent.

        // While there are cells that are covered, continue to look for inferences.
        while (!getCovered().isEmpty()) {
            for (int r = 0; r < getKnownWorld().length; r++) {
                for (int c = 0; c < getKnownWorld()[0].length; c++) {
                    Cell cell = getKnownWorld()[r][c];

                    // if
                    if (getCovered().contains(cell)) {
                        sps(r, c);
                    }

                    if (getCovered().contains(cell)) {
                        alternative(cell);
                    }

                    // if the cell is still covered (no changes), then increment counter.
                    if (getCovered().contains(cell)) {
                        counter++;
                    }
                }
            }

            if (counter >= getRowSize() * getColumnSize()) {
                printFinal(0);
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

        //TODO: remove debugs.
        System.out.println("SUITABLE CELLS");
        for (Cell c : cells) {
            System.out.println(c.getR()+ "  "+ c.getC());
        }

        String kbu = createKBU(cells);  // create KBU.

        if (proveMineOrFree(cell, kbu, true)) {
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
     *
     * Used for Logical inference and building the KBU in both intermediate agents.
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

            //TODO: might have to do a separate function for the connectors - different for CNF.
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
     */
    private boolean proveMineOrFree(Cell cell, String kbu, boolean proveMine) {
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

        return false;
    }

    /**
     * Get all the possible sets of size equal to the remaining mines count.
     *
     * @param coveredNeighbours the neighbours that are covered.
     * @param setSize        if DNF the set size is equal to the count of mines. If CNF then the set size is equal to 2.
     * @return an ArrayList containing an inner ArrayList with all the possible sets.
     */
    public ArrayList<ArrayList<Cell>> minesPossibleSets(ArrayList<Cell> coveredNeighbours, int setSize) {
        ArrayList<ArrayList<Cell>> possibleMinesSets = permutations(coveredNeighbours);

        possibleMinesSets.removeIf(set -> set.size() != setSize); // remove the sets that do not have the mines count size.

        return possibleMinesSets;
    }

    /**
     * TODO: improve comments - MAKE PRIVATE (MAYBE)
     * Get all the possible permutations for given covered neighbours.
     *
     * @param coveredNeighbours the covered neighbours given.
     * @return all the possible permutations
     */
    public ArrayList<ArrayList<Cell>> permutations(ArrayList<Cell> coveredNeighbours) {
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
     *
     * @param sets              the sets to be filled.
     * @param coveredNeighbours the covered neighbours given.
     */
    private void fillInnerSets(ArrayList<ArrayList<Cell>> sets, ArrayList<Cell> coveredNeighbours) {
        //TODO: check here mines count statement.
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
     * Get all the options available from a cell in a logic sentence.
     *
     * @param cell the cell to get the logic options of.
     * @return the options of that cell in a logic sentence.
     */
    public String getLogic(Cell cell) {
        String logicOptions = "";
        System.out.println("LOGIC FOR: "+ cell.getR() + " "+cell.getC());
        ArrayList<Cell> coveredNeighbours = getOnlyCoveredNeighbours(cell.getR(), cell.getC()); // get covered neighbours.
        int numberOfMarkedMinesNeighbours = getNumberOfMinesMarkedNeighbours(cell.getR(), cell.getC()); // get number of marked mines in neighbours.

        // Unmarked mines = clue - numberOfMarkedMines.
        // get number of mines not marked yet.
        int remainingMines = Integer.parseInt(String.valueOf(cell.getValue())) - numberOfMarkedMinesNeighbours;
        ArrayList<ArrayList<Cell>> minesPosSets = minesPossibleSets(coveredNeighbours, remainingMines);

        //TODO: REMOVE DEBUG.
        System.out.println("mines pos sets "+minesPosSets);


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

                //TODO: piani ta akalifta tou protou set. dame je kamni sindiasmous.

                // Iterate through the covered neighbours, to add inner element combinations.
                for (int x = 0; x < coveredNeighbours.size(); x++) {
                    Cell neighbour = coveredNeighbours.get(x); // get neighbour.
                    System.out.println("-- covered neighbour -- "+ neighbour.toString());

                    // Connect the different options with an AND (if not the beginning).
                    if (x != 0) {
                        logicOptions += andInner;
                    }
                    System.out.println("DOES "+minesPosSets.get(i)+" CONTAIN "+ neighbour+" ? \n");
                    // Check if the current set does not contain neighbour. If not then append the NOT symbol.
                    if (!minesPosSets.get(i).contains(neighbour)) {
                        logicOptions += NOT;
                    }
                    // Add cell in the logic options string.
                    logicOptions += "M" + neighbour.getR() + neighbour.getC();
                }
                logicOptions += ")";

                System.out.println(logicOptions +" \n\n");
            }
            logicOptions += ")";
        }

        System.out.println(logicOptions);
        return logicOptions;
    }

    /**
     * TODO: improve comments
     * Uncover or mark cells, depending on the result returned by LogicNG.
     *
     * @param result the result returned by LogicNG.
     * @param cell   the cell to be uncovered or marked.
     */
    private boolean uncoverCell(Tristate result, Cell cell) {

        // if result equals FALSE, then the cell is safe, uncover.
        if (result.equals(Tristate.FALSE)) {
            uncover(cell.getR(), cell.getC()); // uncover cell.
            worldChangedOuput();
            logicInference = true;
            return true;
        }
        return false;
    }

    /**
     * Marks a cell as a mine.
     * @param result the result returned by LogicNG.
     * @param cell the cell to be uncovered or marked.
     * @return
     */
    private boolean markCell(Tristate result, Cell cell) {
        // if result equals FALSE, then mark as danger!
        if (result.equals(Tristate.FALSE)) {
            markCell(cell.getR(), cell.getC()); // mark cell.
            worldChangedOuput();
            logicInference = true;
            return true;
        }
        return false;
    }

}