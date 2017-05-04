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

import adept.common.Sentence;
import adept.common.SyntacticChunk;


/**
 * The Class SyntacticChunker.
 */
public abstract class SyntacticChunker extends AbstractModule implements
		ISentenceProcessor<SyntacticChunk> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentenceProcessor#process(adept.common.Sentence)
	 */
	@Override
	public List<SyntacticChunk> process(Sentence sentence) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentenceProcessor#processAsync(adept.common.Sentence)
	 */
	@Override
	public long processAsync(Sentence sentence) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentenceProcessor#tryGetResult(long, java.util.List)
	 */
	@Override
	public Boolean tryGetResult(long requestId, List<SyntacticChunk> hltContents) {
		// TODO Auto-generated method stub
		return Boolean.FALSE;
	}

}
