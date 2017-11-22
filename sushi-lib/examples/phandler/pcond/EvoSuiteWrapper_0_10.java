package phandler.pcond;

import static sushi.compile.path_condition_distance.DistanceBySimilarityWithPathCondition.distance;

import static java.lang.Double.*;
import static java.lang.Math.*;

import sushi.compile.path_condition_distance.*;
import sushi.logging.Level;
import sushi.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvoSuiteWrapper_0_10 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;


    public double test0(phandler.ExecutionHandler2 __ROOT_this, common.LinkedList __ROOT_primaryP, common.LinkedList __ROOT_secondaryP) throws Exception {
        //generated for state .1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1.2.1.1.1.1.1.1.1.2.1.2[164]
        final ArrayList<ClauseSimilarityHandler> pathConditionHandler = new ArrayList<>();
        ValueCalculator valueCalculator;
        // {R0} == Object[0] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this", Class.forName("phandler.ExecutionHandler2")));
        // pre_init(phandler/ExecutionHandler2)
        ;
        // !pre_init(java/lang/Object)
        ;
        // {R1} == Object[1] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP", Class.forName("common.LinkedList")));
        // {V2} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (0) ? 0 : isNaN((V2) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // pre_init(jbse/meta/Analysis)
        ;
        // pre_init(common/LinkedList$ListItr)
        ;
        // pre_init(common/LinkedList)
        ;
        // pre_init(java/util/AbstractSequentialList)
        ;
        // pre_init(java/util/AbstractList)
        ;
        // pre_init(java/util/AbstractCollection)
        ;
        // {R3} == Object[3] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header", Class.forName("common.LinkedList$Entry")));
        // {R6} == Object[4] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next", Class.forName("common.LinkedList$Entry")));
        // {R12} == Object[1] (aliases {ROOT}:primaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next._owner", "{ROOT}:primaryP"));
        // {V2} >= 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (1) ? 0 : isNaN((V2) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R10} == Object[5] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next", Class.forName("common.LinkedList$Entry")));
        // {R16} == Object[1] (aliases {ROOT}:primaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next.next._owner", "{ROOT}:primaryP"));
        // {V2} >= 2
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (2) ? 0 : isNaN((V2) - (2)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (2));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R9} == Object[6] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.element", Class.forName("phandler.Process")));
        // {R14} == Object[7] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next", Class.forName("common.LinkedList$Entry")));
        // {R20} == Object[1] (aliases {ROOT}:primaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next.next.next._owner", "{ROOT}:primaryP"));
        // {V2} >= 3
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (3) ? 0 : isNaN((V2) - (3)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (3));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R13} == Object[8] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.element", Class.forName("phandler.Process")));
        // {R18} == Object[9] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next", Class.forName("common.LinkedList$Entry")));
        // {R24} == Object[1] (aliases {ROOT}:primaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next.next.next.next._owner", "{ROOT}:primaryP"));
        // {V2} >= 4
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (4) ? 0 : isNaN((V2) - (4)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (4));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R17} == Object[10] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.element", Class.forName("phandler.Process")));
        // {R22} == Object[11] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.next", Class.forName("common.LinkedList$Entry")));
        // {R28} == Object[1] (aliases {ROOT}:primaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next.next.next.next.next._owner", "{ROOT}:primaryP"));
        // {V2} >= 5
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (5) ? 0 : isNaN((V2) - (5)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (5));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R21} == Object[12] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.element", Class.forName("phandler.Process")));
        // {R26} == Object[13] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.next.next", Class.forName("common.LinkedList$Entry")));
        // {R32} == Object[1] (aliases {ROOT}:primaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next.next.next.next.next.next._owner", "{ROOT}:primaryP"));
        // {V2} >= 6
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (6) ? 0 : isNaN((V2) - (6)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (6));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R25} == Object[14] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.next.element", Class.forName("phandler.Process")));
        // {R30} == Object[15] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.next.next.next", Class.forName("common.LinkedList$Entry")));
        // {R36} == Object[1] (aliases {ROOT}:primaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next.next.next.next.next.next.next._owner", "{ROOT}:primaryP"));
        // {V2} >= 7
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (7) ? 0 : isNaN((V2) - (7)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (7));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R29} == Object[16] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.next.next.element", Class.forName("phandler.Process")));
        // {R34} == Object[17] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.next.next.next.next", Class.forName("common.LinkedList$Entry")));
        // {R40} == Object[1] (aliases {ROOT}:primaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next.next.next.next.next.next.next.next._owner", "{ROOT}:primaryP"));
        // {V2} >= 8
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (8) ? 0 : isNaN((V2) - (8)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (8));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R33} == Object[18] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.next.next.next.element", Class.forName("phandler.Process")));
        // {R38} == Object[19] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.next.next.next.next.next", Class.forName("common.LinkedList$Entry")));
        // {R44} == Object[1] (aliases {ROOT}:primaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next.next.next.next.next.next.next.next.next._owner", "{ROOT}:primaryP"));
        // {V2} >= 9
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (9) ? 0 : isNaN((V2) - (9)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (9));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R37} == Object[20] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.next.next.next.next.element", Class.forName("phandler.Process")));
        // {R42} == Object[3] (aliases {ROOT}:primaryP.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next.next.next.next.next.next.next.next.next.next", "{ROOT}:primaryP.header"));
        // {V2} == 9
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) == (9) ? 0 : isNaN((V2) - (9)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (9));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R41} == Object[21] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:primaryP.header.next.next.next.next.next.next.next.next.next.element", Class.forName("phandler.Process")));
        // pre_init(common/LinkedList$Entry)
        ;
        // {R2} == Object[34] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:secondaryP", Class.forName("common.LinkedList")));
        // {V26} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:secondaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V26 = (int) variables.get(0);
                return (V26) >= (0) ? 0 : isNaN((V26) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V26) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R45} == Object[36] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:secondaryP.header", Class.forName("common.LinkedList$Entry")));
        // {R47} == Object[37] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:secondaryP.header.next", Class.forName("common.LinkedList$Entry")));
        // {R53} == Object[34] (aliases {ROOT}:secondaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:secondaryP.header.next._owner", "{ROOT}:secondaryP"));
        // {V26} >= 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:secondaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V26 = (int) variables.get(0);
                return (V26) >= (1) ? 0 : isNaN((V26) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V26) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R50} == Object[38] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:secondaryP.header.next.element", Class.forName("phandler.Process")));
        // {R51} == Object[36] (aliases {ROOT}:secondaryP.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:secondaryP.header.next.next", "{ROOT}:secondaryP.header"));
        // {V26} == 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:secondaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V26 = (int) variables.get(0);
                return (V26) == (1) ? 0 : isNaN((V26) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V26) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // !pre_init(java/util/HashSet)
        ;
        // pre_init(java/util/AbstractSet)
        ;
        // pre_init(java/util/HashMap)
        ;
        // pre_init(java/util/AbstractMap)
        ;
        // {V31} < 10
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:secondaryP.header.next.element.priority");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V31 = (int) variables.get(0);
                return (V31) < (10) ? 0 : isNaN((V31) - (10)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V31) - (10));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // pre_init(java/util/HashMap$Entry)
        ;

        final HashMap<String, Object> candidateObjects = new HashMap<>();
        candidateObjects.put("{ROOT}:this", __ROOT_this);
        candidateObjects.put("{ROOT}:primaryP", __ROOT_primaryP);
        candidateObjects.put("{ROOT}:secondaryP", __ROOT_secondaryP);

        double d = distance(pathConditionHandler, candidateObjects);
        if (d == 0.0d)
            System.out.println("test0 0 distance");
        return d;
    }
}
