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

package adept.utilities;

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

import java.io.IOException;
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;

import adept.common.OntType;
import adept.kbapi.KB;
import adept.kbapi.KBConfigurationException;
import adept.kbapi.KBEntity;
import adept.kbapi.KBEntityMentionProvenance;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBParameters;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBUpdateException;

public class ConfirmRemoteTripleStoreInsertQueryDelete {
	public static void main(String[] args) throws KBConfigurationException, InvalidPropertiesFormatException, IOException, KBUpdateException, KBQueryException {
		KBParameters kbParameters = new KBParameters();

		KB kb = new KB(kbParameters);
		KBEntityMentionProvenance.InsertionBuilder canonicalMention = new
				KBEntityMentionProvenance.InsertionBuilder();
		canonicalMention.setValue("Test");
		canonicalMention.setType("NOMINAL");
		KBEntity.InsertionBuilder entityBuilder = KBEntity.entityInsertionBuilder(
				Collections.singletonMap(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Person"), .3f),
				canonicalMention,
				.3f,
				.3f);

		KBEntity insertedEntity = entityBuilder.insert(kb);

		KBEntity queriedEntity = kb.getEntityById(insertedEntity.getKBID());

		System.out.println("Inserted and queried entities are equal: "+insertedEntity.equals(queriedEntity));

		kb.deleteKBObject(insertedEntity.getKBID());

		try{
			KBEntity postDeleteEntity = kb.getEntityById(insertedEntity.getKBID());
			System.out.println("Error: Entity still existed after delete: "+postDeleteEntity.getKBID());
		}catch(Exception e){
			System.out.println("Entity deleted successfully.");
		}


		kb.close();
	}
}