/**
 * Copyright (C) 2010-2015 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser Public License as published by the
 * Free Software Foundation, either version 3.0 of the License, or (at your
 * option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License along
 * with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 *
 */
package org.evosuite.instrumentation.coverage;


import java.util.List;

import org.evosuite.Properties;
import org.evosuite.graphs.GraphPool;
import org.evosuite.graphs.cfg.BytecodeInstruction;
import org.evosuite.graphs.cfg.RawControlFlowGraph;
import org.evosuite.testcase.execution.ExecutionTracer;
import org.evosuite.utils.LoggingUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * BranchInstrumentation class.
 * </p>
 *
 * @author Copied from CFGMethodAdapter
 */
public class PathConditonInstrumentation implements MethodInstrumentation {  /*SUSHI: Path condition fitness*/

	/** Constant <code>logger</code> */
	protected static final Logger logger = LoggerFactory.getLogger(PathConditonInstrumentation.class);

    private static final String EXECUTION_TRACER = Type.getInternalName(ExecutionTracer.class);
    
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.evosuite.cfg.MethodInstrumentation#analyze(org.objectweb
	 * .asm.tree.MethodNode, org.jgrapht.Graph, java.lang.String,
	 * java.lang.String, int)
	 */
	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void analyze(ClassLoader classLoader, MethodNode mn, String className,
			String methodName, int access) {
		RawControlFlowGraph completeCFG = GraphPool.getInstance(classLoader).getRawCFG(className, methodName);
	
		if (mn.name.equals("<init>")) {
			return; //TODO: shall we cope with path conditions of constructors?
		}

		AbstractInsnNode firstInstr = mn.instructions.getFirst();
		for (BytecodeInstruction v : completeCFG.vertexSet()) {
			if (firstInstr.equals(v.getASMNode())) {
				InsnList instrumentation = getInstrumentation(v, mn, className, methodName, access);

				if (instrumentation == null)
					throw new IllegalStateException("error instrumenting node "
							+ v.toString());
				if (Properties.PATH_CONDITION_CHECK_AT_METHOD_EXIT) {
					mn.instructions.insert(v.getASMNode(), instrumentation);
					LoggingUtils.getEvoLogger().info("Instrumentation set to check path condition at method exit ");
				} else {
					mn.instructions.insertBefore(v.getASMNode(), instrumentation); /* this should be the default behavior */					
				}
				mn.maxStack += 7;
				break;
			}
		}
	}

	/**
	 * <p>
	 * getInstrumentation
	 * </p>
	 *
	 * @param instruction
	 *            a {@link org.evosuite.graphs.cfg.BytecodeInstruction} object.
	 * @return a {@link org.objectweb.asm.tree.InsnList} object.
	 */
	protected InsnList getInstrumentation(BytecodeInstruction instruction, MethodNode mn, String className,
	        String methodName, int access) {
		if (instruction == null)
			throw new IllegalArgumentException("null given");

		InsnList instrumentation = new InsnList();
		
		instrumentation.add(new LdcInsnNode(className));
		instrumentation.add(new LdcInsnNode(methodName));
				
		//count parameters out of the signature
		int numOfParams = 0;
		String sig = mn.desc;
		sig = sig.substring(1, sig.lastIndexOf(')'));
		if (sig.contains("["))
			sig = sig.replaceAll("\\[", "");
		String[] params = sig.split(";");
		for (String p : params) {
			int indexL = p.indexOf('L');
			if (indexL < 0)
				numOfParams += p.length();
			else 
				numOfParams += indexL + 1;
		}
		if ((mn.access & Opcodes.ACC_STATIC) <= 0) { //non static: receiver object is part of parameters
			numOfParams++;
		}
		
		addArrayOfParams(numOfParams, mn.localVariables, instrumentation);
		
		instrumentation.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
				EXECUTION_TRACER, "passedMethodCall", "(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V", false));
		logger.debug("Adding passedMethodCall");
		
		return instrumentation;
	}

	private void addArrayOfParams(int paramLength, List<LocalVariableNode> paramList, InsnList instrumentation) {
	    // Create array with length equal to paramLength
	    instrumentation.add(new IntInsnNode(Opcodes.BIPUSH, paramLength));
	    instrumentation.add(new TypeInsnNode(Opcodes.ANEWARRAY, "java/lang/Object"));

	    // Fill the created array with method parameters
	    for (int paramIndex = 0, paramByteCodeIndex = 0; paramIndex < paramLength; paramIndex++, paramByteCodeIndex++)  {
	    	instrumentation.add(new InsnNode(Opcodes.DUP));////instrumentation.add(new VarInsnNode(Opcodes.ALOAD, paramList.size()));
	    	instrumentation.add(new IntInsnNode(Opcodes.BIPUSH, paramIndex));

	    	LocalVariableNode p  = paramList.get(paramIndex);

	        if (p.desc.equals(Type.BOOLEAN_TYPE.getDescriptor())) {
	        	instrumentation.add(new VarInsnNode(Opcodes.ILOAD, paramByteCodeIndex));
	            instrumentation.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;"));
	        }
	        else if (p.desc.equals(Type.BYTE_TYPE.getDescriptor())) {
	            instrumentation.add(new VarInsnNode(Opcodes.ILOAD, paramByteCodeIndex));
	            instrumentation.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;"));
	        }
	        else if (p.desc.equals(Type.CHAR_TYPE.getDescriptor())) {
	            instrumentation.add(new VarInsnNode(Opcodes.ILOAD, paramByteCodeIndex));
	            instrumentation.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;"));
	        }
	        else if (p.desc.equals(Type.SHORT_TYPE.getDescriptor())) {
	            instrumentation.add(new VarInsnNode(Opcodes.ILOAD, paramByteCodeIndex));
	            instrumentation.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;"));
	        }
	        else if (p.desc.equals(Type.INT_TYPE.getDescriptor())) {
	            instrumentation.add(new VarInsnNode(Opcodes.ILOAD, paramByteCodeIndex));
	            instrumentation.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;"));
	        }
	        else if (p.desc.equals(Type.LONG_TYPE.getDescriptor())) {
	            instrumentation.add(new VarInsnNode(Opcodes.LLOAD, paramByteCodeIndex));
	            instrumentation.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;"));
	            paramByteCodeIndex++;
	        }
	        else if (p.desc.equals(Type.FLOAT_TYPE.getDescriptor())) {
	            instrumentation.add(new VarInsnNode(Opcodes.FLOAD, paramByteCodeIndex));
	            instrumentation.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;"));
	        }
	        else if (p.desc.equals(Type.DOUBLE_TYPE.getDescriptor())) {
	            instrumentation.add(new VarInsnNode(Opcodes.DLOAD, paramByteCodeIndex));
	            instrumentation.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;"));
	            paramByteCodeIndex++;
	        }
	        else
	            instrumentation.add(new VarInsnNode(Opcodes.ALOAD, paramByteCodeIndex));

	        instrumentation.add(new InsnNode(Opcodes.AASTORE));
	    }
	}
	
	
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.evosuite.cfg.MethodInstrumentation#executeOnExcludedMethods
	 * ()
	 */
	/** {@inheritDoc} */
	@Override
	public boolean executeOnExcludedMethods() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.evosuite.cfg.MethodInstrumentation#executeOnMainMethod()
	 */
	/** {@inheritDoc} */
	@Override
	public boolean executeOnMainMethod() {
		return false;
	}

}
