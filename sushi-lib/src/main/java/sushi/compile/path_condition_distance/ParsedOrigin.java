package sushi.compile.path_condition_distance;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sushi.util.ReflectionUtils;

public class ParsedOrigin {
	
	private final String origin;
	private int nextUnparsed = 0;
	private String[] fields = null;
	private String parsedPrefix = "";

	private Set<String> dependedOrigins = new HashSet<>();

	private OriginAccessor[] originAccessSpecifier = null;

	public ParsedOrigin(String origin) {
		assert (origin != null && !origin.isEmpty()); 
		this.origin = origin;
		fields = origin.split("\\.");
		fields = rearrangeFieldsStringsWrtArrayAccesses(fields);
		
		originAccessSpecifier = new OriginAccessor[fields.length];
		
		String dependedOrigin = fields[0];
		dependedOrigins.add(dependedOrigin);
		for (int i = 1; i < fields.length; i++) {
			dependedOrigin += "." + fields[i];
			dependedOrigins.add(dependedOrigin);			
		}
	}
	
	private String[] rearrangeFieldsStringsWrtArrayAccesses(String[] fields) {
		List<String> fieldsRefined = new ArrayList<>();
		fieldsRefined.add(fields[0]); //fields[0] cannot be an array access specifier

		String arrayAccessSpecifier = "";
		int unmatched = 0;
		for (int i = 1; i < fields.length; i++) { 
			if (fields[i].charAt(0) == '[') {
				unmatched++;
			}
			if (unmatched > 0) {
				if (fields[i].charAt(0) != '[') {
					arrayAccessSpecifier += ".";
				}
				arrayAccessSpecifier += fields[i];
			} else {
				fieldsRefined.add(fields[i]);
			}
			if (fields[i].charAt(fields[i].length() - 1) == ']') {
				unmatched--;
				if (unmatched == 0) {
					fieldsRefined.add(arrayAccessSpecifier);
					arrayAccessSpecifier = "";
				}
			}
		}
		
		return fieldsRefined.toArray(new String[0]);
	}
	
	public Object get(Map<String, Object> candidateObjects, CandidateBackbone candidateBackbone, SushiLibCache cache) throws FieldDependsOnInvalidFieldPathException, FieldNotInCandidateException {
		/*SushiLibCache._I().attempts++;

		if (SushiLibCache._I().attempts > SushiLibCache._I().nextOutputAtAttempt) {
			SushiLibCache._I().nextOutputAtAttempt += 1000000;
			throw new RuntimeException(
					"Cache: " + SushiLibCache._I().hashCode() + ", cl: " + SushiLibCache._I().getClass().getClassLoader() +
					" attempts = " + SushiLibCache._I().attempts +
					" hits = " + SushiLibCache._I().hits +
					" phits = " + SushiLibCache._I().partialHits +
					" misses = " + SushiLibCache._I().misses +	
					" nextOutputAtAttempt = " + SushiLibCache._I().nextOutputAtAttempt 
					);
		}*/
		
		
		//1. Check if any dependedOrigin is invalid, throw exception to abort
		Set<String> smallerSet = candidateBackbone.getInvalidOrigins();
		Set<String> biggerSet = dependedOrigins;
		if (smallerSet.size() > biggerSet.size()) {
			smallerSet = dependedOrigins;
			biggerSet = candidateBackbone.getInvalidOrigins();
		}
		for (String s : smallerSet) {
			if (biggerSet.contains(s)) {
				throw new FieldDependsOnInvalidFieldPathException(s);
			}
		}
		
		/*if (nextUnparsed <= 0) {
			SushiLibCache._I().misses++;
		} else if (nextUnparsed >= fields.length) {
			SushiLibCache._I().hits++;			
		} else {
			SushiLibCache._I().partialHits++;
		}*/
		
		//2. retrieve the object for the already parsed fields
		Object obj = null;
		for (int i = 0; i < originAccessSpecifier.length; i++) {
			OriginAccessor accessor = originAccessSpecifier[i]; 
			if (accessor == null) { 
				if (i == 1) continue; //check also next accessor, because the second accessor can remain empty for origins that start with static fields
				else break;
			}
			obj = accessor.getActualObject(candidateObjects, obj, candidateBackbone, cache);
		}
		
		//3. complete parsing, is not yet done or done only partially
		while (nextUnparsed < fields.length) {
			
			if (nextUnparsed == 0) {
				boolean isStatic = fields[0].matches("\\[.*\\]");
				if (isStatic) {
					obj = parseAccessorStaticField();
					parsedPrefix = fields[0] + "." + fields[1];
					nextUnparsed = 2;		
				} else {
					obj = parseAccessorRootObject(candidateObjects);
					parsedPrefix = fields[0];
					nextUnparsed = 1;		
				}
			} else {
				if (obj == null) {
					throw new FieldNotInCandidateException();
				} else {
					if (obj.getClass().isArray()) {
						obj = parseAccessorArrayLocation(obj, candidateObjects, candidateBackbone, cache);  
					} else if (needsHack4StringJava6(obj)){
					obj = parseAccessorHack4StringJava6(obj);
					} else {
					obj = parseAccessorField(obj);
					}
					parsedPrefix = fields[nextUnparsed++];	
				}
			}
		}
		
		return obj;
	}

	private Object parseAccessorStaticField() {	
		try {
			String className = fields[0].substring(1, fields[0].length() - 1).replace('/', '.');
			final Field f = Class.forName(className).getDeclaredField(fields[1]);
			OriginAccessorStaticField accessor = new OriginAccessorStaticField(f);
			Object ret = accessor.getActualObject();
			originAccessSpecifier[0] = accessor;
			return ret;
		} catch (NoSuchFieldException | SecurityException | ClassNotFoundException e) {
			throw new SimilarityComputationException("Unexpected error while retrieving the value of static field from orgin " + origin + " : " + e );
		}

	}

	private Object parseAccessorRootObject(Map<String, Object> candidateObjects) {
		if (!candidateObjects.containsKey(fields[0])) {
			throw new SimilarityComputationException("Origin" + origin + " does not correspond to any root object in candidate");
		}
		OriginAccessorRootObject accessor = new OriginAccessorRootObject(fields[0]);
		Object ret = accessor.getActualObject(candidateObjects);
		originAccessSpecifier[0] = accessor;
		return ret;
	}

	private Object parseAccessorArrayLocation(Object obj, Map<String, Object> candidateObjects, CandidateBackbone candidateBackbone, SushiLibCache cache) 
			throws FieldNotInCandidateException, FieldDependsOnInvalidFieldPathException {
		OriginAccessorArrayLocation accessor = new OriginAccessorArrayLocation(fields[nextUnparsed]);
		Object ret = accessor.getActualObject(candidateObjects, obj, candidateBackbone, cache); 
		originAccessSpecifier[nextUnparsed] = accessor;
		return ret;
	}

	private boolean needsHack4StringJava6(Object obj) {
		return obj instanceof String && ("offset".equals(fields[nextUnparsed]) || "count".equals(fields[nextUnparsed]) || "<hashCode>".equals(fields[nextUnparsed]));
	}

	private Object parseAccessorHack4StringJava6(Object obj) throws FieldNotInCandidateException {
		OriginAccessorHack4StringJava6 accessor = new OriginAccessorHack4StringJava6(fields[nextUnparsed]);
		Object ret = accessor.getActualObject(obj);
		originAccessSpecifier[nextUnparsed] = accessor;
		return ret;
	}

	private Object parseAccessorField(Object obj) throws FieldNotInCandidateException {
		Field f = ReflectionUtils.getInheritedPrivateField(obj.getClass(), fields[nextUnparsed]);
		if (f == null) {
			throw new SimilarityComputationException("Field name " + fields[nextUnparsed] + " of origin " + origin + " does not exist in the corresponding object");
		}
		OriginAccessorField accessor = new OriginAccessorField(f);
		Object ret = accessor.getActualObject(obj);
		originAccessSpecifier[nextUnparsed] = accessor;
		return ret;
	}

	private abstract class OriginAccessor {
		abstract Object getActualObject(Map<String, Object> candidateObjects, Object obj, CandidateBackbone candidateBackbone, SushiLibCache cache) 
				throws FieldNotInCandidateException, FieldDependsOnInvalidFieldPathException;
	}
	
	private class OriginAccessorStaticField extends OriginAccessor {
		final Field field;
		OriginAccessorStaticField(Field field) {
			this.field = field;
		}

		@Override
		Object getActualObject(Map<String, Object> candidateObjects, Object obj, CandidateBackbone candidateBackbone, SushiLibCache cache) {
			return getActualObject();
		}

		Object getActualObject() {
			field.setAccessible(true);
			try {
				return field.get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new SimilarityComputationException("Unexpected error while retrieving the value of a static field: " + field);
			}
		}
	}

	private class OriginAccessorRootObject extends OriginAccessor {
		final String rootObjIdentifier;
		OriginAccessorRootObject(String rootObjIdentifier) {
			this.rootObjIdentifier = rootObjIdentifier;
		}

		@Override
		Object getActualObject(Map<String, Object> candidateObjects, Object obj, CandidateBackbone candidateBackbone, SushiLibCache cache) {
			return getActualObject(candidateObjects);
		}

		Object getActualObject(Map<String, Object> candidateObjects) {
			return candidateObjects.get(rootObjIdentifier);
		}
}
	
	private class OriginAccessorField extends OriginAccessor {
		private final Field field;
		OriginAccessorField(Field field) {
			this.field = field;
		}

		@Override
		Object getActualObject(Map<String, Object> candidateObjects, Object obj, CandidateBackbone candidateBackbone, SushiLibCache cache) 
				throws FieldNotInCandidateException {
			return getActualObject(obj);
		}

		private Object getActualObject(Object obj) throws FieldNotInCandidateException {
			if (obj == null) {
				throw new FieldNotInCandidateException();
			}	
			try {
				field.setAccessible(true);
				return field.get(obj);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new FieldNotInCandidateException();
				//throw new SimilarityComputationException("Unexpected error while retrieving the value of member field: " + field + ", from object of class " + obj.getClass());
			}
		}
	}

	private class OriginAccessorArrayLocation extends OriginAccessor {
		private final String arrayItemSpecifier;	
		private final boolean isAccessToFieldLength;	
		private Integer arrayIndex;	
		private String arrayIndexExpression;	
		private String[] arrayIndexDependedOrgins;	

		OriginAccessorArrayLocation(String arrayItemSpecifier) {
			this.arrayItemSpecifier = arrayItemSpecifier;
			
			if (arrayItemSpecifier.equals("length")) {
				isAccessToFieldLength = true;
				arrayIndex = null;
				arrayIndexExpression = null;
				arrayIndexDependedOrgins = null;
			} else if (arrayItemSpecifier.matches("\\[.*\\]")) {
				isAccessToFieldLength = false;
				String indexString = arrayItemSpecifier.substring(1, arrayItemSpecifier.length() - 1);
				
				try {
					arrayIndex = Integer.parseInt(indexString);
					arrayIndexExpression = null;
					arrayIndexDependedOrgins = null;
				} catch (NumberFormatException e) {
					//throw new RuntimeException("Not implemented yet");
					//TODO: What follows is just an hack for the very specific case that the indexString is in the form "{somevar}+1+1+1..."
					//TODO: Fix to support evaluation of arbitrary expressions
					
					if (indexString.indexOf('{') >= 0) {
						int startOrigin = indexString.indexOf('{');
						
						int endOrigin = indexString.indexOf(')', startOrigin);
						if (endOrigin < 0) {
							endOrigin = indexString.length();
						}
						
						String origin = indexString.substring(startOrigin, endOrigin);
						arrayIndexDependedOrgins = new String[]{origin};
					}
					
					int last = 0;
					int index = 0;
					while (indexString.indexOf('1', last) >= 0) {
						index += 1;
						last = indexString.indexOf('1', last) + 1;
					}
					arrayIndexExpression = "" + index;
					
					/*ScriptEngineManager mgr = new ScriptEngineManager();
				    ScriptEngine engine = mgr.getEngineByName("JavaScript");
				    Object ev;
				    try {
				    	ev = engine.eval(indexString);
					} catch (ScriptException e) {
						throw new SimilarityComputationException("Cannot evaluate an array index out of expression " + indexString + " for " + fieldSpec);
					}
				    if (ev instanceof Integer) {
						index = (Integer) ev;
					} else {
						throw new SimilarityComputationException("Unexpected type (" + ev.getClass() +") while retrieving the value of an array index: " + indexString + "." + fieldSpec);
					}*/
				}
			    
			} else {
				throw new SimilarityComputationException("Unexpected field or indexSpec in array object: " +  arrayItemSpecifier);					
			}		
		}

		@Override
		Object getActualObject(Map<String, Object> candidateObjects, Object obj, CandidateBackbone candidateBackbone, SushiLibCache cache) throws FieldNotInCandidateException, FieldDependsOnInvalidFieldPathException {
			if (obj == null) {
				throw new FieldNotInCandidateException();
			}	
			return retrieveFromArray(obj, candidateBackbone, candidateObjects, cache);
		}	
		
		private Object retrieveFromArray(Object obj, CandidateBackbone candidateBackbone, Map<String, Object> candidateObjects, SushiLibCache cache) 
				throws FieldNotInCandidateException, FieldDependsOnInvalidFieldPathException {
			if (isAccessToFieldLength) {
				return Array.getLength(obj);
			}
			else if (arrayIndex != null) {					
				try {
			    		return Array.get(obj, arrayIndex);
			    } catch (ArrayIndexOutOfBoundsException e) {
			    		throw new FieldNotInCandidateException();
			    }  
			} else if (arrayIndexExpression != null) {
				//TODO: generalize this
				int index = Integer.parseInt(arrayIndexExpression);
				if (arrayIndexDependedOrgins[0] != null) {
					index += (Integer) candidateBackbone.retrieveOrVisitField(arrayIndexDependedOrgins[0], candidateObjects, cache);
				} 
				try {
		    			return Array.get(obj, index);
				} catch (ArrayIndexOutOfBoundsException e) {
		    			throw new FieldNotInCandidateException();
				}	 	
			} else {
				throw new SimilarityComputationException("Unexpected field or indexSpec in array object: " +  arrayItemSpecifier);					
			}
		}
	}

	private class OriginAccessorHack4StringJava6 extends OriginAccessor {
		private final String fieldName;	
		OriginAccessorHack4StringJava6(String fieldName) {
			this.fieldName = fieldName;
		}

		@Override
		Object getActualObject(Map<String, Object> candidateObjects, Object obj, CandidateBackbone candidateBackbone, SushiLibCache cache) 
				throws FieldNotInCandidateException {
			return getActualObject(obj);
		}
		
		Object getActualObject(Object obj) throws FieldNotInCandidateException {
			if (obj == null) {
				throw new FieldNotInCandidateException();
			}	
			return hack4StringJava6(obj, fieldName);
		}

		private Object hack4StringJava6(Object obj, String fname) {
			if (obj instanceof String) {
				if ("offset".equals(fname)) {
					return 0;
				} else if ("count".equals(fname)) {
					try {
						return String.class.getMethod("length", (Class<?>[]) null).invoke(obj, (Object[]) null);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e) {
						throw new RuntimeException(e);
					}
				} else if ("<hashCode>".equals(fname)) {
					try {
						return String.class.getMethod("hashCode", (Class<?>[]) null).invoke(obj, (Object[]) null);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
							| NoSuchMethodException | SecurityException e) {
						throw new RuntimeException(e);
					}
				}
			}
			throw new SimilarityComputationException("cannot handle hack4StringJava6 for object " + obj.getClass() + "." + fname);
		}
	}
		
}
