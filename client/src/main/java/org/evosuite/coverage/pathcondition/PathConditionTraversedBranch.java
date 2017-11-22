package org.evosuite.coverage.pathcondition;

import org.evosuite.coverage.branch.BranchCoverageGoal;

public abstract class PathConditionTraversedBranch {
	public final String className;
	//public final String methodDescriptor;
	public final String methodName;
	private final int hash;
	
	PathConditionTraversedBranch(String className, /*String methodDescriptor,*/ String methodName) {
		if (className == null || /*methodDescriptor == null ||*/ methodName == null) {
			throw new NullPointerException();
		}
		this.className = className;
		//this.methodDescriptor = methodDescriptor;
		this.methodName = methodName;
		final int prime = 31;
		int result = 1;
		result = prime * result + this.className.hashCode();
		//result = prime * result + this.methodDescriptor.hashCode();
		result = prime * result + this.methodName.hashCode();
		this.hash = result;
	}
	
	@Override
	public int hashCode() {
		return this.hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PathConditionTraversedBranch other = (PathConditionTraversedBranch) obj;
		if (!this.className.equals(other.className)) {
			return false;
		}
		/*if (!this.methodDescriptor.equals(other.methodDescriptor)) {
			return false;
		}*/
		if (!this.methodName.equals(other.methodName)) {
			return false;
		}
		return true;
	}
	
	public static PathConditionTraversedBranch makeMethodEntryPoint(String className, String methodDescriptor, String methodName) {
		return new MethodEntryPoint(className, /* methodDescriptor,*/ methodName);
	}

	public static PathConditionTraversedBranch makeRealBranch(String className, String methodDescriptor, String methodName, int bytecodeFrom, int bytecodeTo) {
		return new RealBranch(className, /* methodDescriptor,*/ methodName, bytecodeFrom, bytecodeTo);
	}

	static class MethodEntryPoint extends PathConditionTraversedBranch {
		MethodEntryPoint(String className, /* String methodDescriptor,*/ String methodName) {
			super(className, /* methodDescriptor,*/ methodName);
		}
	}
	

	static class RealBranch extends PathConditionTraversedBranch {
		public final int bytecodeFrom;
		public final int bytecodeTo;
		private final int hash;
		
		RealBranch(String className, /* String methodDescriptor,*/ String methodName, int bytecodeFrom, int bytecodeTo) {
			super(className, /*methodDescriptor,*/ methodName);
			this.bytecodeFrom = bytecodeFrom;
			this.bytecodeTo = bytecodeTo;
			final int prime = 59;
			int result = super.hashCode();
			result = prime * result + this.bytecodeFrom;
			result = prime * result + this.bytecodeTo;
			this.hash = result;
		}
		
		@Override
		public int hashCode() {
			return this.hash;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!super.equals(obj)) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final RealBranch other = (RealBranch) obj;
			if (this.bytecodeFrom != other.bytecodeFrom) {
				return false;
			}
			if (this.bytecodeTo != other.bytecodeTo) {
				return false;
			}
			return true;
		}
	}


	public static PathConditionTraversedBranch convertEvoSuiteBranchGoal(BranchCoverageGoal b) {
		if (b.getBranch() == null) {
			return new MethodEntryPoint(b.getClassName(), b.getMethodName());
		} else {
			return new RealBranch(b.getClassName(), b.getMethodName(), b.getBranch().getInstruction().getBytecodeOffset(), 0);
		}
	}
	
}