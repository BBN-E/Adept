/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

package adept.mappers.concrete;

import edu.jhu.hlt.concrete.io.ProtocolBufferReader;
import java.util.Scanner;

public class MappingTest {


    public static void main(String[] args) {

	ProtocolBufferReader pbr;
	ConcreteAdeptMapper mapper = new ConcreteAdeptMapper();


	try {
	    pbr = new ProtocolBufferReader(args[0], edu.jhu.hlt.concrete.Concrete.Communication.class);

	edu.jhu.hlt.concrete.Concrete.Communication comm = (edu.jhu.hlt.concrete.Concrete.Communication)(pbr.next());

        edu.jhu.hlt.concrete.Concrete.UUID tknUuid = edu.jhu.hlt.concrete.Concrete.UUID.newBuilder()
				.setHigh(1)
				.setLow(2)
				.build();

        edu.jhu.hlt.concrete.Concrete.Tokenization.Builder tknBuilder = edu.jhu.hlt.concrete.Concrete.Tokenization.newBuilder()
                                .setUuid(tknUuid);

	Scanner scanner = new Scanner(comm.getText());

	int index = 1;
	while (scanner.hasNext()) 
	{

	        edu.jhu.hlt.concrete.Concrete.Token token = edu.jhu.hlt.concrete.Concrete.Token.newBuilder()
				.setTokenIndex(index)
				.setText(scanner.next())
				.build();
		tknBuilder.addToken(token);
		++index;
	}

        edu.jhu.hlt.concrete.Concrete.Tokenization tkn = tknBuilder.build();


	edu.jhu.hlt.concrete.Concrete.TokenRefSequence tokenRefSequence = edu.jhu.hlt.concrete.Concrete.TokenRefSequence.newBuilder()
		.addTokenIndex(0)
		.addTokenIndex(1)
		.setTokenizationId(tkn.getUuid())
		.build();

	edu.jhu.hlt.concrete.Concrete.UUID entityMentionUuid = edu.jhu.hlt.concrete.Concrete.UUID.newBuilder()
		.setHigh(3)
		.setLow(4)
		.build();

	edu.jhu.hlt.concrete.Concrete.EntityMention entityMention = edu.jhu.hlt.concrete.Concrete.EntityMention.newBuilder()
		.setUuid(entityMentionUuid)
		.setTokens(tokenRefSequence)
		.setText("Baltimore, Maryland.")
		.build();

	edu.jhu.hlt.concrete.Concrete.Communication.Builder communicationBuilder = edu.jhu.hlt.concrete.Concrete.Communication.newBuilder();

	edu.jhu.hlt.concrete.Concrete.SectionSegmentation.Builder sectionSegmentationBuilder = edu.jhu.hlt.concrete.Concrete.SectionSegmentation.newBuilder();

	edu.jhu.hlt.concrete.Concrete.Section.Builder sectionBuilder = edu.jhu.hlt.concrete.Concrete.Section.newBuilder();

	edu.jhu.hlt.concrete.Concrete.SentenceSegmentation.Builder sentenceSegmentationBuilder = edu.jhu.hlt.concrete.Concrete.SentenceSegmentation.newBuilder();

	edu.jhu.hlt.concrete.Concrete.Sentence.Builder sentenceBuilder = edu.jhu.hlt.concrete.Concrete.Sentence.newBuilder(comm.getSectionSegmentation(0)
                                .getSection(0)
                                .getSentenceSegmentation(0)
                                .getSentence(0));

	edu.jhu.hlt.concrete.Concrete.Sentence sentence = sentenceBuilder.addTokenization(tkn).build();

	edu.jhu.hlt.concrete.Concrete.SentenceSegmentation sentenceSegmentation = sentenceSegmentationBuilder.setUuid(comm.getSectionSegmentation(0)
                                .getSection(0)
                                .getSentenceSegmentation(0)
				.getUuid())
				.addSentence(sentence)
				.build();

	edu.jhu.hlt.concrete.Concrete.Section section = sectionBuilder.setUuid(comm.getSectionSegmentation(0)
                                .getSection(0)
				.getUuid())
				.setKind(comm.getSectionSegmentation(0)
                                .getSection(0)
				.getKind())
				.addSentenceSegmentation(sentenceSegmentation)
				.build();

	edu.jhu.hlt.concrete.Concrete.SectionSegmentation sectionSegmentation = sectionSegmentationBuilder.setUuid(comm.getSectionSegmentation(0)
				.getUuid())
				.addSection(section)
				.build();

	edu.jhu.hlt.concrete.Concrete.Communication communication = communicationBuilder.setGuid(comm.getGuid())
				.setUuid(comm.getUuid())
				.addSectionSegmentation(sectionSegmentation)
				.build();


	edu.jhu.hlt.concrete.ConcreteEntityMention concreteEntityMention = new edu.jhu.hlt.concrete.ConcreteEntityMention(entityMention, communication);

	System.out.println("Made Concrete Entity Mention");

	System.out.println(concreteEntityMention.getTokensAsStrings());

	adept.common.EntityMention adeptEntityMention = mapper.map(concreteEntityMention);

	System.out.println("Mapped Concrete Entity Mention");

	System.out.println("Adept Entity Mention Value: " + adeptEntityMention.getTokenStream().getDocument().getValue());

	} catch(Exception e){
	    System.err.println(e.getMessage());
	    System.exit(1);
	}
    }
}
	
