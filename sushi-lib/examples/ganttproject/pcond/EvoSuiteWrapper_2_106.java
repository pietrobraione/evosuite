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

public class EvoSuiteWrapper_2_106 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;


    public double test0(ganttproject.DependencyGraph __ROOT_this) throws Exception {
        //generated for state .1.2.3.1.2.1[41]
        final ArrayList<ClauseSimilarityHandler> pathConditionHandler = new ArrayList<>();
        ValueCalculator valueCalculator;
        // {R0} == Object[0] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this", Class.forName("ganttproject.DependencyGraph")));
        // pre_init(ganttproject/DependencyGraph)
        ;
        // {R2} == Object[1] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.myData", Class.forName("ganttproject.GraphData")));
        // {R5} == Object[2] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.myData.myTxn", Class.forName("ganttproject.Transaction")));
        // {R1} == Object[2] (aliases {ROOT}:this.myData.myTxn)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:this.myTxn", "{ROOT}:this.myData.myTxn"));
        // pre_init(jbse/meta/Analysis)
        ;
        // WIDEN-I({V3}) != 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.myData.myTxn.isRunning");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final boolean V3 = (boolean) variables.get(0);
                return (((V3) == false ? 0 : 1)) != (0) ? 0 : isNaN((((V3) == false ? 0 : 1)) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE;
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R4} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.myData.myBackup"));
        // {R6} == Object[3] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.myData.myTxn.myTouchedNodes", Class.forName("common.LinkedList")));
        // {V6} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.myData.myTxn.myTouchedNodes.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                return (V6) >= (0) ? 0 : isNaN((V6) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V6) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
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
        // {R8} == Object[5] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.myData.myTxn.myTouchedNodes.header", Class.forName("common.LinkedList$Entry")));
        // {R10} == Object[5] (aliases {ROOT}:this.myData.myTxn.myTouchedNodes.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:this.myData.myTxn.myTouchedNodes.header.next", "{ROOT}:this.myData.myTxn.myTouchedNodes.header"));
        // {R12} == Object[3] (aliases {ROOT}:this.myData.myTxn.myTouchedNodes)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:this.myData.myTxn.myTouchedNodes.header._owner", "{ROOT}:this.myData.myTxn.myTouchedNodes"));
        // {V6} == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.myData.myTxn.myTouchedNodes.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                return (V6) == (0) ? 0 : isNaN((V6) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V6) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));

        final HashMap<String, Object> candidateObjects = new HashMap<>();
        candidateObjects.put("{ROOT}:this", __ROOT_this);

        double d = distance(pathConditionHandler, candidateObjects);
        if (d == 0.0d)
            System.out.println("test0 0 distance");
        return d;
    }
}
