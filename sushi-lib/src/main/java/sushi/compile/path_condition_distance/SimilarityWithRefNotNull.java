package sushi.compile.path_condition_distance;

import java.util.Map;

import sushi.logging.Logger;

public class SimilarityWithRefNotNull implements ClauseSimilarityHandler {
	private static final Logger logger = new Logger(SimilarityWithRefNotNull.class);
	
	protected final String theReferenceOrigin;

	public SimilarityWithRefNotNull(String theReferenceOrigin) {
		if (theReferenceOrigin == null) {
			throw new SimilarityComputationException("Origin cannot be null");
		}
		this.theReferenceOrigin = theReferenceOrigin;
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
		//logger.debug("Null reference");
		
		double similarity = 0.0d;
		
		if (referredObject != null) {
			//logger.debug("Field " + theReferenceOrigin + " is null also in candidate");
			similarity += 1.0d;	
		}
		else {
			//logger.debug("Field " + theReferenceOrigin + " is not null in candidate");
		}
		
		//logger.debug("Similarity increases by: " + similarity);
		return similarity;
	}

}
