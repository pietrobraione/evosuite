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

public class EvoSuiteWrapper_0_40 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;


    public double test0(phandler.ExecutionHandler2 __ROOT_this, common.LinkedList __ROOT_primaryP, common.LinkedList __ROOT_secondaryP) throws Exception {
        //generated for state .1.1.1.2.1[303]
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
        // {R6} == Object[3] (aliases {ROOT}:primaryP.header)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header.next", "{ROOT}:primaryP.header"));
        // {R8} == Object[1] (aliases {ROOT}:primaryP)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:primaryP.header._owner", "{ROOT}:primaryP"));
        // {V2} == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:primaryP.size");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) == (0) ? 0 : isNaN((V2) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // pre_init(common/LinkedList$Entry)
        ;
        // !pre_init(java/util/HashSet)
        ;
        // pre_init(java/util/AbstractSet)
        ;
        // pre_init(java/util/HashMap)
        ;
        // pre_init(java/util/AbstractMap)
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
