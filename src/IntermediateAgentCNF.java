import java.util.ArrayList;

public class IntermediateAgentCNF extends IntermediateAgent {

    public IntermediateAgentCNF(char[][] board, boolean verbose) {
        super(board, verbose);
    }

    @Override
    public void alternative(Cell cell) {
        // DNF Encoding Technique.
        ArrayList<Cell> cells = getSuitableCells();
        String kbu = createKBU(cells);  // create KBU.

    }









}
