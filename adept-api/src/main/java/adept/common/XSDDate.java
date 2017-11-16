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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import java.io.Serializable;

/**
 * 
 * @author dkolas
 */
public class XSDDate extends Item implements TemporalValue, Serializable {

	private static final long serialVersionUID = 2864695510651066484L;
	private final String xsdDateString;

	private XSDDate(String xsdDateString) {
		this.xsdDateString = checkNotNull(xsdDateString);
		checkArgument(!xsdDateString.isEmpty(), "XSD Date strings may not be empty");
	}

	@Override
	public String asString() {
		return xsdDateString;
	}
	
	/**
	 * @param xsdDateString
	 *            Must be a valid XSD Date string. Otherwise, behavior is
	 *            undefined.
	 */
	public static XSDDate fromString(String xsdDateString) {
		return new XSDDate(xsdDateString);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(xsdDateString);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		final XSDDate other = (XSDDate) obj;
		return Objects.equal(this.xsdDateString, other.xsdDateString);
	}

}
