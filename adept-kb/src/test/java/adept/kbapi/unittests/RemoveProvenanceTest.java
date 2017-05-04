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


import org.junit.Assert;
import org.junit.Test;

import adept.kbapi.KBNumber;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBProvenance;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;
  
/**
 * 
 * @author dkolas
 */
public class RemoveProvenanceTest extends KBUnitTest {

	
	@Test
	public void removeProvenance() throws KBUpdateException, KBQueryException {
		KBNumber.InsertionBuilder builder = KBNumber.numberInsertionBuilder(3);
		builder.addProvenance(generateProvenance("1"));
		builder.addProvenance(generateProvenance("2"));
		builder.addProvenance(generateProvenance("3"));
		KBNumber number = builder.insert(kb);
		
		Assert.assertEquals("Wrong number of initial provenances.",3, number.getProvenances().size());
		
		KBNumber.UpdateBuilder updater = number.updateBuilder();
		
		updater.removeProvenance(getProvenance(number, "2"));
		
		KBNumber updatedNumber = updater.update(kb);
		
		Assert.assertEquals("Wrong number of updated provenances.", 2, updatedNumber.getProvenances().size());
		
	}
	
	private KBProvenance getProvenance(KBPredicateArgument kbObject, String value) throws KBQueryException{
		for (KBProvenance provenance : kbObject.getProvenances()){
			KBTextProvenance textProvenance = (KBTextProvenance)provenance;
			if (textProvenance.getValue().equals(value)){
				return textProvenance;
			}
		}
		return null;
	}
}
