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

package adept.module;

import java.util.List;

import adept.common.Entailment;
import adept.common.Passage;

// TODO: Auto-generated Javadoc
/**
 * The Class TextualEntailmentDetector.
 */
public abstract class TextualEntailmentDetector extends AbstractModule implements
		IPassagePairProcessor<Entailment> {

	/**
	 * Instantiates a new textual entailment detector.
	 */
	public TextualEntailmentDetector() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IPassagePairProcessor#process(adept.common.Passage,
	 * adept.common.Passage)
	 */
	@Override
	public List<Entailment> process(Passage text, Passage hypothesis) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IPassagePairProcessor#processAsync(adept.common.Passage,
	 * adept.common.Passage)
	 */
	@Override
	public long processAsync(Passage text, Passage hypothesis) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IPassagePairProcessor#tryGetResult(long,
	 * java.util.List)
	 */
	@Override
	public Boolean tryGetResult(long requestId, List<Entailment> entailments) {
		// TODO Auto-generated method stub
		return null;
	}

}