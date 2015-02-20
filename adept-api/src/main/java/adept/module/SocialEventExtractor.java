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
import adept.common.*;


// TODO: Auto-generated Javadoc
/**
 * The Class SocialEventExtractor.
 */
public abstract class SocialEventExtractor extends AbstractModule implements
		IDocumentHltContentProcessor<Relation> {

	/* (non-Javadoc)
	 * @see adept.module.IDocumentHltContentProcessor#process(adept.common.Document, adept.common.HltContentContainer)
	 */
	@Override
	public List<Relation> process(Document document,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see adept.module.IDocumentHltContentProcessor#processAsync(adept.common.Document, adept.common.HltContentContainer)
	 */
	@Override
	public long processAsync(Document document,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see adept.module.IDocumentHltContentProcessor#tryGetResult(long, java.util.List)
	 */
	@Override
	public Boolean tryGetResult(long requestId, List<Relation> hltContents)
			throws AdeptModuleException {
		// TODO Auto-generated method stub
		return null;
	}


}