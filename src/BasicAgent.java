public class BasicAgent extends Agent{

    public BasicAgent(char[][] board, boolean verbose) {
        super(board, verbose);
    }

    @Override
    public void alternative(Cell cell) {

    }

    /**
     * Probe in the cells in order.
     */
    @Override
    public void probe() {
        printAgentKnownWorld(false); // print initial.
        // Iterate agent's world.
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                // Check only NON-BLOCKED cells and those NOT-UNCOVERED yet.
                if (getKnownWorld()[r][c].getValue() != 'b' && getKnownWorld()[r][c].getValue() == '?' && !containInList(r,c, getUncovered())) {
                    uncover(r, c);

                    worldChangedOuput();
                }
            }
        }
    }

}
