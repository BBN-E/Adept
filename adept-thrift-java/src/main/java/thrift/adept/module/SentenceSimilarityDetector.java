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

package thrift.adept.module;

import java.util.List;

import thrift.adept.common.HltContentUnion;
import thrift.adept.common.Sentence;

// TODO: Auto-generated Javadoc
/**
 * The Class SentenceSimilarityDetector.
 */
public class SentenceSimilarityDetector extends AbstractModule implements
		SentencePairProcessor.Iface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentencePairProcessor#process(adept.common.Sentence,
	 * adept.common.Sentence)
	 */
	@Override
	public List<HltContentUnion> process(Sentence sentence1,
			Sentence sentence2) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.ISentencePairProcessor#processAsync(adept.common.Sentence,
	 * adept.common.Sentence)
	 */
	@Override
	public long processAsync(Sentence sentence1, Sentence sentence2) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentencePairProcessor#tryGetResult(long,
	 * java.util.List)
	 */
	@Override
	public boolean tryGetResult(long requestId,
			List<HltContentUnion> hltContents) {
		// TODO Auto-generated method stub
		return false;
	}

}