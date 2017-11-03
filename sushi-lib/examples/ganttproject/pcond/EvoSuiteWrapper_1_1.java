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

public class EvoSuiteWrapper_1_1 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;


    public double test0(ganttproject.DependencyGraph __ROOT_this) throws Exception {
        //generated for state .1.2[8]
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
        // WIDEN-I({V3}) == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.myData.myTxn.isRunning");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final boolean V3 = (boolean) variables.get(0);
                return (((V3) == false ? 0 : 1)) == (0) ? 0 : isNaN((((V3) == false ? 0 : 1)) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((((V3) == false ? 0 : 1)) - (0));
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
