import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IntermediateAgentCNF extends IntermediateAgent {

    public IntermediateAgentCNF(char[][] board, boolean verbose) {
        super(board, verbose);
    }

    @Override
    public void alternative(Cell cell) {
        System.out.println("MESA");
        // CNF Technique.
        ArrayList<Cell> cells = getSuitableCells();
        String kbu = createKBU(cells);  // create KBU.
        System.out.println(kbu);

        HashMap<String, Integer> meow = mapPropToInteger(kbu, "M[0-9][0-9]");
        System.out.println(meow);

        System.out.println(replace(kbu, meow));
        toDimacs(replace(kbu, meow));
//        System.out.println(clauses.get("M21 | M22"));
    }

    public void toDimacs(String kbu) {
        HashMap<String, Integer> propsAsInts = mapPropToInteger(kbu, "M[0-9][0-9]");
        kbu = replace(kbu, propsAsInts); // replace the propositions in the kbu with integers.

        // create a new Arraylist of integer arrays to store each clause.
        ArrayList<Integer[]> clauses = new ArrayList<>();

        // The first matcher removes brackets.
        Matcher m = Pattern.compile("\\((.*?)\\) ").matcher(kbu);
        while (m.find()) {
            ArrayList<Integer> inner = new ArrayList<>();
            String removedBrackets = m.group(1);
            Pattern p = Pattern.compile("-?\\d+");

            // The second matcher deletes finds integers.
            Matcher m_2 = p.matcher(removedBrackets);
            while (m_2.find()) {
                // add in the inner arraylist.
                inner.add(Integer.parseInt(m_2.group()));
            }

            System.out.println(inner.stream().toArray( n -> new Integer[n]));
            Integer[] gg = inner.stream().toArray( n -> new Integer[n]);

            for(int i=0; i<gg.length; i++) {
                System.out.println(gg[i]);
            }
           clauses.add(inner.stream().toArray( n -> new Integer[n]));
        }

        System.out.println(clauses);

    }

    public String replace(String kbu, HashMap<String, Integer> map) {
        String updatedKBU = kbu;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            // replace proposition with integers.
            updatedKBU = updatedKBU.replaceAll(entry.getKey(), entry.getValue().toString());
            // replace not with minus sign.
            updatedKBU = updatedKBU.replaceAll("~", "-");
        }

        return updatedKBU;

    }
    // READ STRING
    // SPLIT IT AND FIND COMMON ELEMENTS
    // PUT COMMON ELEMENTS IN A SET (TO ELIMINATE DUPLICATES)
    // MAP A CELL TO A NUMBER.



   // INSPIRED BY: https://stackoverflow.com/questions/5705111/how-to-get-all-substring-for-a-given-regex

    /**
     * Maps a proposition (e.g., M10) to an integer value (e.g., 1).
     * @param text
     * @param regex
     * @return
     */
    public HashMap<String, Integer> mapPropToInteger(String text, String regex) {

        // Put all the elements in a set.
        HashSet<String> propositions= new HashSet<String>();
        Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(text);
        // while there are elements matching the regex passed in, p
        while(m.find()) {
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

        System.out.println("LOGIC FOR: "+cell);

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
     * @param possibleSets
     * @return
     */
    private String atMostDangers(ArrayList<ArrayList<Cell>> possibleSets) {
        String logicOptions = "";
        // iterate through the possible sets.
        for (int i=0; i<possibleSets.size(); i++) {
            ArrayList<Cell> set = possibleSets.get(i);

            // Iterate through the set and create disjunctions.
            logicOptions += "(";
            String clause = "";
            for (int x=0; x< set.size(); x++) {
                clause += NOT + "M" + set.get(x).getR() + set.get(x).getC();

                if (x != (set.size()-1)) {
                    clause +=  " " + OR + " ";
                }
            }
            logicOptions += clause;
            logicOptions += ") ";

            // Add conjunction of disjunctions.
            if (i != (possibleSets.size()-1)) {
                logicOptions += AND + " ";
            }
        }

        return logicOptions;
    }


    private String atMostNonDangers(ArrayList<ArrayList<Cell>> possibleSets) {
        ArrayList<Cell> combined = putInOne(possibleSets);

        String logicOptions = "(";
        String clause = "";
        for (int i=0; i< combined.size(); i++) {
            clause +=  "M" + combined.get(i).getR() + combined.get(i).getC();

            // if its not the last element then connect them with an AND.
            if (i != (combined.size()-1)) {
                clause += " " + OR + " ";
            }
        }
        logicOptions += clause;
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
