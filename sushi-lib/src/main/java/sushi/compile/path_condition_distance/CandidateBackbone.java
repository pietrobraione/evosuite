package sushi.compile.path_condition_distance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class CandidateBackbone {
	// We keep the direct and reverse mapping between visited objects and their origins 
	private final Map<ObjectMapWrapper, String> visitedObjects = new HashMap<ObjectMapWrapper, String>(); 
	private final Map<String, Object> visitedOrigins = new HashMap<String, Object>(); 

	private final Set<String> invalidOrigins = new HashSet<String>(); 
	
	private void storeInBackbone(Object obj, String origin) {
		// If another origin already exist, this is an alias path
		// and then it shall not be stored
		if (!visitedObjects.containsKey(new ObjectMapWrapper(obj))) {
			visitedOrigins.put(origin, obj);		
			visitedObjects.put(new ObjectMapWrapper(obj), origin);
		}
	}

	public Object getVisitedObject(String origin) {
		return visitedOrigins.get(origin);
	}

	public String getOrigin(Object obj) {
		return visitedObjects.get(new ObjectMapWrapper(obj));
	}

	public void addInvalidOrgin(String origin) {
		invalidOrigins.add(origin);
	}

	public Set<String> getInvalidOrigins() {
		return invalidOrigins;
	}

	private static final class ObjectMapWrapper {
		private Object o;
		ObjectMapWrapper(Object o) { this.o = o; }
	
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof ObjectMapWrapper)) {
				return false;
			}
			final ObjectMapWrapper omw = (ObjectMapWrapper) obj;
			return (this.o == omw.o);
		}
	
		@Override
		public int hashCode() {
			return System.identityHashCode(this.o);
		}
	}

	public Object retrieveOrVisitField(String origin, Map<String, Object> candidateObjects, SushiLibCache cache) 
			throws FieldNotInCandidateException, FieldDependsOnInvalidFieldPathException {
		assert (origin != null); 
		
		if (cache == null) {
			cache = new SushiLibCache(); //no-cache behavior: a new cache every time
		}
		
		// We check whether this entire origin corresponds to an already visited object
		Object obj = getVisitedObject(origin);
		if (obj != null) {
			return obj;
		}
		
		ParsedOrigin parsedOrigin = cache.getParsedOrigin(origin);
		
		obj = parsedOrigin.get(candidateObjects, this, cache);
		
		storeInBackbone(obj, origin); //TODO: incremental storing
		
		return obj;
	}

}
