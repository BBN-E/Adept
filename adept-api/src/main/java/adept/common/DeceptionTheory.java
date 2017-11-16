package adept.common;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.common.base.Preconditions;
import java.io.Serializable;


/**
 * The Class DeceptionTheory.
 * 
 * This class stores the relationship between a Chunk and a judgment
 * of whether it is deceptive.   
 * 
 */
public final class DeceptionTheory extends HltContent implements Serializable {

	private static final long serialVersionUID = -7948854314600402007L;

	/** The confidence. */
	private final float confidence;

	/** The document. */
	private final Chunk chunk;

	private final boolean deceptive;

	/**
	 * Instantiates a new Deception Theory
	 * 
	 * @param chunk the chunk this DeceptionTheory will be based on
	 * @param confidence the confidence level of this relationship
	 * @param deceptive whether or not this relationship is deceptive.
	 * 
	 */
	public DeceptionTheory(Chunk chunk, float confidence, boolean deceptive) {
		super();
		
		Preconditions.checkNotNull(chunk);
        this.confidence = confidence;
        this.deceptive = deceptive;
        this.chunk = chunk;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * @return the chunk
	 */
	public Chunk getChunk() {
		return chunk;
	}

	/**
	 * @return whether or not this chunk is deceptive
	 */
	public boolean isDeceptive() {
		return deceptive;
	}

	

}
