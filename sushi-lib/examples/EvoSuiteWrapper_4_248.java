import static sushi.compile.path_condition_distance.DistanceBySimilarityWithPathCondition.distance;

import static java.lang.Double.*;
import static java.lang.Math.*;

import sushi.compile.path_condition_distance.*;
import sushi.logging.Level;
import sushi.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvoSuiteWrapper_4_248 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;


    public double test0(avl_tree.AvlTree __ROOT_this, int __ROOT_x) throws Exception {
        //generated for state .1.1.1.1.1.1.2.2.2.2.1.2.1.2[120]
        final ArrayList<ClauseSimilarityHandler> pathConditionHandler = new ArrayList<>();
        ValueCalculator valueCalculator;
        // {R0} == Object[0] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this", Class.forName("avl_tree.AvlTree")));
        // pre_init(avl_tree/AvlTree)
        ;
        // !pre_init(java/lang/Object)
        ;
        // {R1} == Object[1] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root", Class.forName("avl_tree.AvlNode")));
        // {V0} < {V1}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:x");
                retVal.add("{ROOT}:this.root.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                final int V1 = (int) variables.get(1);
                return (V0) < (V1) ? 0 : isNaN((V0) - (V1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R3} == Object[2] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.left", Class.forName("avl_tree.AvlNode")));
        // {V0} < {V5}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:x");
                retVal.add("{ROOT}:this.root.left.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                final int V5 = (int) variables.get(1);
                return (V0) < (V5) ? 0 : isNaN((V0) - (V5)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V5));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R10} == Object[3] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.left.left", Class.forName("avl_tree.AvlNode")));
        // {V0} >= {V9}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:x");
                retVal.add("{ROOT}:this.root.left.left.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                final int V9 = (int) variables.get(1);
                return (V0) >= (V9) ? 0 : isNaN((V0) - (V9)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V9));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V0} <= {V9}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:x");
                retVal.add("{ROOT}:this.root.left.left.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                final int V9 = (int) variables.get(1);
                return (V0) <= (V9) ? 0 : isNaN((V0) - (V9)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V9));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R17} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.left.left.left"));
        // {R18} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.left.left.right"));
        // {R11} == Object[4] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.left.right", Class.forName("avl_tree.AvlNode")));
        //  ~ {V14} != 2
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V14 = (int) variables.get(0);
                return (-(V14)) != (2) ? 0 : isNaN((-(V14)) - (2)) ? BIG_DISTANCE : SMALL_DISTANCE;
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // 0 > {V14}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V14 = (int) variables.get(0);
                return (0) > (V14) ? 0 : isNaN((0) - (V14)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((0) - (V14));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R4} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.right"));
        // {R2} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.parent"));

        final HashMap<String, Object> candidateObjects = new HashMap<>();
        candidateObjects.put("{ROOT}:this", __ROOT_this);
        candidateObjects.put("{ROOT}:x", __ROOT_x);

        double d = distance(pathConditionHandler, candidateObjects);
        if (d == 0.0d)
            System.out.println("test0 0 distance");
        return d;
    }
}
