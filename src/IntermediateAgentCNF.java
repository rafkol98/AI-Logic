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

    /**
     * Create agent instance.
     *
     * @param board          the board passed in.
     * @param verbose        whether to print every step
     * @param agentNo        the solving agent number.
     * @param inferencesFlag measure inferences required to reach goal.
     */
    public IntermediateAgentCNF(char[][] board, boolean verbose, int agentNo, boolean inferencesFlag) {
        super(board, verbose, agentNo, inferencesFlag);
    }

    /**
     * Determine whether the passed in cell should be uncovered or marked as mine - using Logical Inference.
     *
     * @param cell      the cell being examined.
     * @param kbu       the knowledge base of unknowns.
     * @param proveMine flag that determines whether to try to prove cell is a mine. If false then try to prove cell
     *                  is free.
     * @return true if there was a change.
     */
    @Override
    public boolean proveMineOrFree(Cell cell, String kbu, boolean proveMine) {
        String entailment;

        // if the proveMine flag is true, then we want to entail whether the cell is a mine.
        if (proveMine) {
            entailment = AND + " (" + NOT + "M" + cell.getR() + cell.getC() + ")";
        } else {
            entailment = AND + " (M" + cell.getR() + cell.getC() + ")";
        }


        String tempKBU = kbu + entailment; // add entailment to the KBU.

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

            // if prove mine flag is true then try to mark cell, otherwise uncover cell.
            if (proveMine) {
                return markCell(problem.isSatisfiable(), cell); // mark cell - depending on the inference.
            } else {
                return uncoverCell(problem.isSatisfiable(), cell); // uncover cell - depending on the inference.
            }

        } catch (TimeoutException | ContradictionException e) {
            e.getMessage();
        }

        return false;
    }

    /**
     * Convert KBU into DIMACS format clauses (in an arraylist of ints).
     *
     * @param kbu the knowledge base of unknowns.
     * @return ArrayList containing integer arrays in dimacs format.
     */
    public ArrayList<int[]> dimacs(String kbu) {
        // create a new Arraylist of integer arrays to store each clause.
        ArrayList<int[]> clauses = new ArrayList<>();

        // Replace the propositions in the kbu with integers.
        kbu = replace(kbu);

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
            clauses.add(inner.stream().mapToInt(i -> i).toArray());  // Add the current clause in the clauses ArrayList.
        }
        return clauses;
    }

    /**
     * Create a new KBU where each proposition is replaced by its corresponding dimacs integer encoding.
     *
     * @param kbu the knowledge base of unknowns.
     * @return updated KBU.
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
     * @param kbu the KBU that its propositions will be mapped.
     * @param regex the regular expression to map to integers.
     * @return HashMap with proposition string as key and value as its mapped integer.
     */
    public HashMap<String, Integer> mapPropToInteger(String kbu, String regex) {

        // Put all the elements in a set.
        HashSet<String> propositions = new HashSet<String>();
        Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(kbu);
        // while there are elements matching the regex passed in, p
        while (m.find()) {
            propositions.add(m.group(1));
        }

        // Create a new map to assign each proposition ot an integer.
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        // Assign each proposition a number.
        int propositionNumber = 1;
        for (String proposition : propositions) {
            map.put(proposition, propositionNumber);
            propositionNumber++;
        }
        return map;
    }

    /**
     * Get all the options available from a cell in a logic sentence - CNF format.
     *
     * @param cell the cell to get the logic options of.
     * @return logical sentence as a string.
     */
    @Override
    public String getLogic(Cell cell) {
        String logicOptions = "";

        ArrayList<Cell> coveredNeighbours = getOnlyCoveredNeighbours(cell.getR(), cell.getC()); // get covered neighbours.

        int numberOfMarkedMinesNeighbours = getNumberOfMinesMarkedNeighbours(cell.getR(), cell.getC()); // get number of marked mines in neighbours.
        int clue = cell.getValueInt() - numberOfMarkedMinesNeighbours; // remove the number of marked mines.

        int sizeSubsetAtMost = clue + 1; // size of sets to consider for atMost. At most is "number of clue" + 1.
        ArrayList<ArrayList<Cell>> atMostSet = minesPossibleSets(coveredNeighbours, sizeSubsetAtMost); // get possible sets for atMost.

        int sizeSubsetAtLeast = (coveredNeighbours.size() - clue) + 1; //  size of sets to consider for atLeast - number of covered neighbours - clue.
        ArrayList<ArrayList<Cell>> atLeastSet = minesPossibleSets(coveredNeighbours, sizeSubsetAtLeast); // get possible sets for atLeast.

        String atMost = atMostOrLeast(atMostSet, true);
        String atLeast = atMostOrLeast(atLeastSet, false);


        // if the clauses are not empty use both at least and at most, act accordingly otherwise
        if (atMost.length() > 0 && atLeast.length() > 0) {
            logicOptions += atMost + AND + " " + atLeast;
        } else if (atLeast.length() > 0) {
            logicOptions += atLeast;
        } else if (atMost.length() > 0) {
            logicOptions += atMost;
        }

        return logicOptions;
    }


    /**
     * Used as either atMmost or atLeast, depending on the most flag.
     *
     * @param possibleSets the possible sets to combine in CNF.
     * @param most         if true, then use atMost otherwise use atLeast.
     * @return
     */
    private String atMostOrLeast(ArrayList<ArrayList<Cell>> possibleSets, boolean most) {
        String logicOptions = "";
        // iterate through the possible sets.
        for (int i = 0; i < possibleSets.size(); i++) {
            ArrayList<Cell> set = possibleSets.get(i);

            if (set.size() > 0) {
                // Iterate through the set and create disjunctions.
                logicOptions += "(";
                String clause = "";
                for (int x = 0; x < set.size(); x++) {
                    // if atMost then append NOT.
                    if (most) {
                        clause += NOT + "M" + set.get(x).getR() + set.get(x).getC();
                    } else {
                        clause += "M" + set.get(x).getR() + set.get(x).getC();
                    }

                    // connect literals.
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
        }
        return logicOptions;
    }

}
