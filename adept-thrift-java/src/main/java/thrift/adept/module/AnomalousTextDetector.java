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
import thrift.adept.common.Document;
import thrift.adept.common.HltContentContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class AnomalousTextDetector.
 */
public class AnomalousTextDetector extends AbstractModule implements
		DocumentHltContentProcessor.Iface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentHltContentProcessor#process(adept.common.Document,
	 * adept.common.HltContentContainer)
	 */
	@Override
	public List<HltContentUnion> process(Document document,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentHltContentProcessor#processAsync(adept.common.Document
	 * , adept.common.HltContentContainer)
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
	 * @see adept.module.IDocumentHltContentProcessor#tryGetResult(long,
	 * java.util.List)
	 */
	@Override
	public boolean tryGetResult(long requestId, List<HltContentUnion> hltContents) {
		// TODO Auto-generated method stub
		return false;
	}

}