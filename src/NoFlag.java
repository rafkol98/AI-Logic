import java.util.ArrayList;

public class NoFlag extends IntermediateAgent{

    public NoFlag(char[][] board, boolean verbose, int agentNo) {
        super(board, verbose, agentNo);
    }

    /**
     * This method is called when the sps technique (implemented in the BeginnerAgent) can
     * make no other deductions. It uses the DNF encoding technique.
     */
    @Override
    public void alternative(Cell cell) {

        ArrayList<Cell> cells = getSuitableCells();

        String kbu = createKBU(cells);  // create KBU.

        // Only prove mine.
        proveMineOrFree(cell, kbu, false);
    }

}
