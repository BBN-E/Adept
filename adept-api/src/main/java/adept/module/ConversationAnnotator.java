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

import adept.common.Document;
import adept.common.HltContentContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class ConversationAnnotator.
 */
public abstract class ConversationAnnotator extends AbstractModule implements
		IDocumentProcessor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IConversationProcessor#process(adept.common.Conversation)
	 */
	@Override
	public HltContentContainer process(Document document, HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IConversationProcessor#processAsync(adept.common.Conversation
	 * )
	 */
	@Override
	public long processAsync(Document document, HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IConversationProcessor#tryGetResult(long,
	 * adept.common.Conversation)
	 */
	@Override
	public Boolean tryGetResult(long requestId, HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return null;
	}

}