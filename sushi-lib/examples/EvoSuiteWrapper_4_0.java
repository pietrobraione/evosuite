import static sushi.compile.path_condition_distance.DistanceBySimilarityWithPathCondition.distance;

import static java.lang.Double.*;
import static java.lang.Math.*;

import sushi.compile.path_condition_distance.*;
import sushi.logging.Level;
import sushi.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvoSuiteWrapper_4_0 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;

    private static final String STRING_LITERAL_0 = "_initialLeft";
    private static final String STRING_LITERAL_1 = "_initialRight";

    public double test0(avl_tree.AvlTree __ROOT_this, int __ROOT_x) throws Exception {
        //generated for state .1.1.1.1.1.1.1.1.1.1.1.1.1.1.2.1.2.1.1.1.1.1.1.1.1.1.1.1.1.1.1.1[111]
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
        // pre_init(avl_tree/AvlNode)
        ;
        // {R2} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.parent"));
        // {V2} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.height");
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
        // pre_init(avl_tree/Range)
        ;
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
        // {R3} == Object[3] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.left", Class.forName("avl_tree.AvlNode")));
        // {R10} == Object[1] (aliases {ROOT}:this.root)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:this.root.left.parent", "{ROOT}:this.root"));
        // {V6} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                return (V6) >= (0) ? 0 : isNaN((V6) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V6) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V5} < {V1}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.element");
                retVal.add("{ROOT}:this.root.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V5 = (int) variables.get(0);
                final int V1 = (int) variables.get(1);
                return (V5) < (V1) ? 0 : isNaN((V5) - (V1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V5) - (V1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // !pre_init(java/lang/String)
        ;
        // !pre_init(java/lang/String$CaseInsensitiveComparator)
        ;
        // {V2} >= 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) >= (1) ? 0 : isNaN((V2) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
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
        // {R11} == Object[9] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.left.left", Class.forName("avl_tree.AvlNode")));
        // {R17} == Object[3] (aliases {ROOT}:this.root.left)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:this.root.left.left.parent", "{ROOT}:this.root.left"));
        // {V10} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.left.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V10 = (int) variables.get(0);
                return (V10) >= (0) ? 0 : isNaN((V10) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V10) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V9} < {V5}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.left.element");
                retVal.add("{ROOT}:this.root.left.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V9 = (int) variables.get(0);
                final int V5 = (int) variables.get(1);
                return (V9) < (V5) ? 0 : isNaN((V9) - (V5)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V9) - (V5));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V6} >= 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                return (V6) >= (1) ? 0 : isNaN((V6) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V6) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V0} < {V9}
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
                return (V0) < (V9) ? 0 : isNaN((V0) - (V9)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V9));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R18} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.left.left.left"));
        // {V10} <= 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.left.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V10 = (int) variables.get(0);
                return (V10) <= (1) ? 0 : isNaN((V10) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V10) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R19} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.left.left.right"));
        // {V10} == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.left.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V10 = (int) variables.get(0);
                return (V10) == (0) ? 0 : isNaN((V10) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V10) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R12} == Object[14] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.left.right", Class.forName("avl_tree.AvlNode")));
        // {R24} == Object[3] (aliases {ROOT}:this.root.left)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:this.root.left.right.parent", "{ROOT}:this.root.left"));
        // {V14} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V14 = (int) variables.get(0);
                return (V14) >= (0) ? 0 : isNaN((V14) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V14) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V13} < {V1}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.right.element");
                retVal.add("{ROOT}:this.root.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V13 = (int) variables.get(0);
                final int V1 = (int) variables.get(1);
                return (V13) < (V1) ? 0 : isNaN((V13) - (V1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V13) - (V1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V13} > {V5}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.right.element");
                retVal.add("{ROOT}:this.root.left.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V13 = (int) variables.get(0);
                final int V5 = (int) variables.get(1);
                return (V13) > (V5) ? 0 : isNaN((V13) - (V5)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V13) - (V5));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V6} >= {V14} + 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.height");
                retVal.add("{ROOT}:this.root.left.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                final int V14 = (int) variables.get(1);
                return (V6) >= ((V14) + (1)) ? 0 : isNaN((V6) - ((V14) + (1))) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V6) - ((V14) + (1)));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // 2 * {V6} <= {V14} + {V10} + 3
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.height");
                retVal.add("{ROOT}:this.root.left.right.height");
                retVal.add("{ROOT}:this.root.left.left.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                final int V14 = (int) variables.get(1);
                final int V10 = (int) variables.get(2);
                return ((2) * (V6)) <= (((V14) + (V10)) + (3)) ? 0 : isNaN(((2) * (V6)) - (((V14) + (V10)) + (3))) ? BIG_DISTANCE : SMALL_DISTANCE + abs(((2) * (V6)) - (((V14) + (V10)) + (3)));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // 1 > {V14}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.left.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V14 = (int) variables.get(0);
                return (1) > (V14) ? 0 : isNaN((1) - (V14)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((1) - (V14));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R4} == Object[16] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.right", Class.forName("avl_tree.AvlNode")));
        // {R31} == Object[1] (aliases {ROOT}:this.root)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:this.root.right.parent", "{ROOT}:this.root"));
        // {V18} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V18 = (int) variables.get(0);
                return (V18) >= (0) ? 0 : isNaN((V18) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V18) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V17} > {V1}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.right.element");
                retVal.add("{ROOT}:this.root.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V17 = (int) variables.get(0);
                final int V1 = (int) variables.get(1);
                return (V17) > (V1) ? 0 : isNaN((V17) - (V1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V17) - (V1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V2} >= {V18} + 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.height");
                retVal.add("{ROOT}:this.root.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                final int V18 = (int) variables.get(1);
                return (V2) >= ((V18) + (1)) ? 0 : isNaN((V2) - ((V18) + (1))) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - ((V18) + (1)));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V2} >= {V6} + 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.height");
                retVal.add("{ROOT}:this.root.left.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                final int V6 = (int) variables.get(1);
                return (V2) >= ((V6) + (1)) ? 0 : isNaN((V2) - ((V6) + (1))) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - ((V6) + (1)));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // 2 * {V2} <= {V18} + {V6} + 3
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.height");
                retVal.add("{ROOT}:this.root.right.height");
                retVal.add("{ROOT}:this.root.left.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                final int V18 = (int) variables.get(1);
                final int V6 = (int) variables.get(2);
                return ((2) * (V2)) <= (((V18) + (V6)) + (3)) ? 0 : isNaN(((2) * (V2)) - (((V18) + (V6)) + (3))) ? BIG_DISTANCE : SMALL_DISTANCE + abs(((2) * (V2)) - (((V18) + (V6)) + (3)));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // 2 - {V18} == 2
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V18 = (int) variables.get(0);
                return ((2) - (V18)) == (2) ? 0 : isNaN(((2) - (V18)) - (2)) ? BIG_DISTANCE : SMALL_DISTANCE + abs(((2) - (V18)) - (2));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));

        final HashMap<String, Object> candidateObjects = new HashMap<>();
        candidateObjects.put("{ROOT}:this", __ROOT_this);
        candidateObjects.put("{ROOT}:x", __ROOT_x);

        double d = distance(pathConditionHandler, candidateObjects);
        if (d == 0.0d)
            System.out.println("test0 0 distance");
        return d;
    }
}
