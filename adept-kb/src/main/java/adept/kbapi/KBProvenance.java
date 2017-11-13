/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

package adept.kbapi;

/*-
 * #%L
 * adept-kb
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

import adept.common.KBID;

/**
 *
 * @author dkolas
 */
public abstract class KBProvenance {
	private KBID kbid;

	public KBProvenance(KBID kbid) {
		this.kbid = kbid;
	}

	public KBID getKBID() {
		return kbid;
	}

	public static abstract class InsertionBuilder extends KBObjectBuilder {
		protected abstract KBProvenance build();
		protected abstract String getValue();
	}

  	public static abstract class UpdateBuilder extends KBObjectBuilder {
    		protected abstract KBProvenance build();
  	}

	public abstract InsertionBuilder modifiedCopyInsertionBuilder();


	@Override
	public boolean equals(Object o) {
		return (o instanceof KBProvenance) && ((KBProvenance) o).kbid.equals(this.kbid);
	}

	@Override
	public int hashCode() {
		return kbid.hashCode();
	}

	public abstract String getValue();
}