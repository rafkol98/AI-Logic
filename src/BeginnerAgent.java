import java.util.ArrayList;

public class BeginnerAgent extends Agent{

    public BeginnerAgent(char[][] board, int strategy, boolean verbose) {
        super(board, strategy, verbose);
    }

    @Override
    public void probe() {
        // SINGLE POINT STRATEGY.
        for (int r = 0; r < getKnownWorld().length; r++) {
            for (int c = 0; c < getKnownWorld()[0].length; c++) {
                if(!getProbed().contains(getKnownWorld()[r][c])) {
                    action(r,c);
                }
            }
        }
    }

    public void action(int r, int c) {
        ArrayList<Cell> adjacent = getAdjacentNeighbours(r,c);
    }

    public ArrayList<Cell> getAdjacentNeighbours(int r, int c) {
        ArrayList<Cell> validNeighbours = new ArrayList<>();

//        validNeighbours.add(getKnownWorld()[r-1][c-1]);
//
//        uncover(r - 1, c - 1); // Top Left
//        uncover(r - 1, c); // Top Center
//        uncover(r - 1, c + 1); // Top Right
//
//        uncover(r, c - 1); // Left
//        uncover(r, c + 1); // Right
//
//        uncover(r + 1, c - 1); // Bottom left
//        uncover(r + 1, c); // Bottom
//        uncover(r + 1, c + 1); // Bottom Right
        return validNeighbours;
    }




}
