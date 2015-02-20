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

import thrift.adept.common.Document;
import thrift.adept.common.DocumentList;
import thrift.adept.common.HltContentContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class CoreferenceResolver.
 */
public class CoreferenceResolver extends AbstractModule implements
		DocumentProcessor.Iface, DocumentListProcessor.Iface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentListProcessor#process(adept.common.DocumentList,
	 * adept.common.HltContentContainer)
	 */
	@Override
	public HltContentContainer process(DocumentList documentList,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentListProcessor#processAsync(adept.common.DocumentList
	 * , adept.common.HltContentContainer)
	 */
	@Override
	public long processAsync(DocumentList documentList,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IDocumentProcessor#process(adept.common.TextDocument,
	 * adept.common.HltContentContainer)
	 */
	@Override
	public HltContentContainer process(Document document,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentProcessor#processAsync(adept.common.TextDocument,
	 * adept.common.HltContentContainer)
	 */
	@Override
	public long processAsync(Document document,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IDocumentProcessor#tryGetResult(long,
	 * adept.common.HltContentContainer)
	 */
	@Override
	public boolean tryGetResult(long requestId,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return false;
	}

}