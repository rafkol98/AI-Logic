import java.util.ArrayList;

/**
 * The NoFlag class is an EXTENSION. Same technique as IntermediateAgent but does not
 * mark cells at all, only at the very end after proven that it won.
 *
 * @author: 210017984
 */
public class NoFlag extends IntermediateAgent{

    /**
     * Create agent instance.
     *
     * @param board   the board passed in.
     * @param verbose whether to print every step
     * @param agentNo the solving agent number.
     * @param inferencesFlag measure inferences required to reach goal.
     */
    public NoFlag(char[][] board, boolean verbose, int agentNo, boolean inferencesFlag) {
        super(board, verbose, agentNo, inferencesFlag);
    }

    /**
     * This method is called when the sps technique (implemented in the BeginnerAgent) can
     * make no other deductions. It uses the DNF encoding technique.
     */
    @Override
    public void alternative(Cell cell) {
        ArrayList<Cell> cells = getSuitableCells(); // get suitable cells.

        String kbu = createKBU(cells);  // create KBU.

        proveMineOrFree(cell, kbu, false);  // Only prove cell is safe.

    }

    /**
     * Mark cells that were left at the end as mines. After proven that won.
     */
    @Override
    public void markAtTheEnd() {
        // Iterate through the covered cells and mark them as mines.
        for (Cell cell : getCovered()) {
            markCell(cell.getR(), cell.getC()); // mark cell.
        }
    }

}
