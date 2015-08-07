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

package adept.serialization;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adept.common.*;
import adept.io.Reader;
import adept.io.Writer;

public class LoadSerializedHccXmlData {

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		String dataDir = "/nfs/mercury-04/u40/deft/ERE_crossdocentities/";
		String outDir = "/nfs/mercury-04/u40/deft/ERE_xdoc_ncc_2/";
		File folder = new File(dataDir);
		File[] listOfFiles = folder.listFiles();

		// Initialize serializer instance
		XMLSerializer xmls = new XMLSerializer(SerializationType.XML);		
		
	    for (int i = 0; i < listOfFiles.length; i++) {
	      if (listOfFiles[i].isFile()) {
	        System.out.println("File " + listOfFiles[i].getName());	        
	        String serialized = Reader.getInstance().readFileIntoString(listOfFiles[i].getPath());	        
			// deserialize
	        HltContentContainer hcc = (HltContentContainer) xmls.deserializeString(serialized,
	        		HltContentContainer.class);
	        
	        if (hcc.getPassages() == null) {	        	
	        	TokenStream ts = hcc.getCoreferences().get(0).getResolvedMentions().get(0).getTokenStream();	        	
	        	TokenOffset to = new TokenOffset(0, ts.size()-1);
	        	Passage p = new Passage(0, to, ts);
	        	List<Passage> ps = new ArrayList<Passage>();
	        	ps.add(p);
	        	hcc.setPassages(ps);
	        }
	        
	        Document document = hcc.getDocument();	        
			// print fields of the deserialized object
			System.out.println("Printing out the document object just deserialized:");
			System.out.println("Doc ID = " + document.getDocId());
			System.out.println("Doc type is: " + document.getDocType());
			System.out.println("Doc language is: " + document.getLanguage());
			System.out.println("The document text is: " + document.getValue());
			System.out.println("19th token: " + document.getTokenStreamList().get(0).get(18).getValue());
			
			System.out.println("Fixing entity-mention entity-id distribution to local entity");
			for(EntityMention em : hcc.getCoreferences().get(0).getResolvedMentions()) {
				Map.Entry<Long,Float> es = em.getEntityIdDistribution().entrySet().iterator().next();
				long kbEntityId = es.getKey();
				long entityId = kbEntityId;				
				/*for(Entity e : hcc.getCoreferences().get(0).getEntities()) {
					String kbe = e.getKBEntityDistribution().entrySet().iterator().next().getKey();
					if (kbe.getEntityId() == kbEntityId) {
						entityId = e.getEntityId();
						System.out.println("Mention: " + em.getValue() + ", KBE: " + kbEntityId + ", E: " + entityId);
						kbe.setKbUri("http://ere");
						IType entityType = new Type(e.getCanonicalMention().getEntityType().getType());
						kbe.setEntityType(entityType);
						break;
					}
				}*/
				Map<Long,Float> entityIdDist = new HashMap<Long,Float>();
				entityIdDist.put(entityId, es.getValue());
				em.setEntityIdDistribution(entityIdDist);
			}
			// serialize
			String hcc_s = xmls.serializeAsString(hcc);
			String outFile = outDir + "/" + listOfFiles[i].getName() + ".xml";
			Writer.getInstance().writeToFile(outFile, hcc_s);

	      } else if (listOfFiles[i].isDirectory()) {
	        System.out.println("ERROR!! Directory " + listOfFiles[i].getName());
	        System.exit(-1);
	      }
	    }		   

	}
	
	
}