package ganttproject.pcond;
import static sushi.compile.path_condition_distance.DistanceBySimilarityWithPathCondition.distance;

import static java.lang.Double.*;
import static java.lang.Math.*;

import sushi.compile.path_condition_distance.*;
import sushi.logging.Level;
import sushi.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvoSuiteWrapper_0_3 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;


    public double test0(ganttproject.DependencyGraph __ROOT_this, ganttproject.Node __ROOT_root) throws Exception {
        //generated for state .1.1.1.1.1.1.1.1.2.1.1.2.1[89]
        final ArrayList<ClauseSimilarityHandler> pathConditionHandler = new ArrayList<>();
        ValueCalculator valueCalculator;
        // {R0} == Object[0] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this", Class.forName("ganttproject.DependencyGraph")));
        // pre_init(ganttproject/DependencyGraph)
        ;
        // pre_init(java/util/AbstractSequentialList)
        ;
        // pre_init(java/util/AbstractList)
        ;
        // pre_init(java/util/AbstractCollection)
        ;
        // {R3} == Object[3] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root", Class.forName("ganttproject.Node")));
        // {R5} == Object[5] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData", Class.forName("ganttproject.NodeData")));
        // {V6} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myLevel");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                return (V6) >= (0) ? 0 : isNaN((V6) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V6) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // pre_init(jbse/meta/Analysis)
        ;
        // {V6} < 3
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myLevel");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                return (V6) < (3) ? 0 : isNaN((V6) - (3)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V6) - (3));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R7} == Object[6] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing", Class.forName("common.LinkedList")));
        // {V9} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V9 = (int) variables.get(0);
                return (V9) >= (0) ? 0 : isNaN((V9) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V9) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // pre_init(java/util/Collections)
        ;
        // pre_init(java/util/Collections$UnmodifiableList)
        ;
        // pre_init(java/util/Collections$UnmodifiableCollection)
        ;
        // pre_init(com/google/common/collect/Lists)
        ;
        // pre_init(com/google/common/base/Preconditions)
        ;
        // pre_init(java/util/ArrayList)
        ;
        // pre_init(com/google/common/collect/Collections2)
        ;
        // {R12} == Object[10] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing.header", Class.forName("common.LinkedList$Entry")));
        // {R20} == Object[11] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing.header.next", Class.forName("common.LinkedList$Entry")));
        // {R26} == Object[6] (aliases {ROOT}:root.myData.myOutgoing)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myOutgoing.header.next._owner", "{ROOT}:root.myData.myOutgoing"));
        // pre_init(common/LinkedList)
        ;
        // {V9} >= 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V9 = (int) variables.get(0);
                return (V9) >= (1) ? 0 : isNaN((V9) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V9) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R23} == Object[12] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing.header.next.element", Class.forName("ganttproject.ExplicitDependencyImpl")));
        // {R24} == Object[10] (aliases {ROOT}:root.myData.myOutgoing.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myOutgoing.header.next.next", "{ROOT}:root.myData.myOutgoing.header"));
        // {V9} == 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V9 = (int) variables.get(0);
                return (V9) == (1) ? 0 : isNaN((V9) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V9) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // pre_init(java/util/AbstractList$Itr)
        ;
        // {R6} == Object[21] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myIncoming", Class.forName("common.LinkedList")));
        // {V40} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myIncoming.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V40 = (int) variables.get(0);
                return (V40) >= (0) ? 0 : isNaN((V40) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V40) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R32} == Object[25] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myIncoming.header", Class.forName("common.LinkedList$Entry")));
        // {R34} == Object[25] (aliases {ROOT}:root.myData.myIncoming.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myIncoming.header.next", "{ROOT}:root.myData.myIncoming.header"));
        // {R36} == Object[21] (aliases {ROOT}:root.myData.myIncoming)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myIncoming.header._owner", "{ROOT}:root.myData.myIncoming"));
        // {V40} == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myIncoming.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V40 = (int) variables.get(0);
                return (V40) == (0) ? 0 : isNaN((V40) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V40) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));

        final HashMap<String, Object> candidateObjects = new HashMap<>();
        candidateObjects.put("{ROOT}:this", __ROOT_this);
        candidateObjects.put("{ROOT}:root", __ROOT_root);

        double d = distance(pathConditionHandler, candidateObjects);
        if (d == 0.0d)
            System.out.println("test0 0 distance");
        return d;
    }
}
