/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.common;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;


/**
 * <p>Represents a TemporalSpan, or a span between a begin date and an end date.</p>
 * 
 * <p>This class is primarily intended to be used to set the valid temporal span
 * for Sentiments and Beliefs.</p>
 */

public final class TemporalSpan extends HltContent  {

	private final Optional<TemporalValue> beginDate;
	private final Optional<TemporalValue> endDate;
	
    
    private TemporalSpan(TemporalValue beginDate, TemporalValue endDate) {
    	this.beginDate = Optional.fromNullable(beginDate);
    	this.endDate = Optional.fromNullable(endDate);
    }

    public Optional<TemporalValue> getBeginDate(){
    	return beginDate;
    }
    
    public Optional<TemporalValue> getEndDate(){
    	return endDate;
    }
    
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
    	private TemporalValue beginDate;
    	private TemporalValue endDate;
    	
        private Builder() {
        }

        public Builder setBeginDate(TemporalValue beginDate){
        	this.beginDate = beginDate;
        	return this;
        }
        
        public Builder setEndDate(TemporalValue endDate){
        	this.endDate = endDate;
        	return this;
        }


        public TemporalSpan build() {
        	checkArgument(beginDate != null || endDate != null);
            return new TemporalSpan(beginDate, endDate);
        }
    }
    
}
