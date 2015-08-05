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

package adept.mappers.handlers;

import thrift.adept.common.*;


/**
 * The Class Translation.
 */
public class TranslationHandler implements TranslationService.Iface {

	private Translation myTranslation;

	/**
	 * Instantiates a new translation.
	 * 
	 * @param sourceChunk
	 *            the source chunk
	 * @param targetChunk
	 *            the target chunk
	 */
	public TranslationHandler(ChunkUnion sourceChunk, ChunkUnion targetChunk) {
		super();
		myTranslation = new Translation();
		myTranslation.sourceChunk = sourceChunk;
		myTranslation.targetChunk = targetChunk;
	}

	/**
	 * Gets the source chunk.
	 * 
	 * @return myTranslation.the source chunk
	 */
	public ChunkUnion getSourceChunk() {
		return myTranslation.sourceChunk;
	}

	/**
	 * Gets the target chunk.
	 * 
	 * @return myTranslation.the target chunk
	 */
	public ChunkUnion getTargetChunk() {
		return myTranslation.targetChunk;
	}
	public Translation getTranslation() {
		return myTranslation;
	}

}