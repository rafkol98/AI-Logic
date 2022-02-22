public class BasicAgent extends Agent{

    public BasicAgent(char[][] board, int strategy, boolean verbose) {
        super(board, strategy, verbose);
    }

    /**
     * Probe in order.
     */
    @Override
    public void probe() {
        printAgentKnownWorld(false);
        // Iterate agent world.
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                // Check only NON-BLOCKED cells and those NOT-UNCOVERED yet.
                if (getKnownWorld()[r][c].getValue() != 'b' && getKnownWorld()[r][c].getValue() == '?' && !containInList(r,c, getUncovered())) {
                    uncover(r, c);

                    if (getGame().isGameWon(getUncovered().size(),0,1)) {
                        printFinal(1);
                    } else {
                        printAgentKnownWorld(false);
                    }

                }
            }
        }
    }

}
