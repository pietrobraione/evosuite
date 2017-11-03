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

public class EvoSuiteWrapper_0_56 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;

    private static final String STRING_LITERAL_0 = "int";

    public double test0(ganttproject.DependencyGraph __ROOT_this, ganttproject.Node __ROOT_root) throws Exception {
        //generated for state .1.1.1.1.1.2.1.1.1.1.2.2.1.1.2.2.1.3.2.1[797]
        final ArrayList<ClauseSimilarityHandler> pathConditionHandler = new ArrayList<>();
        ValueCalculator valueCalculator;
        // {R0} == Object[0] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this", Class.forName("ganttproject.DependencyGraph")));
        // pre_init(ganttproject/DependencyGraph)
        ;
        // !pre_init(java/lang/Object)
        ;
        // !pre_init(java/util/LinkedList)
        ;
        // pre_init(java/util/AbstractSequentialList)
        ;
        // pre_init(java/util/AbstractList)
        ;
        // pre_init(java/util/AbstractCollection)
        ;
        // !pre_init(java/util/LinkedList$Entry)
        ;
        // {R3} == Object[3] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root", Class.forName("ganttproject.Node")));
        // {R5} == Object[5] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData", Class.forName("ganttproject.NodeData")));
        // {V0} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myLevel");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                return (V0) >= (0) ? 0 : isNaN((V0) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // pre_init(jbse/meta/Analysis)
        ;
        // {V0} < 3
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myLevel");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                return (V0) < (3) ? 0 : isNaN((V0) - (3)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (3));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R7} == Object[6] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myOutgoing", Class.forName("common.LinkedList")));
        // {V1} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V1 = (int) variables.get(0);
                return (V1) >= (0) ? 0 : isNaN((V1) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V1) - (0));
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
        // {R20} == Object[10] (aliases {ROOT}:root.myData.myOutgoing.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myOutgoing.header.next", "{ROOT}:root.myData.myOutgoing.header"));
        // {R22} == Object[6] (aliases {ROOT}:root.myData.myOutgoing)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myOutgoing.header._owner", "{ROOT}:root.myData.myOutgoing"));
        // pre_init(common/LinkedList)
        ;
        // {V1} == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myOutgoing.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V1 = (int) variables.get(0);
                return (V1) == (0) ? 0 : isNaN((V1) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V1) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // !pre_init(java/lang/Class)
        ;
        // !pre_init(java/lang/String)
        ;
        // !pre_init(java/lang/String$CaseInsensitiveComparator)
        ;
        // pre_init(java/util/AbstractList$Itr)
        ;
        // {R6} == Object[19] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myIncoming", Class.forName("common.LinkedList")));
        // {V18} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myIncoming.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V18 = (int) variables.get(0);
                return (V18) >= (0) ? 0 : isNaN((V18) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V18) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R23} == Object[23] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myIncoming.header", Class.forName("common.LinkedList$Entry")));
        // {R25} == Object[24] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myIncoming.header.next", Class.forName("common.LinkedList$Entry")));
        // {R31} == Object[19] (aliases {ROOT}:root.myData.myIncoming)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myIncoming.header.next._owner", "{ROOT}:root.myData.myIncoming"));
        // {V18} >= 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myIncoming.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V18 = (int) variables.get(0);
                return (V18) >= (1) ? 0 : isNaN((V18) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V18) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R28} == Object[25] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myIncoming.header.next.element", Class.forName("ganttproject.ImplicitInheritedDependency")));
        // {R29} == Object[23] (aliases {ROOT}:root.myData.myIncoming.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myIncoming.header.next.next", "{ROOT}:root.myData.myIncoming.header"));
        // {V18} == 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myIncoming.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V18 = (int) variables.get(0);
                return (V18) == (1) ? 0 : isNaN((V18) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V18) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R32} == Object[27] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myIncoming.header.next.element.myExplicitDep", Class.forName("ganttproject.ExplicitDependencyImpl")));
        // {R39} == Object[28] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myIncoming.header.next.element.myExplicitDep.myDstNode", Class.forName("ganttproject.Node")));
        // {R33} == Object[3] (aliases {ROOT}:root)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myIncoming.header.next.element.mySrc", "{ROOT}:root"));
        // {R9} == Object[29] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myData.myTxn", Class.forName("ganttproject.Transaction")));
        // WIDEN-I({V24}) == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myTxn.isRunning");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final boolean V24 = (boolean) variables.get(0);
                return (((V24) == false ? 0 : 1)) == (0) ? 0 : isNaN((((V24) == false ? 0 : 1)) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((((V24) == false ? 0 : 1)) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R34} == Object[3] (aliases {ROOT}:root)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myIncoming.header.next.element.myDst", "{ROOT}:root"));
        // {R4} == Object[30] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:root.myTask", Class.forName("ganttproject.TaskImpl")));
        // {R30} == Object[23] (aliases {ROOT}:root.myData.myIncoming.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:root.myData.myIncoming.header.next.previous", "{ROOT}:root.myData.myIncoming.header"));
        // {R2} == Object[34] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.myData", Class.forName("ganttproject.GraphData")));
        // {R49} == Object[35] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.myData.myTxn", Class.forName("ganttproject.Transaction")));
        // WIDEN-I({V34}) != 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.myData.myTxn.isRunning");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final boolean V34 = (boolean) variables.get(0);
                return (((V34) == false ? 0 : 1)) != (0) ? 0 : isNaN((((V34) == false ? 0 : 1)) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE;
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R48} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.myData.myBackup"));
        // pre_init(ganttproject/GraphData)
        ;
        // pre_init(ganttproject/GraphData$IntegerComparator)
        ;
        // pre_init(ganttproject/GraphData$NodeComparator)
        ;
        // pre_init(com/google/common/collect/TreeMultimap)
        ;
        // pre_init(com/google/common/collect/AbstractSortedSetMultimap)
        ;
        // pre_init(com/google/common/collect/AbstractSetMultimap)
        ;
        // pre_init(com/google/common/collect/AbstractMultimap)
        ;
        // pre_init(java/util/TreeMap)
        ;
        // pre_init(java/util/AbstractMap)
        ;
        // pre_init(java/util/Collections$UnmodifiableCollection$1)
        ;
        // pre_init(common/LinkedList$ListItr)
        ;
        // 0 != {V0}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myLevel");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                return (0) != (V0) ? 0 : isNaN((0) - (V0)) ? BIG_DISTANCE : SMALL_DISTANCE;
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // !pre_init(java/lang/Integer)
        ;
        // !pre_init(java/lang/Number)
        ;
        // !pre_init(java/lang/Integer$IntegerCache)
        ;
        //  ~ {V0} - 1 + 128 == 125
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:root.myData.myLevel");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                return (((-(V0)) - (1)) + (128)) == (125) ? 0 : isNaN((((-(V0)) - (1)) + (128)) - (125)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((((-(V0)) - (1)) + (128)) - (125));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // !pre_init(java/util/TreeSet)
        ;
        // pre_init(java/util/AbstractSet)
        ;
        // pre_init(java/util/TreeMap$Entry)
        ;

        final HashMap<String, Object> candidateObjects = new HashMap<>();
        candidateObjects.put("{ROOT}:this", __ROOT_this);
        candidateObjects.put("{ROOT}:root", __ROOT_root);

        double d = distance(pathConditionHandler, candidateObjects);
        if (d == 0.0d)
            System.out.println("test0 0 distance");
        return d;
    }
}
