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

public class EvoSuiteWrapper_1_93 {
    private static final double SMALL_DISTANCE = 1;
    private static final double BIG_DISTANCE = 1E300;


    private final SushiLibCache cache = new SushiLibCache();
    private final HashMap constants = new HashMap<>();
    private final ClassLoader classLoader;
    public EvoSuiteWrapper_1_93(ClassLoader classLoader) {
    		this.classLoader = classLoader;
    }
    
    public double test0(avl_tree.AvlTree __ROOT_this, int __ROOT_x) throws Exception {
        //generated for state .1.2[15]
        final ArrayList<ClauseSimilarityHandler> pathConditionHandler = new ArrayList<>();
        ValueCalculator valueCalculator;
        // {R0} == Object[0] (fresh)
        pathConditionHandler.add(new SimilarityWithRefToFreshObject("{ROOT}:this", Class.forName("avl_tree.AvlTree")));
        // pre_init(avl_tree/AvlTree)
        ;
        // !pre_init(java/lang/Object)
        ;
        // {R1} == null
        pathConditionHandler.add(new SimilarityWithRefToNull("{ROOT}:this.root"));

        final HashMap<String, Object> candidateObjects = new HashMap<>();
        candidateObjects.put("{ROOT}:this", __ROOT_this);
        candidateObjects.put("{ROOT}:x", __ROOT_x);

        double d = distance(pathConditionHandler, candidateObjects, constants, classLoader, cache);
        if (d == 0.0d)
            System.out.println("test0 0 distance");
        return d;
    }
}
