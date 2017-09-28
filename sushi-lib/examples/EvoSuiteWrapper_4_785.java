import static sushi.compile.path_condition_distance.DistanceBySimilarityWithPathCondition.distance;

import static java.lang.Double.*;
import static java.lang.Math.*;

import sushi.compile.path_condition_distance.*;
import sushi.logging.Level;
import sushi.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvoSuiteWrapper_4_785 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;


    public double test0(avl_tree.AvlTree __ROOT_this, int __ROOT_x) throws Exception {
        //generated for state .1.1.2.1.1.1.1.2.1.1.2.1.2.2.2.2.1.1[186]
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
        // {V0} >= {V1}
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
                return (V0) >= (V1) ? 0 : isNaN((V0) - (V1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V0} > {V1}
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
                return (V0) > (V1) ? 0 : isNaN((V0) - (V1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R4} == Object[2] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.right", Class.forName("avl_tree.AvlNode")));
        // {V0} < {V5}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:x");
                retVal.add("{ROOT}:this.root.right.element");
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
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.right.left", Class.forName("avl_tree.AvlNode")));
        // {V0} >= {V9}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:x");
                retVal.add("{ROOT}:this.root.right.left.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                final int V9 = (int) variables.get(1);
                return (V0) >= (V9) ? 0 : isNaN((V0) - (V9)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V9));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V0} > {V9}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:x");
                retVal.add("{ROOT}:this.root.right.left.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                final int V9 = (int) variables.get(1);
                return (V0) > (V9) ? 0 : isNaN((V0) - (V9)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V9));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R18} == Object[4] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.right.left.right", Class.forName("avl_tree.AvlNode")));
        // {V0} >= {V13}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:x");
                retVal.add("{ROOT}:this.root.right.left.right.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                final int V13 = (int) variables.get(1);
                return (V0) >= (V13) ? 0 : isNaN((V0) - (V13)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V13));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V0} > {V13}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:x");
                retVal.add("{ROOT}:this.root.right.left.right.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V0 = (int) variables.get(0);
                final int V13 = (int) variables.get(1);
                return (V0) > (V13) ? 0 : isNaN((V0) - (V13)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V13));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R25} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.right.left.right.right"));
        // pre_init(avl_tree/AvlNode)
        ;
        // {R24} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.right.left.right.left"));
        // {R17} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.right.left.left"));
        // {R16} == Object[2] (aliases {ROOT}:this.root.right)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:this.root.right.left.parent", "{ROOT}:this.root.right"));
        // {R11} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.right.right"));
        // {R9} == Object[1] (aliases {ROOT}:this.root)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:this.root.right.parent", "{ROOT}:this.root"));
        // {R3} == Object[6] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.left", Class.forName("avl_tree.AvlNode")));
        // 2 - {V18} == 2
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V18 = (int) variables.get(0);
                return ((2) - (V18)) == (2) ? 0 : isNaN(((2) - (V18)) - (2)) ? BIG_DISTANCE : SMALL_DISTANCE + abs(((2) - (V18)) - (2));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
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
