package avl_tree.pcond;
import static sushi.compile.path_condition_distance.DistanceBySimilarityWithPathCondition.distance;

import static java.lang.Double.*;
import static java.lang.Math.*;

import sushi.compile.path_condition_distance.*;
import sushi.logging.Level;
import sushi.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EvoSuiteWrapper_4_95 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;

    private static final String STRING_LITERAL_0 = "_initialLeft";
    private static final String STRING_LITERAL_1 = "_initialRight";

    public double test0(avl_tree.AvlTree __ROOT_this, int __ROOT_x) throws Exception {
        //generated for state .1.1.1.2.1.1.1.1.1.2.1.2.1.2.1.2.1[133]
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
        // {R4} == Object[3] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this.root.right", Class.forName("avl_tree.AvlNode")));
        // {R10} == Object[1] (aliases {ROOT}:this.root)
        pathConditionHandler.add(new SimilarityWithRefToAlias("{ROOT}:this.root.right.parent", "{ROOT}:this.root"));
        // {V6} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                return (V6) >= (0) ? 0 : isNaN((V6) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V6) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V5} > {V1}
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.right.element");
                retVal.add("{ROOT}:this.root.element");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V5 = (int) variables.get(0);
                final int V1 = (int) variables.get(1);
                return (V5) > (V1) ? 0 : isNaN((V5) - (V1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V5) - (V1));
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
        // {V0} >= {V5}
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
                return (V0) >= (V5) ? 0 : isNaN((V0) - (V5)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V5));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {V0} > {V5}
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
                return (V0) > (V5) ? 0 : isNaN((V0) - (V5)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V0) - (V5));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R12} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.right.right"));
        // {V6} <= 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                return (V6) <= (1) ? 0 : isNaN((V6) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V6) - (1));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R11} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.right.left"));
        // {V6} == 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.right.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V6 = (int) variables.get(0);
                return (V6) == (0) ? 0 : isNaN((V6) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V6) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // {R3} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root.left"));
        // {V2} == 1
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V2 = (int) variables.get(0);
                return (V2) == (1) ? 0 : isNaN((V2) - (1)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V2) - (1));
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
