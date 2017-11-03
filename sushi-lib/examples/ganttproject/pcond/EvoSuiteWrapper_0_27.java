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

public class EvoSuiteWrapper_0_27 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;


    public double test0(ganttproject.DependencyGraph __ROOT_this, ganttproject.Node __ROOT_root) throws Exception {
        //generated for state .1.1.1.1.1.1.1.3.2.1.2.1.1.1.2.1.2.1.2.1.1.2.1[89]
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
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing.header.next.element", Class.forName("ganttproject.ImplicitSubSuperTaskDependency")));
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
        // {R27} == Object[3] (aliases {ROOT}:root)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myOutgoing.header.next.element.mySubTask", "{ROOT}:root"));
        // {R9} == Object[21] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myTxn", Class.forName("ganttproject.Transaction")));
        // WIDEN-I({V39}) == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myTxn.isRunning");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final boolean V39 = (boolean) variables.get(0);
                return (((V39) == false ? 0 : 1)) == (0) ? 0 : isNaN((((V39) == false ? 0 : 1)) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((((V39) == false ? 0 : 1)) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R4} == Object[22] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myTask", Class.forName("ganttproject.TaskImpl")));
        // {R28} == Object[23] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask", Class.forName("ganttproject.Node")));
        // {R36} == Object[24] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myTask", Class.forName("ganttproject.TaskImpl")));
        // {R25} == Object[10] (aliases {ROOT}:root.myData.myOutgoing.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myOutgoing.header.next.previous", "{ROOT}:root.myData.myOutgoing.header"));
        // {R37} == Object[25] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData", Class.forName("ganttproject.NodeData")));
        // {V62} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myLevel");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V62 = (int) variables.get(0);
                return (V62) >= (0) ? 0 : isNaN((V62) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V62) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V62} < 3
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myLevel");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V62 = (int) variables.get(0);
                return (V62) < (3) ? 0 : isNaN((V62) - (3)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V62) - (3));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R45} == Object[26] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myTxn", Class.forName("ganttproject.Transaction")));
        // WIDEN-I({V64}) == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myTxn.isRunning");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final boolean V64 = (boolean) variables.get(0);
                return (((V64) == false ? 0 : 1)) == (0) ? 0 : isNaN((((V64) == false ? 0 : 1)) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((((V64) == false ? 0 : 1)) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R42} == Object[27] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myIncoming", Class.forName("common.LinkedList")));
        // {V66} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myIncoming.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V66 = (int) variables.get(0);
                return (V66) >= (0) ? 0 : isNaN((V66) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V66) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R48} == Object[28] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myIncoming.header", Class.forName("common.LinkedList$Entry")));
        // {R50} == Object[28] (aliases {ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myIncoming.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myIncoming.header.next", "{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myIncoming.header"));
        // {R52} == Object[27] (aliases {ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myIncoming)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myIncoming.header._owner", "{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myIncoming"));
        // {V66} == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myIncoming.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V66 = (int) variables.get(0);
                return (V66) == (0) ? 0 : isNaN((V66) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V66) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R2} == Object[32] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.myData", Class.forName("ganttproject.GraphData")));
        // {R55} == Object[33] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.myData.myTxn", Class.forName("ganttproject.Transaction")));
        // WIDEN-I({V74}) == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.myData.myTxn.isRunning");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final boolean V74 = (boolean) variables.get(0);
                return (((V74) == false ? 0 : 1)) == (0) ? 0 : isNaN((((V74) == false ? 0 : 1)) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((((V74) == false ? 0 : 1)) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // pre_init(java/util/Collections$UnmodifiableCollection$1)
        ;
        // pre_init(common/LinkedList$ListItr)
        ;
        // 0 == {V62}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.header.next.element.mySuperTask.myData.myLevel");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V62 = (int) variables.get(0);
                return (0) == (V62) ? 0 : isNaN((0) - (V62)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((0) - (V62));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R6} == Object[37] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myIncoming", Class.forName("common.LinkedList")));
        // {V78} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myIncoming.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V78 = (int) variables.get(0);
                return (V78) >= (0) ? 0 : isNaN((V78) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V78) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R57} == Object[41] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myIncoming.header", Class.forName("common.LinkedList$Entry")));
        // {R59} == Object[41] (aliases {ROOT}:root.myData.myIncoming.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myIncoming.header.next", "{ROOT}:root.myData.myIncoming.header"));
        // {R61} == Object[37] (aliases {ROOT}:root.myData.myIncoming)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myIncoming.header._owner", "{ROOT}:root.myData.myIncoming"));
        // {V78} == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myIncoming.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V78 = (int) variables.get(0);
                return (V78) == (0) ? 0 : isNaN((V78) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V78) - (0));
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
