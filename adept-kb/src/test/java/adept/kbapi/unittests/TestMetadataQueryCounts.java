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

package adept.kbapi.unittests;

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


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;






import org.junit.Assert;
import org.junit.Test;

import adept.kbapi.KBEntity;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBUpdateException;
  
/**
 * 
 * @author dkolas
 */
public class TestMetadataQueryCounts extends KBUnitTest{

	@Test
	public void testEntityQueries() throws InvalidPropertiesFormatException, FileNotFoundException, KBUpdateException, IOException, KBQueryException {
		KBEntity entity = insertDefaultTestEntity();
		
		int beforeCount = kb.getSQLConnectionStatistics().getQueries();
		
		KBEntity queriedEntity = kb.getEntityById(entity.getKBID());
		
		int middleCount = kb.getSQLConnectionStatistics().getQueries();
		
		Assert.assertEquals("Fetching an entity by id should not have caused a query", beforeCount, middleCount);
		
		queriedEntity.getProvenances();
		
		int afterCount = kb.getSQLConnectionStatistics().getQueries();
		
		Assert.assertEquals("Fetching provenances should have caused a query", middleCount +1 , afterCount);
	}
	
	@Test
	public void testRelationQueries() throws InvalidPropertiesFormatException, FileNotFoundException, KBUpdateException, IOException, KBQueryException {
		
		KBRelation relation = insertDefaultTestRelation(insertDefaultTestEntity(), insertDefaultTestEntity());
		
		int beforeCount = kb.getSQLConnectionStatistics().getQueries();
		
		KBRelation queriedRelation = kb.getRelationById(relation.getKBID());
		
		int middleCount = kb.getSQLConnectionStatistics().getQueries();
		
		Assert.assertEquals("Fetching an entity by id should not have caused a query", beforeCount, middleCount);
		
		queriedRelation.getProvenances();
		
		int afterCount = kb.getSQLConnectionStatistics().getQueries();
		
		Assert.assertEquals("Fetching provenances should have caused a query", middleCount +1 , afterCount);
	}
	
}