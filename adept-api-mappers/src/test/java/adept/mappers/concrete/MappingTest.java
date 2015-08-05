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

package adept.mappers.concrete;

import edu.jhu.hlt.concrete.io.ProtocolBufferReader;
import java.util.Scanner;

public class MappingTest {


    public static void main(String[] args) {

	ProtocolBufferReader pbr;
	ConcreteAdeptMapper mapper = new ConcreteAdeptMapper();
	try {


	edu.jhu.hlt.concrete.Communication comm = new edu.jhu.hlt.concrete.Communication();
	comm.setText("Concrete originates in Baltimore, Maryland");

        edu.jhu.hlt.concrete.UUID tknUuid = new edu.jhu.hlt.concrete.UUID("1");

        edu.jhu.hlt.concrete.Tokenization tkn = new edu.jhu.hlt.concrete.Tokenization();
        edu.jhu.hlt.concrete.TokenList tknList = new edu.jhu.hlt.concrete.TokenList();
        tkn.setUuid(tknUuid);

	Scanner scanner = new Scanner(comm.getText());

	int index = 1;
	while (scanner.hasNext()) 
	{

	        edu.jhu.hlt.concrete.Token token = new edu.jhu.hlt.concrete.Token();
				token.setTokenIndex(index);
				token.setText(scanner.next());
		tknList.addToTokens(token);
		++index;
	}

        tkn.setTokenList(tknList);


	edu.jhu.hlt.concrete.TokenRefSequence tokenRefSequence = new edu.jhu.hlt.concrete.TokenRefSequence();
		tokenRefSequence.addToTokenIndexList(3);
		tokenRefSequence.addToTokenIndexList(4);
		tokenRefSequence.setTokenizationId(tkn.getUuid());

	edu.jhu.hlt.concrete.UUID entityMentionUuid = new edu.jhu.hlt.concrete.UUID("2");

	edu.jhu.hlt.concrete.EntityMention entityMention = new edu.jhu.hlt.concrete.EntityMention();
		entityMention.setUuid(entityMentionUuid);
		entityMention.setTokens(tokenRefSequence);
		entityMention.setText("Baltimore, Maryland.");

	edu.jhu.hlt.concrete.Communication communication = new edu.jhu.hlt.concrete.Communication();

	edu.jhu.hlt.concrete.SectionSegmentation sectionSegmentation = new edu.jhu.hlt.concrete.SectionSegmentation();

	edu.jhu.hlt.concrete.Section section = new edu.jhu.hlt.concrete.Section();

	edu.jhu.hlt.concrete.SentenceSegmentation sentenceSegmentation = new edu.jhu.hlt.concrete.SentenceSegmentation();

/*	edu.jhu.hlt.concrete.Sentence sentence = new edu.jhu.hlt.concrete.Sentence(comm.getSectionSegmentations()
				.get(0)
                                .getSectionList().get(0)
                                .getSentenceSegmentation().get(0)
                                .getSentenceList().get(0));
*/
	
	edu.jhu.hlt.concrete.Sentence sentence = new edu.jhu.hlt.concrete.Sentence();

	sentence.addToTokenizationList(tkn);

/*	sentenceSegmentation.setUuid(comm.getSectionSegmentations().get(0)
                                .getSectionList().get(0)
                                .getSentenceSegmentation().get(0)
				.getUuid());
*/
	sentenceSegmentation.addToSentenceList(sentence);

/*	section.setUuid(comm.getSectionSegmentations().get(0)
                                .getSectionList().get(0)
				.getUuid());
	section.setKind(comm.getSectionSegmentations().get(0)
                                .getSectionList().get(0)
				.getKind());
*/
	section.addToSentenceSegmentation(sentenceSegmentation);
/*
	sectionSegmentation.setUuid(comm.getSectionSegmentations().get(0)
				.getUuid());
*/
	sectionSegmentation.addToSectionList(section);

				communication.setUuid(comm.getUuid());
				comm.addToSectionSegmentations(sectionSegmentation);


	edu.jhu.hlt.concrete.EntityMention concreteEntityMention = new edu.jhu.hlt.concrete.EntityMention(entityMention);


	System.out.println("Concrete Entity Mention Value: " + concreteEntityMention.getText());

	adept.common.EntityMention adeptEntityMention = mapper.map(comm, concreteEntityMention);

	System.out.println("Mapped Concrete Entity Mention");

	System.out.println("Adept Entity Mention Value: " + adeptEntityMention.getValue());

	} catch(Exception e){
	    e.printStackTrace();
	    System.exit(1);
	}
    }
}