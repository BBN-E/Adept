package adept.module;

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

import java.util.List;

import adept.common.Chunk;
import adept.common.Paraphrase;


/**
 * The Class Paraphraser.
 */
public abstract class Paraphraser implements IChunkProcessor<Paraphrase> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IChunkProcessor#process(adept.common.Chunk)
	 */
	@Override
	public List<Paraphrase> process(Chunk chunk) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IChunkProcessor#processAsync(adept.common.Chunk)
	 */
	@Override
	public long processAsync(Chunk chunk) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IChunkProcessor#tryGetResult(long, java.util.List)
	 */
	@Override
	public Boolean tryGetResult(long requestId, List<Paraphrase> hltContents) {
		// TODO Auto-generated method stub
		return Boolean.FALSE;
	}

}
