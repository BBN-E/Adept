/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
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
* -------
*/

package adept.common;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Translation.
 */
public class Translation {

	/** The source chunk. */
	private final Chunk sourceChunk;

	/** The target chunk. */
	private final Chunk targetChunk;

	/**
	 * Instantiates a new translation.
	 * 
	 * @param sourceChunk
	 *            the source chunk
	 * @param targetChunk
	 *            the target chunk
	 */
	public Translation(Chunk sourceChunk, Chunk targetChunk) {
		super();

                checkArgument(sourceChunk!=null);
                checkArgument(targetChunk!=null);

		this.sourceChunk = sourceChunk;
		this.targetChunk = targetChunk;
	}

	/**
	 * Gets the source chunk.
	 * 
	 * @return the source chunk
	 */
	public Chunk getSourceChunk() {
		return sourceChunk;
	}

	/**
	 * Gets the target chunk.
	 * 
	 * @return the target chunk
	 */
	public Chunk getTargetChunk() {
		return targetChunk;
	}
}