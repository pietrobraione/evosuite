package sushi.minimize;

public class TestSuites {


	public static String[] treemapSushi() {
		return new String[] {
				"treemap.TreeMap_clear_PC_3_0_Test",
				"treemap.TreeMap_containsKey_PC_6_0_Test",
				"treemap.TreeMap_containsKey_PC_6_555_Test",
				"treemap.TreeMap_containsValue_PC_5_1255_Test",
				"treemap.TreeMap_containsValue_PC_5_1336_Test",
				"treemap.TreeMap_containsValue_PC_5_18_Test",
				"treemap.TreeMap_firstKey_PC_7_19_Test",
				"treemap.TreeMap_firstKey_PC_7_6_Test",
				"treemap.TreeMap_get_PC_1_0_Test",
				"treemap.TreeMap_get_PC_1_740_Test",
				"treemap.TreeMap_lastKey_PC_8_19_Test",
				"treemap.TreeMap_lastKey_PC_8_6_Test",
				"treemap.TreeMap_put_PC_2_1101_Test",
				"treemap.TreeMap_put_PC_2_1207_Test",
				"treemap.TreeMap_put_PC_2_121_Test",
				"treemap.TreeMap_put_PC_2_122_Test",
				"treemap.TreeMap_put_PC_2_315_Test",
				"treemap.TreeMap_put_PC_2_642_Test",
				"treemap.TreeMap_put_PC_2_748_Test",
				"treemap.TreeMap_put_PC_2_749_Test",
				"treemap.TreeMap_remove_PC_0_12_Test",
				"treemap.TreeMap_remove_PC_0_190_Test",
				"treemap.TreeMap_remove_PC_0_196_Test",
				"treemap.TreeMap_remove_PC_0_201_Test",
				"treemap.TreeMap_remove_PC_0_20_Test",
				"treemap.TreeMap_remove_PC_0_22_Test",
				"treemap.TreeMap_remove_PC_0_23_Test",
				"treemap.TreeMap_remove_PC_0_32_Test",
				"treemap.TreeMap_remove_PC_0_5_Test",
				"treemap.TreeMap_remove_PC_0_640_Test",
				"treemap.TreeMap_remove_PC_0_72_Test",
				"treemap.TreeMap_remove_PC_0_8_Test",
				"treemap.TreeMap_size_PC_4_0_Test"

		};
	}

	public static String[] treemapNOINV_SUSHI() {
		return new String[] {
				//TODO
		};	
	}
	
	public static String[] treemapNOINV_DHM() {
		return new String[] {
				"treemap.PC_remove_0_734_Test",
				"treemap.PC_remove_0_2368_Test",
				"treemap.PC_remove_0_2385_Test",
				"treemap.PC_remove_0_8685_Test",
				//"treemap.PC_remove_0_64_Test", /* false positive*/
				//"treemap.PC_remove_0_303_Test", /* false positive*/
				//"treemap.PC_remove_0_336_Test", /* false positive*/
				//"treemap.PC_remove_0_673_Test", /* false positive*/
				//"treemap.PC_remove_0_7005_Test", /* false positive*/
				"treemap.PC_get_1_0_Test",
				"treemap.PC_get_1_155_Test",
				"treemap.PC_put_2_1427_Test",
				"treemap.PC_put_2_1606_Test",
				"treemap.PC_put_2_3074_Test",
				"treemap.PC_put_2_5413_Test",
				//"treemap.PC_put_2_1609_Test", /* false positive*/
				//"treemap.PC_put_2_3612_Test", /* false positive*/
				//"treemap.PC_put_2_5226_Test", /* false positive*/
				"treemap.PC_clear_3_0_Test",
				"treemap.PC_size_4_0_Test",
				"treemap.PC_containsValue_5_240_Test",
				"treemap.PC_containsValue_5_80123_Test",
				"treemap.PC_containsValue_5_91707_Test",
				//"treemap.PC_containsValue_5_66107_Test", /* false positive*/
				"treemap.PC_containsKey_6_0_Test",
				"treemap.PC_containsKey_6_93_Test",				
				"treemap.PC_firstKey_7_3_Test",
				"treemap.PC_firstKey_7_5_Test",
				"treemap.PC_lastKey_8_3_Test",
				"treemap.PC_lastKey_8_5_Test"
		};
	}


	public static String[] avlSushi() {
		return new String[] {
				"avl_tree.AvlTree_findMax_PC_2_3_Test",
				"avl_tree.AvlTree_findMax_PC_2_5_Test",
				"avl_tree.AvlTree_findMin_PC_3_3_Test",
				"avl_tree.AvlTree_findMin_PC_3_5_Test",
				"avl_tree.AvlTree_find_PC_1_42_Test",
				"avl_tree.AvlTree_find_PC_1_93_Test",
				"avl_tree.AvlTree_insertElem_PC_4_0_Test",
				"avl_tree.AvlTree_insertElem_PC_4_23_Test",
				"avl_tree.AvlTree_insertElem_PC_4_75_Test",
				"avl_tree.AvlTree_insertElem_PC_4_95_Test",
				"avl_tree.AvlTree_insertElem_PC_4_113_Test",
				"avl_tree.AvlTree_isEmpty_PC_0_0_Test",
				"avl_tree.AvlTree_isEmpty_PC_0_1_Test",
				"avl_tree.AvlTree_makeEmpty_PC_5_0_Test"
		};	
		
	}
	
	public static String[] avlNOINV_SUSHI() {
		return new String[] {
				//TODO
		};	
		
	}

	public static String[] avlNOINV_DHM() {
		return new String[] {
				"avl_tree.noinv.dhm.PC_isEmpty_0_0_Test",
				"avl_tree.noinv.dhm.PC_isEmpty_0_1_Test",
				"avl_tree.noinv.dhm.PC_find_1_42_Test",
				"avl_tree.noinv.dhm.PC_find_1_93_Test",
				"avl_tree.noinv.dhm.PC_findMax_2_3_Test",
				"avl_tree.noinv.dhm.PC_findMax_2_5_Test",
				"avl_tree.noinv.dhm.PC_findMin_3_3_Test",
				"avl_tree.noinv.dhm.PC_findMin_3_5_Test",
				//"avl_tree.noinv.dhm.PC_insertElem_4_248_Test", /* false positive*/
				//"avl_tree.noinv.dhm.PC_insertElem_4_785_Test", /* false positive*/
				"avl_tree.noinv.dhm.PC_makeEmpty_5_0_Test"
		};	
		
	}

	public static String[] cachingSushi() {
		return new String[] {
				"node_caching_linked_list.NodeCachingLinkedList_add_PC_0_0_Test",
				"node_caching_linked_list.NodeCachingLinkedList_add_PC_0_3_Test",
				"node_caching_linked_list.NodeCachingLinkedList_contains_PC_3_8_Test",
				"node_caching_linked_list.NodeCachingLinkedList_contains_PC_3_13_Test",
				"node_caching_linked_list.NodeCachingLinkedList_removeIndex_PC_4_0_Test",
				"node_caching_linked_list.NodeCachingLinkedList_removeIndex_PC_4_1_Test",
				"node_caching_linked_list.NodeCachingLinkedList_removeIndex_PC_4_2_Test",
				"node_caching_linked_list.NodeCachingLinkedList_removeIndex_PC_4_13_Test",
				"node_caching_linked_list.NodeCachingLinkedList_remove_PC_1_16_Test",
				"node_caching_linked_list.NodeCachingLinkedList_remove_PC_1_24_Test"
		};	
	}
	
	public static String[] cachingNOINV_SUSHI() {
		return new String[] {
				"node_caching_linked_list.noinv.NodeCachingLinkedList_addLast_PC_0_2_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_add_PC_2_0_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_add_PC_2_1_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_contains_PC_5_11_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_contains_PC_5_16_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_contains_PC_5_38_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_removeIndex_PC_1_0_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_removeIndex_PC_1_1_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_removeIndex_PC_1_2_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_removeIndex_PC_1_37_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_removeIndex_PC_1_42_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_remove_PC_3_114_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_remove_PC_3_118_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_remove_PC_3_29_Test",
				"node_caching_linked_list.noinv.NodeCachingLinkedList_remove_PC_3_91_Test"
		};	
	}

	public static String[] cachingNOINV_DHM() {
		return new String[] {
				"node_caching_linked_list.noinv.dhm.PC_add_0_0_Test",
				"node_caching_linked_list.noinv.dhm.PC_add_0_2_Test",
				"node_caching_linked_list.noinv.dhm.PC_contains_3_16_Test",
				"node_caching_linked_list.noinv.dhm.PC_contains_3_38_Test",
				"node_caching_linked_list.noinv.dhm.PC_removeIndex_4_0_Test",
				"node_caching_linked_list.noinv.dhm.PC_removeIndex_4_1_Test",
				"node_caching_linked_list.noinv.dhm.PC_removeIndex_4_2_Test",
				"node_caching_linked_list.noinv.dhm.PC_removeIndex_4_33_Test",
				//"node_caching_linked_list.noinv.dhm.PC_remove_1_86_Test", /* false positive*/
				"node_caching_linked_list.noinv.dhm.PC_remove_1_118_Test"
		};	
	}

	public static String[] closure01Sushi() {
		return new String[] {
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_143_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_199_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_346_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_3004_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_28340_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_44252_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_44789_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_45383_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_45848_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_45864_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_45867_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_68471_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_85529_Test"
		};	
	}
	
	public static String[] closure01DHM() {
		return new String[] {
				"com.google.javascript.jscomp.PC_driver_RemoveUnusedVars_process_0_199_Test",
				"com.google.javascript.jscomp.PC_driver_RemoveUnusedVars_process_0_44252_Test",
				"com.google.javascript.jscomp.PC_driver_RemoveUnusedVars_process_0_44789_Test",
				"com.google.javascript.jscomp.PC_driver_RemoveUnusedVars_process_0_45848_Test",
				"com.google.javascript.jscomp.PC_driver_RemoveUnusedVars_process_0_45864_Test",
				"com.google.javascript.jscomp.PC_driver_RemoveUnusedVars_process_0_45867_Test",
				"com.google.javascript.jscomp.PC_driver_RemoveUnusedVars_process_0_68471_Test"
			};	
	}
	public static String[] closure01NOINV_SUSHI() {
		return new String[] {
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_2003_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_2497_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_4417_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RemoveUnusedVars_process_PC_0_4471_Test"
		};	
	}

	
	public static String[] closure01NOINV_DHM() {
		return new String[] {
				"com.google.javascript.jscomp.PC_driver_RemoveUnusedVars_process_0_527_Test",
				"com.google.javascript.jscomp.PC_driver_RemoveUnusedVars_process_0_530_Test",
				"com.google.javascript.jscomp.PC_driver_RemoveUnusedVars_process_0_4441_Test"
				/* no false positives*/
		};	
	}

	public static String[] closure72Sushi() {
		return new String[] {
				"com.google.javascript.jscomp.AnalysisDriver_driver_RenameLabels_process_PC_0_118_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RenameLabels_process_PC_0_215_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RenameLabels_process_PC_0_38_Test"
		};
	}


	public static String[] closure72NOINV_SUSHI() {
		return new String[] {
				"com.google.javascript.jscomp.AnalysisDriver_driver_RenameLabels_process_PC_0_170_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_RenameLabels_process_PC_0_33_Test"
		};
	}

	public static String[] closure72NOINV_DHM() {
		return new String[] {
				"com.google.javascript.jscomp.PC_driver_RenameLabels_process_0_1600_Test", /* false positive*/
				"com.google.javascript.jscomp.PC_driver_RenameLabels_process_0_1726_Test", /* false positive*/
				"com.google.javascript.jscomp.PC_driver_RenameLabels_process_0_1838_Test", /* false positive*/
		};
	}

	public static String[] closure11Sushi() {
		return new String[] {
				"com.google.javascript.jscomp.AnalysisDriver_driver_TypeCheck_process_PC_0_0_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_TypeCheck_process_PC_0_3_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_TypeCheck_process_PC_0_41_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_TypeCheck_process_PC_0_42_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_TypeCheck_process_PC_0_47_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_TypeCheck_process_PC_0_59_Test",
				"com.google.javascript.jscomp.ManualTest00",
				"com.google.javascript.jscomp.ManualTest03",
				"com.google.javascript.jscomp.ManualTest05",
				"com.google.javascript.jscomp.ManualTest08",
				"com.google.javascript.jscomp.ManualTest041",
				"com.google.javascript.jscomp.ManualTest042",
				"com.google.javascript.jscomp.ManualTest045",
				"com.google.javascript.jscomp.ManualTest047",
				"com.google.javascript.jscomp.ManualTest059",
				"com.google.javascript.jscomp.ManualTest060",
				"com.google.javascript.jscomp.ManualTest062",
				"com.google.javascript.jscomp.ManualTest063",
				"com.google.javascript.jscomp.ManualTest064",
				"com.google.javascript.jscomp.ManualTest065",
				"com.google.javascript.jscomp.ManualTest066",
				"com.google.javascript.jscomp.ManualTest069",
				"com.google.javascript.jscomp.ManualTest070",
				"com.google.javascript.jscomp.ManualTest071",
				"com.google.javascript.jscomp.ManualTest072",
				"com.google.javascript.jscomp.ManualTest084",
				"com.google.javascript.jscomp.ManualTest086",
				"com.google.javascript.jscomp.ManualTest087",
				"com.google.javascript.jscomp.ManualTest088",
				"com.google.javascript.jscomp.ManualTest0253"
		};
	}
	
	public static String[] tsafeSushi() {
		return new String[] {
				"tsafe.Driver_TS_R_TS_R_3_PC_0_2_Test",
				"tsafe.Driver_TS_R_TS_R_3_PC_0_3_Test",
				"tsafe.Driver_TS_R_TS_R_3_PC_0_29_Test",
				"tsafe.Driver_TS_R_TS_R_3_PC_0_106_Test",
				"tsafe.Driver_TS_R_TS_R_3_PC_0_107_Test",
				"tsafe.Driver_TS_R_TS_R_3_PC_0_109_Test",
				"tsafe.Driver_TS_R_TS_R_3_PC_0_112_Test",
				"tsafe.Driver_TS_R_TS_R_3_PC_0_113_Test",
				"tsafe.Driver_TS_R_TS_R_3_PC_0_122_Test",
				"tsafe.Driver_TS_R_TS_R_3_PC_0_123_Test"
		};
	}
	
	public static String[] tsafeNOINV_SUSHI() {
		return new String[] {
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_1002_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_1003_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_1016_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_1021_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_128_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_130_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_131_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_244_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_363_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_908_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_992_Test",
				"tsafe.noinv.Driver_TS_R_TS_R_3_PC_0_993_Test"
		};
	}

	public static String[] tsafeNOINV_DHM() {
		return new String[] {
				/*non generated because of non-linear path condition*///"tsafe.noinv.dhm.Driver_TS_R_TS_R_3_PC_0_128_Test",/*non generated because non-linear path condition*///
				/*non generated because of non-linear path condition*///"tsafe.noinv.dhm.Driver_TS_R_TS_R_3_PC_0_365_Test",/* false positive*/
				/*non generated because of non-linear path condition*///"tsafe.noinv.dhm.Driver_TS_R_TS_R_3_PC_0_366_Test",
				/*non generated because of non-linear path condition*///"tsafe.noinv.dhm.Driver_TS_R_TS_R_3_PC_0_908_Test",
				/*non generated because of non-linear path condition*///"tsafe.noinv.dhm.Driver_TS_R_TS_R_3_PC_0_909_Test",
				/*non generated because of non-linear path condition*///"tsafe.noinv.dhm.Driver_TS_R_TS_R_3_PC_0_989_Test",
				/*non generated because of non-linear path condition*///"tsafe.noinv.dhm.Driver_TS_R_TS_R_3_PC_0_992_Test",
				/*non generated because of non-linear path condition*///"tsafe.noinv.dhm.Driver_TS_R_TS_R_3_PC_0_993_Test",
				"tsafe.noinv.dhm.PC_TS_R_3_0_1002_Test",
				"tsafe.noinv.dhm.PC_TS_R_3_0_1003_Test"
		};
	}

	public static String[] ganttSushi() {
		return new String[] {
				"ganttproject.DependencyGraph_removeImplicitDependencies_PC_0_1_Test",
				"ganttproject.DependencyGraph_removeImplicitDependencies_PC_0_13_Test",
				"ganttproject.DependencyGraph_removeImplicitDependencies_PC_0_33_Test",
				"ganttproject.DependencyGraph_removeImplicitDependencies_PC_0_38_Test",
				"ganttproject.DependencyGraph_startTransaction_PC_1_0_Test",
				"ganttproject.DependencyGraph_startTransaction_PC_1_1_Test",
				"ganttproject.DependencyGraph_rollbackTransaction_PC_2_0_Test",
				"ganttproject.DependencyGraph_rollbackTransaction_PC_2_106_Test"
		};
	}
	
	public static String[] ganttNOINV_SUSHI() {
		return new String[] {
				"ganttproject.noinv.sushi.DependencyGraph_removeImplicitDependencies_PC_0_18451_Test",
				"ganttproject.noinv.sushi.DependencyGraph_rollbackTransaction_PC_2_0_Test",
				"ganttproject.noinv.sushi.DependencyGraph_rollbackTransaction_PC_2_123_Test",
				"ganttproject.noinv.sushi.DependencyGraph_startTransaction_PC_1_0_Test", 
				"ganttproject.noinv.sushi.DependencyGraph_startTransaction_PC_1_1_Test" 
		};
	}

	public static String[] ganttNOINV_DHM() {
		return new String[] {
				//"ganttproject.noinv.dhm.PC_removeImplicitDependencies_0_1563_Test",/* false positive*/
				"ganttproject.noinv.dhm.PC_rollbackTransaction_2_0_Test", 
				//"ganttproject.noinv.dhm.PC_rollbackTransaction_2_95_Test",/* false positive*/
				"ganttproject.noinv.dhm.PC_startTransaction_1_0_Test", 
				"ganttproject.noinv.dhm.PC_startTransaction_1_1_Test" 
		};
	}

	public static String[] closure08Sushi() {
		return new String[] {
				//"com.google.javascript.jscomp.AnalysisDriver_driver_CollapseVariableDeclarations_process_PC_0_21_Test",
				//"com.google.javascript.jscomp.AnalysisDriver_driver_CollapseVariableDeclarations_process_PC_0_153_Test",
				//"com.google.javascript.jscomp.AnalysisDriver_driver_CollapseVariableDeclarations_process_PC_0_165_Test",
				//"com.google.javascript.jscomp.AnalysisDriver_driver_CollapseVariableDeclarations_process_PC_0_18_Test",
				//"com.google.javascript.jscomp.AnalysisDriver_driver_CollapseVariableDeclarations_process_PC_0_969_Test",
				//"com.google.javascript.jscomp.AnalysisDriver_driver_CollapseVariableDeclarations_process_PC_0_1047_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_CollapseVariableDeclarations_process_PC_0_373_Test",
				"com.google.javascript.jscomp.AnalysisDriver_driver_CollapseVariableDeclarations_process_PC_0_6713_Test"
		};
	}


}
