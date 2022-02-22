import org.logicng.formulas.FormulaFactory;

import java.util.ArrayList;

public class IntermediateAgent extends BasicAgent {
//    public class IntermediateAgent extends BeginnerAgent {

    protected final FormulaFactory f = new FormulaFactory();
    private final String NOT = "~";
    private final String AND = "&";
    private final String OR = "|";

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


    public void allLogicOptions(ArrayList<Cell> cells) {
        ArrayList<String> logicOptions = new ArrayList<>();

        for (Cell cell : cells) {

        }

    }

    //MAYBE ARRAYLIST OF ARRAYLISTS.

    public String getLogicOptions(Cell cell) {
        String logicOptions = "";

        ArrayList<Cell> coveredNeighbours = getOnlyCoveredNeighbours(cell.getR(),cell.getC());
        int numberOfMarkedMinesNeighbours = getNumberOfMinesMarkedNeighbours(cell.getR(), cell.getC());

        // Unmarked mines = clue - numberOfMarkedMines.
        int unmarkedMines = cell.getValue() - numberOfMarkedMinesNeighbours;

        for (int i=0; i<coveredNeighbours.size(); i++) {
            //TODO: have to make a power set containing all options.
//            logicOptions +=
        }

        return logicOptions;
    }


//    KNOWLEDGE BASE ONLY FOR THE DANGER WORLDS.
//    NEW KNOWLEDGE BASE EVERY TIME WE ARE TRYING TO UNCOVER SOMETHING

    /**
     * Add in the knowledge base only cells that are not Blocked and do not contain mines.
     */
    public void createKBU() {
        String KBU = "";



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
                if(coveredNeighbourExists(r,c) && getUncovered().contains(getKnownWorld()[r][c])) {
                    cells.add(getKnownWorld()[r][c]); // cell is suitable.
                }
            }
        }

        return cells;
    }

    private boolean coveredNeighbourExists(int r, int c) {
        ArrayList<Cell> neighbours = getAdjacentNeighbours(r,c);
        for (Cell neighbour : neighbours) {
            // check if
            if (isCellCovered(neighbour)) {
                return true;
            }
        }

        return false;
    }


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


    //plays the game using the satisfiability test reasoning strategy (SATS) from lectures,
    // in ADDITION to the SPS

    //prove that a cell is clear (NO MINE) in given coordinates (x,y) by proving KB ^ Dx,y is FALSE.

    // AGENT ABLE TO TRANSFORM ITS CURRENTKNOWNWORLD INTO A LOGIC SENTENCE. USE DNF ENCODING TECHNIQUE.

    // USE SATISFIABILITY RESULTS TO SELECT NEXT MOVE

    // THE LIBRARY LOGICNG SHOULD BE USED TO PROVE THAT A GIVEN CELL DOES OR DOES NOT CONTAIN A MINE.

    // STOP WHEN NO OTHER DEDUCTION COULD BE MADE.


}
