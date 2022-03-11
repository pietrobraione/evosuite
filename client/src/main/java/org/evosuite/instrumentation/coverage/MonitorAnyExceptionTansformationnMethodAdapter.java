/*
 * Copyright (C) 2010-2018 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3.0 of the License, or
 * (at your option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.instrumentation.coverage;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by giovanni on 17/02/2022.
 * 
 * Add a try-catch(Throwable) block that encloses the method body, and thus intercepts any 
 * exception that propagates to callers. The block simply reflects the exception, thus making
 * the execution continuing as expected, but at the same time allowing for a location 
 * where we can introduce instrumentation related to the happening of the exception. For example,
 * this is exploited for evaluating fitness related to satisfying post-conditions in PathConditionFitness.
 */
public class MonitorAnyExceptionTansformationnMethodAdapter extends GeneratorAdapter {

    protected final static Logger logger = LoggerFactory.getLogger(MonitorAnyExceptionTansformationnMethodAdapter.class);

    private final String className;
    private final String methodName;
    private final MethodVisitor next;
    private final List<TryCatchBlock> tryCatchBlockss = new LinkedList<>();
    
    private static class TryCatchBlock {
        Label start;
        Label end;
        Label handler;
        String type;
        public TryCatchBlock(Label start, Label end, Label handler, String type) {
            this.start = start;
            this.end = end;
            this.handler = handler;
            this.type = type;
        }
    }

    public MonitorAnyExceptionTansformationnMethodAdapter(MethodVisitor mv, String className,
                                                String methodName, int access, String desc) {
        super(Opcodes.ASM9, mv, access, methodName, desc);
        //super(Opcodes.ASM9,
        //        new AnnotatedMethodNode(access, methodName, desc, null, null), access,
        //        methodName, desc);
        this.className = className;
        this.methodName = methodName;
        next = mv;

    }

	private Label start = newLabel();
	private Label end   = newLabel();
	private Label catchLabel  = newLabel();
    int exceptionInstanceVar;
    boolean isCallToSuperClassConstructor = false;
	
    @Override
    public void visitCode() {
    	if (methodName.equals("<init>")) {
            isCallToSuperClassConstructor = true; // postpone exception handling block after super-class constructor 
    	} else {
    		addStartOfDominantExceptionHandlingBlock() ;
    	}
        super.visitCode();
    }    
	    
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
		if (isCallToSuperClassConstructor) {
			addStartOfDominantExceptionHandlingBlock() ;
			isCallToSuperClassConstructor = false;
		}
	}

    private void addStartOfDominantExceptionHandlingBlock() {
        // Insert start of try block label
        mark(start);    	
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
		addEndOfDominantExceptionHandlingBlock();
		super.visitMaxs(maxStack + 1, maxLocals + 1);
    }

    private void addEndOfDominantExceptionHandlingBlock() {
        // Insert end of try block label
        mark(end);
        // Skip catch block if no exception was thrown
        Label afterCatch = newLabel();
        goTo(afterCatch);
        mark(catchLabel);
        // reflect the exception.
        throwException();
        mark(afterCatch);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        TryCatchBlock block = new TryCatchBlock(start, end, handler, type);
        tryCatchBlockss.add(block);
        // super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
	public void visitEnd() {
        for (TryCatchBlock tryCatchBlock : tryCatchBlockss) {
            super.visitTryCatchBlock(tryCatchBlock.start,
                    tryCatchBlock.end, tryCatchBlock.handler,
                    tryCatchBlock.type);
        }
        super.visitTryCatchBlock(start, end, catchLabel, "java/lang/Throwable"); //settle body-enclosing TryCatchBlock as last TryCatchBlock
		super.visitEnd();
	}
	
}
