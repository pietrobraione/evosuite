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

import avl_tree.AvlTree;

public class EvoSuiteWrapper_0_0 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;
    
    private final SushiLibCache cache = new SushiLibCache();
    private final HashMap constants = new HashMap<>();
    private final ClassLoader classLoader;
    public EvoSuiteWrapper_0_0(ClassLoader classLoader) {
    		this.classLoader = classLoader;
    }
    
    public static void main(String[] args) throws Exception {
    		new EvoSuiteWrapper_0_0(null).test0(new AvlTree());
    }
    
    public double test0(avl_tree.AvlTree __ROOT_this) throws Exception {
        //generated for state .1.1.1[39]
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
        // {V1} >= 0
        valueCalculator = new ValueCalculator() {
            @Override public Iterable<String> getVariableOrigins() {
                ArrayList<String> retVal = new ArrayList<>();
                retVal.add("{ROOT}:this.root.height");
                return retVal;
            }
            @Override public double calculate(List<Object> variables) {
                final int V1 = (int) variables.get(0);
                return (V1) >= (0) ? 0 : isNaN((V1) - (0)) ? BIG_DISTANCE : SMALL_DISTANCE + abs((V1) - (0));
            }
        };
        pathConditionHandler.add(new SimilarityWithNumericExpression(valueCalculator));
        // pre_init(jbse/meta/Analysis)
        ;
        // pre_init(avl_tree/Range)
        ;

        final HashMap<String, Object> candidateObjects = new HashMap<>();
        candidateObjects.put("{ROOT}:this", __ROOT_this);

        double d = distance(pathConditionHandler, candidateObjects, constants, classLoader, cache);
        if (d == 0.0d)
            System.out.println("test0 0 distance");
        return d;
    }
}
