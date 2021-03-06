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

import adept.data.IValue;
import java.io.Serializable;


/**
 * The Class Value.
 */
public class Value implements IValue, Serializable {

	private static final long serialVersionUID = -2390288540653891267L;
	/** The value id. */
	private final long valueId;

	/**
	 * Instantiates a new value.
	 * 
	 * @param valueId
	 *            the value id
	 */
	public Value(long valueId) {
		super();
		this.valueId = valueId;
	}

	/**
	 * Gets the value id.
	 * 
	 * @return the value id
	 */
	public long getValueId() {
		return valueId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.IValue#getValue()
	 */
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
