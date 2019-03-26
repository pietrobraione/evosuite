package sushi.compile.path_condition_distance;

import java.util.Map;

import sushi.logging.Logger;

public class SimilarityWithRefNotAlias implements ClauseSimilarityHandler {
	private static final Logger logger = new Logger(SimilarityWithRefNotAlias.class);

	protected final String theReferenceOrigin;
	private final String theAliasOrigin;
	
	public SimilarityWithRefNotAlias(String theReferenceOrigin, String theAliasOrigin) {
		if (theReferenceOrigin == null) {
			throw new SimilarityComputationException("Origin cannot be null");
		}
		this.theReferenceOrigin = theReferenceOrigin;
		if (theAliasOrigin == null) {
			throw new SimilarityComputationException("Alias origin cannot be null");
		}
		this.theAliasOrigin = theAliasOrigin;
	}

	@Override
	public double evaluateSimilarity(CandidateBackbone backbone, Map<String, Object> candidateObjects, SushiLibCache cache) {
		//logger.debug("Handling similarity with field reference " + theReferenceOrigin);
		
		double similarity = 0.0d;
		try {
			Object referredObj = backbone.retrieveOrVisitField(theReferenceOrigin, candidateObjects, cache);

			similarity = evaluateSimilarity(backbone, referredObj);
			
			if(similarity != 1.0d) {
				backbone.addInvalidOrgin(theReferenceOrigin);
			}			
		} catch (FieldNotInCandidateException e) {
			//logger.debug("Field " + theReferenceOrigin + " does not yet exist in candidate");			
		} catch (FieldDependsOnInvalidFieldPathException e) {
			//logger.debug("Field " + theReferenceOrigin + " depends on field path that did not converge yet: " + e.getMessage());			
		}
		return similarity;
	}

	protected double evaluateSimilarity(CandidateBackbone backbone, Object referredObject) {
		//logger.debug("Ref that do not alias another ref");
		
		double similarity;
		
		Object alias = backbone.getVisitedObject(theAliasOrigin);

		if (referredObject == alias) {
			//logger.debug("Matching aliases between field " + theReferenceOrigin + " and field " + theAliasOrigin);
			similarity = 0.0d;
		}
		else {
			similarity = 1.0d;
			//logger.debug("Non matching aliases: field " + theReferenceOrigin + " is null rather than alias of " + theAliasOrigin);
		}

		//logger.debug("Similarity increases by: " + similarity);
		return similarity;
	}

}
