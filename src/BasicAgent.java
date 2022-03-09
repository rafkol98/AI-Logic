/**
 * The BasicAgent class. Uses simple probing in order strategy.
 *
 * @author: 210017984
 */
public class BasicAgent extends Agent {

    /**
     * Create agent instance.
     *
     * @param board          the board passed in.
     * @param verbose        whether to print every step
     * @param agentNo        the solving agent number.
     * @param inferencesFlag measure inferences required to reach goal.
     */
    public BasicAgent(char[][] board, boolean verbose, int agentNo, boolean inferencesFlag) {
        super(board, verbose, agentNo, inferencesFlag);
    }

    //Not used for basic agent.
    @Override
    public void alternative(Cell cell) {
    }

    //Not used for basic agent.
    @Override
    public void markAtTheEnd() {
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
                if (getKnownWorld()[r][c].getValue() != 'b' && getKnownWorld()[r][c].getValue() == '?' && !containInList(r, c, getUncovered())) {
                    uncover(r, c);
                    worldChangedOuput();
                }
            }
        }
    }

}
