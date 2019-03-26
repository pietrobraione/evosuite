package sushi.compile.path_condition_distance;

import java.util.HashMap;
import java.util.Map;

public class SushiLibCache {
	
	private Map<String, ParsedOrigin> parsedOrigins = new HashMap<>();
	
	int attempts = 0;
	int partialHits = 0;
	int hits = 0;
	int misses = 0;
	int nextOutputAtAttempt = 100;
	
	public SushiLibCache() {
	}
	
	public ParsedOrigin getParsedOrigin(String origin)  {
		ParsedOrigin cachedOrigin = parsedOrigins.get(origin);
		if (cachedOrigin == null) {
			cachedOrigin = new ParsedOrigin(origin);
			parsedOrigins.put(origin, cachedOrigin);
		}
		return cachedOrigin;
	}

}
