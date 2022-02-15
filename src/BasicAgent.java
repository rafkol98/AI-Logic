import java.util.ArrayList;

public abstract class BasicAgent {

    private Game game;

    private Cell[][] knownWorld;
    private ArrayList<Cell> blocked; // the agent must know the blocked cells.
    private Cell topLeft;
    private Cell centreCell;
    private int numberOfMines;

    private ArrayList<Cell> probed;
    private ArrayList<Cell> mines;
    private ArrayList<Cell> uncovered;

    private int rowSize, columnSize;

    public BasicAgent(char[][] board, int strategy) {
        getKnowledgeBase();
        game = new Game(board);
        probed = new ArrayList();
        mines = new ArrayList<>();
        uncovered = new ArrayList<>();
    }

    public void getKnowledgeBase() {
        blocked = game.getBlockedCells();
        topLeft = game.getTopLeftCell();
        centreCell = game.getCentreOfBoardCell();
        numberOfMines = game.getTotalNumberMines();
        rowSize = game.getBoardRowSize();
        columnSize = game.getBoardColumnSize();
    }

    public void probe(int strategy) {
        if (strategy == 1) {
            inOrder();
        }
    }


    /**
     * Probe in order.
     */
    public void inOrder() {

        // Start from top left.
        for(int i=0; i < rowSize; i++) {
            for (int j=0; j<columnSize; j++) {
                if () {

                }
            }
        }


    }

    public void flagCell(Cell cell) {

    }

}
