
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IntermediateAgentCNF extends IntermediateAgent {

    final int MAXVAR = getRowSize() * getColumnSize(); // theoretically the maximum number of variables is equal to the area of the board.

    public IntermediateAgentCNF(char[][] board, boolean verbose) {
        super(board, verbose);
    }

    /**
     * Determine whether the passed in cell should be uncovered or marked as mine - using Logical Inference.
     * @param cell the cell being examined.
     * @param kbu the knowledge base of unknowns.
     * @param proveMine flag that determines whether to try to prove cell is a mine. If false then try to prove cell
     *                  is free.
     */
    @Override
    public boolean proveMineOrFree(Cell cell, String kbu, boolean proveMine) {
        String entailment;

        // if the proveMine flag is true, then we want to entail whether the cell is a mine.
        if (proveMine) {
            entailment = " " + AND + " (" + NOT + "M" + cell.getR() + cell.getC()+")";
        } else {
            entailment = " " + AND + " (M" + cell.getR() + cell.getC()+")";
        }

        String tempKBU = kbu + entailment;

        ArrayList<int[]> clausesDimacs = dimacs(tempKBU); // get clauses in arrays of ints - dimacs format.

        // Create solver.
        ISolver solver = SolverFactory.newDefault();
        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(clausesDimacs.size());

        try {
            // Add clauses to the solver.
            for (int[] clause : clausesDimacs) {
                solver.addClause(new VecInt(clause));
            }

            IProblem problem = solver;

            if (proveMine) {
                return markCell(problem.isSatisfiable(), cell); // mark cell - depending on the inference.
            } else {
                return uncoverCell(problem.isSatisfiable(), cell); // uncover cell - depending on the inference.
            }

        } catch (TimeoutException | ContradictionException e) {
            System.out.println("There was a problem: " + e.getMessage());
        }

        return false;
    }

    /**
     * Convert KBU into dimacs format clauses (in an arraylist of ints).
     * @param kbu
     * @return
     */
    public ArrayList<int[]> dimacs(String kbu) {
        // create a new Arraylist of integer arrays to store each clause.
        ArrayList<int[]> clauses = new ArrayList<>();


        // Replace the propositions in the kbu with integers.
        kbu = replace(kbu);
        System.out.println(kbu);

        // The first matcher removes brackets.
        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(kbu);
        while (m.find()) {
            ArrayList<Integer> inner = new ArrayList<>();
            String removedBrackets = m.group(1);

            // The second matcher finds integers in the newly created string (without brackets).
            Pattern p = Pattern.compile("-?\\d+");
            Matcher m_2 = p.matcher(removedBrackets);
            // Add all elements of the current clause.
            while (m_2.find()) {
                // add in the inner arraylist.
                inner.add(Integer.parseInt(m_2.group()));
            }
            clauses.add(inner.stream().mapToInt(i -> i).toArray());  // Add the clause in the clauses ArrayList.
        }

        return clauses;
    }

    /**
     * Create a new KBU where each proposition is replaced by its corresponding dimacs integer encoding.
     * @param kbu
     * @return
     */
    public String replace(String kbu) {
        // Get a map containing the integer representation of each proposition.
        HashMap<String, Integer> map = mapPropToInteger(kbu, "M[0-9][0-9]");

        String updatedKBU = kbu;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            // GetKey gives us the cell (e.g. M02) and the getValue its corresponding dimacs encoding number.
            updatedKBU = updatedKBU.replaceAll(entry.getKey(), entry.getValue().toString());
            // replace not with minus sign.
            updatedKBU = updatedKBU.replaceAll("~", "-");
        }

        return updatedKBU;

    }


    /**
     * Maps a proposition (e.g., M10) to an integer value (e.g., 1).
     *
     * @param text
     * @param regex
     * @return
     */
    public HashMap<String, Integer> mapPropToInteger(String text, String regex) {

        // Put all the elements in a set.
        HashSet<String> propositions = new HashSet<String>();
        Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(text);
        // while there are elements matching the regex passed in, p
        while (m.find()) {
            propositions.add(m.group(1));
        }

        // Create a new map to assign each proposition ot an integer.
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        int count = 1;
        for (String proposition : propositions) {
            map.put(proposition, count);
            count++;
        }

        return map;
    }

    @Override
    public String getLogic(Cell cell) {
        String logicOptions = "";

        ArrayList<Cell> coveredNeighbours = getOnlyCoveredNeighbours(cell.getR(), cell.getC()); // get covered neighbours.

        int k = cell.getValueInt();
        int sizeAtMost = k + 1; // size of sets to consider for atMostDangers. At most is "number of clue" + 1.
        ArrayList<ArrayList<Cell>> atMostSet = minesPossibleSets(coveredNeighbours, sizeAtMost);

        int sizeAtMostNon = coveredNeighbours.size() - k; //  size of sets to consider for atMostNonDangers - number of covered neighbours - clue.
        ArrayList<ArrayList<Cell>> atMostNotSet = minesPossibleSets(coveredNeighbours, sizeAtMostNon);

        logicOptions += atMostDangers(atMostSet) + AND + " " + atMostNonDangers(atMostNotSet);

        return logicOptions;
    }


    /**
     * At most given sets n
     *
     * @param possibleSets
     * @return
     */
    private String atMostDangers(ArrayList<ArrayList<Cell>> possibleSets) {
        String logicOptions = "";
        // iterate through the possible sets.
        for (int i = 0; i < possibleSets.size(); i++) {
            ArrayList<Cell> set = possibleSets.get(i);

            // Iterate through the set and create disjunctions.
            logicOptions += "(";
            String clause = "";
            for (int x = 0; x < set.size(); x++) {
                clause += NOT + "M" + set.get(x).getR() + set.get(x).getC();

                if (x != (set.size() - 1)) {
                    clause += " " + OR + " ";
                }
            }
            logicOptions += clause;
            logicOptions += ") ";

            // Add conjunction of disjunctions.
            if (i != (possibleSets.size() - 1)) {
                logicOptions += AND + " ";
            }
        }

        return logicOptions;
    }


    private String atMostNonDangers(ArrayList<ArrayList<Cell>> possibleSets) {
        ArrayList<Cell> combined = putInOne(possibleSets);

        String logicOptions = "(";
        String clause = "";
        for (int i = 0; i < combined.size(); i++) {
            clause += "M" + combined.get(i).getR() + combined.get(i).getC();

            // if its not the last element then connect them with an AND.
            if (i != (combined.size() - 1)) {
                clause += " " + OR + " ";
            }
        }
        logicOptions += clause;
        logicOptions += ")";


        return logicOptions;
    }


    private ArrayList<Cell> putInOne(ArrayList<ArrayList<Cell>> sets) {
        Set<Cell> setx = new HashSet<Cell>();

        for (ArrayList<Cell> set : sets) {
            for (Cell cell : set) {
                setx.add(cell);
            }
        }

        return new ArrayList<Cell>(setx);
    }

    /**
     * TODO: improve comments
     * Uncover or mark cells, depending on the result returned
     *
     * @param result the result returned by LogicNG.
     * @param cell   the cell to be uncovered or marked.
     */
    private boolean uncoverCell(boolean result, Cell cell) {

        // if result equals FALSE, then the cell is safe, uncover.
        if (!result) {
            uncover(cell.getR(), cell.getC()); // uncover cell.
            worldChangedOuput();
            logicInference = true;
            return true;
        }
        return false;
    }

    /**
     * Marks a cell as a mine.
     *
     * @param result the result returned
     * @param cell   the cell to be uncovered or marked.
     * @return
     */
    private boolean markCell(boolean result, Cell cell) {
        // if result equals FALSE, then mark as danger!
        if (!result) {
            markCell(cell.getR(), cell.getC()); // mark cell.
            worldChangedOuput();
            logicInference = true;
            return true;
        }
        return false;
    }


}
