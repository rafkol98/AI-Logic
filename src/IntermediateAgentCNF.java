import jdk.swing.interop.SwingInterOpUtils;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ISolver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class IntermediateAgentCNF extends IntermediateAgent {

    public IntermediateAgentCNF(char[][] board, boolean verbose) {
        super(board, verbose);
    }

    @Override
    public void alternative(Cell cell) {
        System.out.println("MESA");
        // CNF Technique.
        ArrayList<Cell> cells = getSuitableCells();
        System.out.println("Suitable cells "+cells+"\n");
        String kbu = createKBU(cells);  // create KBU.
    }

    @Override
    public String getLogic(Cell cell) {
        String logicOptions = "(";

        System.out.println(cell);

        ArrayList<Cell> coveredNeighbours = getOnlyCoveredNeighbours(cell.getR(), cell.getC()); // get covered neighbours.
        int numberOfMarkedMinesNeighbours = getNumberOfMinesMarkedNeighbours(cell.getR(), cell.getC()); // get number of marked mines in neighbours.


//        // Unmarked mines = clue - numberOfMarkedMines.
//        // get number of mines not marked yet.
//        int remainingMines = Integer.parseInt(String.valueOf(cell.getValue())) - numberOfMarkedMinesNeighbours;
//        ArrayList<ArrayList<Cell>> minesPosSets = minesPossibleSets(coveredNeighbours, 2);

        int k = cell.getValueInt();
        int sizeAtMost = k + 1;
        System.out.println(sizeAtMost);
        ArrayList<ArrayList<Cell>> minesPosSets = minesPossibleSets(coveredNeighbours, sizeAtMost);
        System.out.println("AT MOST: "+minesPosSets);

        int sizeAtMostNot = coveredNeighbours.size() - k;
        System.out.println("at most not: "+sizeAtMostNot);
        ArrayList<ArrayList<Cell>> minesPosSetsNOT = minesPossibleSets(coveredNeighbours, sizeAtMostNot);
        System.out.println("AT MOST NON: "+minesPosSetsNOT);


//        logicOptions += atLeast(minesPosSets) + AND + " " + atMost(minesPosSets);
//
//        logicOptions += ")";

        return logicOptions;
    }

    private String atMost(ArrayList<ArrayList<Cell>> possibleSets) {
        String logicOptions = "";
        // iterate through the possible sets.
        for (int i=0; i<possibleSets.size(); i++) {
            ArrayList<Cell> set = possibleSets.get(i);

            // Iterate through the set and create disjunctions.
            logicOptions += "(";
            for (int x=0; x< set.size(); x++) {
                logicOptions += NOT + "M" + set.get(x).getR() + set.get(x).getC();

                if (x != (set.size()-1)) {
                    logicOptions +=  " " + OR + " ";
                }
            }
            logicOptions += ") ";

            // Add conjunction of disjunctions.
            if (i != (possibleSets.size()-1)) {
                logicOptions += AND + " ";
            }
        }

        return logicOptions;
    }


    private String atLeast(ArrayList<ArrayList<Cell>> possibleSets) {
        ArrayList<Cell> combined = putInOne(possibleSets);

        String logicOptions = "(";
        for (int i=0; i< combined.size(); i++) {
            logicOptions +=  "M" + combined.get(i).getR() + combined.get(i).getC();

            // if its not the last element then connect them with an AND.
            if (i != (combined.size()-1)) {
                logicOptions += " " + OR + " ";
            }
        }
        logicOptions += ")";

        return logicOptions;
    }


    private ArrayList<Cell> putInOne (ArrayList<ArrayList<Cell>> sets) {
        Set<Cell> setx = new HashSet<Cell>();

        for (ArrayList<Cell> set : sets) {
            for (Cell cell : set) {
                setx.add(cell);
            }
        }

        return new ArrayList<Cell>(setx);
    }


}
