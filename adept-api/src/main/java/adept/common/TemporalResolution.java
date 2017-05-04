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


import adept.metadata.SourceAlgorithm;
import java.io.Serializable;
  
/**
 * 
 * @author dkolas
 */
public class TemporalResolution implements Serializable {

	private static final long serialVersionUID = -2334048345179864265L;
	private final TemporalValue resolvedTemporalValue;
	private final TimePhrase timePhrase;
	
	

	private final float confidence;
	private SourceAlgorithm sourceAlgorithm;
	
	private TemporalResolution(TemporalValue temporalValue, TimePhrase timePhrase, float confidence, SourceAlgorithm sourceAlgorithm){
		this.resolvedTemporalValue = temporalValue;
		this.timePhrase = timePhrase;
		this.confidence = confidence;
		this.sourceAlgorithm = sourceAlgorithm;
	}
	
	
	public static Builder builder(){
		return new Builder();
	}
	
	public static class Builder {
		private TemporalValue resolvedTemporalValue;
		private TimePhrase timePhrase;
		private float confidence = 0f;
		private SourceAlgorithm sourceAlgorithm;
		
		private Builder(){}
		
		public Builder setResolvedTemporalValue(TemporalValue temporalValue){
			this.resolvedTemporalValue = temporalValue;
			return this;
		}
		
		public Builder setTimePhrase(TimePhrase timePhrase){
			this.timePhrase = timePhrase;
			return this;
		}
		
		public Builder setConfidence(float confidence){
			this.confidence = confidence;
			return this;
		}
		
		public Builder setSourceAlgorithm(SourceAlgorithm sourceAlgorithm){
			this.sourceAlgorithm = sourceAlgorithm;
			return this;
		}
		
		public TemporalResolution build(){
			return new TemporalResolution(resolvedTemporalValue, timePhrase, confidence, sourceAlgorithm);
		}
	}
	
	/**
	 * @return the resolvedTemporalValue
	 */
	public TemporalValue getResolvedTemporalValue() {
		return resolvedTemporalValue;
	}

	/**
	 * @return the timePhrase
	 */
	public TimePhrase getTimePhrase() {
		return timePhrase;
	}

	/**
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * @return the sourceAlgorithm
	 */
	public SourceAlgorithm getSourceAlgorithm() {
		return sourceAlgorithm;
	}
}
