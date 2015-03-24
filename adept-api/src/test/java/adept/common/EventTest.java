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

package adept.common;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventTest {

    // Create dummy Chunk using code from ChunkTest, for ease of testing
	private Chunk dummyChunk(String content) {
		String corpusId = "corpusId";
		String type = "corpusType";
		String name = "corpusName";
		String uri = "corpusUri";
		Corpus corpus = new Corpus(corpusId, type, name, uri);
		String docId = "docId";
		String docType = "docType";
		String docUri = "docUri";
		String language = "English";
		Document doc = new Document(docId, corpus, docType, docUri, language );
		doc.setValue(content);
		String[] spaceTokens = content.split(" ");
		TokenStream tokenStream = new TokenStream(TokenizerType.WHITESPACE, 
													TranscriptType.SOURCE, 
													language, 
													ChannelName.NONE, 
													ContentType.TEXT, 
													doc);
//		tokenStream.setDocument( doc );
		int startOffset = 0;
		int sequenceId = 0;
		for( String s : spaceTokens)
		{
			int endOffset = Math.min(startOffset + s.length()+1, content.length());
			CharOffset charOffset = new CharOffset( startOffset, endOffset );
			Token t = new Token( sequenceId, charOffset, s);
			startOffset = endOffset;
			++ sequenceId;
			tokenStream.add(t);
		}		
	    final TokenOffset tokenOffset = new TokenOffset(0, sequenceId-1);
	    return new Chunk(tokenOffset, tokenStream);
	}
	
	private EntityMention dummyEntityMention(int id, String content)
	{
		Chunk chunk = dummyChunk(content);
		return new EntityMention(id, chunk.getTokenOffset(), chunk.getTokenStream());
	}
    
    @Test
    // implements the example from UT Austin's Statistical Script Inference
    // algorithm description
    public void testUTAustin() {
        // Four people have been arrested and a Homeland Security officer is under
        // investigation for firing his weapon during a weekend scuffle between
        // pro-Israel demonstrators and Palestinian supporters outside the
        // Federal Builder in Los Angeles.

        final Type subject = new Type("subject");
        final Type object = new Type("object");

        final EntityMention fourPeople = dummyEntityMention(0, "four People");
        final EntityMention aHomelandSecurityOfficer = dummyEntityMention(1, "a Homeland Security Officer");
        final EntityMention demonstrators = dummyEntityMention(2, "demonstrators");

        final Type chargeType = new Type("charge");
        final EventMention chargeEvent = EventMention.builder(chargeType)
                .addArguments(ImmutableList.of(
                        EventMentionArgument.builder(chargeType, subject, fourPeople).build()))
                .setScore(-11.5f)
                .build();

        final Type shootType = new Type("shoot");
        final EventMention shootEvent = EventMention.builder(shootType)
                .addArguments(ImmutableList.of(
                        EventMentionArgument.builder(shootType, subject, aHomelandSecurityOfficer).build(),
                        EventMentionArgument.builder(shootType, object, demonstrators).build()))
                .setScore(-12.0f)
                .build();

        assertEquals(chargeType, chargeEvent.getEventType());
        assertEquals(2, shootEvent.getArguments().size());
        final EventMentionArgument arg1 = Iterables.getFirst(shootEvent.getArguments(), null);
        final EventMentionArgument arg2 = Iterables.get(shootEvent.getArguments(), 1);
        assertEquals(subject, arg1.getRole());
        assertEquals(object, arg2.getRole());
        assertEquals(aHomelandSecurityOfficer, arg1.getFiller());
        assertEquals(demonstrators, arg2.getFiller());
    }

    @Test
    // implements the example from U Washington's NewsSpike-E2 algorithm description
    public void testUWashingtonNewsSpike() {
        // while the Obamas' visit to Miami-Dade appears to have a domestic agenda...
        final Type travelTo = new Type("travel-to");

        final Type subject = new Type("subject");
        final Type object = new Type("object");

        final EntityMention obama = dummyEntityMention(0, "Obama");
        final EntityMention miamiDade = dummyEntityMention(1, "Miami Dade");

        final EventMention travelEvent = EventMention.builder(travelTo).addArguments(ImmutableSet.of(
                EventMentionArgument.builder(travelTo, subject, obama).build(),
                EventMentionArgument.builder(travelTo, object, miamiDade).build()))
                .setScore(0.8f)
                .build();
        assertEquals(travelTo, travelEvent.getEventType());
        assertEquals(Optional.absent(), travelEvent.getProvenance());
        final EventMentionArgument arg1 = Iterables.getFirst(travelEvent.getArguments(), null);
        final EventMentionArgument arg2 = Iterables.get(travelEvent.getArguments(), 1);
        assertEquals(obama, arg1.getFiller());
        assertEquals(subject, arg1.getRole());
        assertEquals(miamiDade, arg2.getFiller());
        assertEquals(object, arg2.getRole());
    }

    @Test
    // tests representing the sample output from UMass's event argument
    // extraction algorithm description
    public void testUMass() {
        // Police detained Lianne Smith, 43, on Tuesday at the four-start Hotel Miramar on
        // the beach at the coastal resort of Lloret del...
    	final String name = "Lianne Smith";
        final Type justiceArrestJail = new Type("Justice.Arrest-Jail");
        final Type personRole = new Type("Person");
        final EntityMention lianneSmith = dummyEntityMention(0, name);

        final EventMentionArgument arg = EventMentionArgument
                .builder(justiceArrestJail, personRole, lianneSmith).build();
        assertEquals(name, arg.getFiller().value);
        assertEquals(personRole, arg.getRole());
    }

    @Test
    public void testStanfordWithinDoc() {
        // Voters in reclusive Turkmenistan cast ballots Sunday in a parliamentary election
        // hailed by the government as an exercise in democracy but dismissed by critics as
        // a sham.
        final Type electType = new Type("elect");
        final Type ballotingType = new Type("balloting");
        final Chunk electChunk = dummyChunk("election chunk");
        final Chunk ballotingChunk = dummyChunk("balloting chunk");

        final EventText electEvent = EventText.builder(electType, ImmutableList.of(electChunk) ).build();
        final EventText ballotingEvent = EventText.builder(ballotingType, ImmutableList.of(ballotingChunk)).build();

        final EventTextSet eventProvenanceSet = EventTextSet
                .builder(ImmutableSet.of(electEvent, ballotingEvent)).build();
        assert(eventProvenanceSet.getProvenances().contains(ballotingEvent));
        assert(electEvent.getProvenanceChunks().contains(electChunk));
    }

    @Test
    public void testOSU() {
        final DocumentEventArgument.Filler RajatGuptaEntity = DocumentEventArgument.Filler.fromEntity(new Entity(0, new Type("PER")));
        final DocumentEventArgument.Filler RajatGuptaEntityInDoc2 = DocumentEventArgument.Filler.fromEntity(new Entity(0, new Type("PER")));

        final EntityMention RajatGuptaMention1 = dummyEntityMention(0, "Rajat Gupta");
        final EntityMention RajatGuptaMention2 = dummyEntityMention(0, "Rajat Gupta");
        final EntityMention illegallyTippingHisFormerFriend = dummyEntityMention(1, "illegally Tipping His Former Friend");
        final DocumentEventArgument.Filler illegallyTippingEntity = DocumentEventArgument.Filler.fromEntity(new Entity(1, new Type("PER")));
        final Chunk chunk = dummyChunk("2011-10-XX");
        final TimePhrase october = new TimePhrase(chunk.getTokenOffset(), chunk.getTokenStream(), null);        
        october.setResolution("2011-10-XX");
        final DocumentEventArgument.Filler octoberEntity = DocumentEventArgument.Filler.fromTemporalValue(
                TimexValue.fromString(october.getResolution()));
        final EntityMention fiveCountsOfSecuritiesFraud = dummyEntityMention(3, "five Counts Of Securities Fraud");
        final DocumentEventArgument.Filler securitiesFraudEntity = DocumentEventArgument.Filler.fromEntity(new Entity(3, new Type("PER")));
        final EntityMention providingTips = dummyEntityMention(4, "providing tips");

        final DocumentEventArgument.Filler tipsEntity = DocumentEventArgument.Filler.fromEntity(new Entity(4, new Type("PER")));
        final EntityMention charge14PassingIllegal = dummyEntityMention(5, "charge 14 Passing Illegal");
        final DocumentEventArgument.Filler passingIllegalEntity = DocumentEventArgument.Filler.fromEntity(new Entity(5, new Type("PER")));

        // skipping other crime argument for brevity
        final Type JusticeChargeIndict = new Type("Justice.Charge-Indict");
        final Type Defendant = new Type("Defendant");
        final Type Time = new Type("Time");
        final Type Crime = new Type("Crime");

        final Chunk RajatGuptaAFormerDirectorEllipsesHisFormerFriendRajRajaratnam = dummyChunk("Raj Rajaratnam chunk");
        final Chunk GuptaWasIndictedEllipsesCountOfConspiracy = dummyChunk("Count Of Conspiracy chunk");
        final Chunk GuptaAFormerEllipses2007And2008 = dummyChunk("Ellipses 2007 and 2008 chunk");

        final EventMentionArgument charge11Gupta = EventMentionArgument.builder(
                JusticeChargeIndict, Defendant, RajatGuptaMention1).setScore(1.0f).build();
        final EventMentionArgument charge11Crime = EventMentionArgument.builder(
                JusticeChargeIndict, Crime, illegallyTippingHisFormerFriend).setScore(1.0f).build();
        final EventMention charge11 = EventMention.builder(JusticeChargeIndict)
                .setProvenance(EventText.builder(JusticeChargeIndict,
                        RajatGuptaAFormerDirectorEllipsesHisFormerFriendRajRajaratnam).build())
                .addArgument(charge11Gupta)
                .addArgument(charge11Crime)
                .build();

        final EventMentionArgument charge12time = EventMentionArgument.builder(
                JusticeChargeIndict, Time, october).setScore(1.0f).build();
        final EventMentionArgument charge12Crime = EventMentionArgument.builder(
                JusticeChargeIndict, Crime, fiveCountsOfSecuritiesFraud).setScore(1.0f).build();
        final EventMention charge12 = EventMention.builder(JusticeChargeIndict)
                .setProvenance(EventText.builder(JusticeChargeIndict, GuptaWasIndictedEllipsesCountOfConspiracy).build())
                .addArgument(charge12time)
                .addArgument(charge12Crime)
                .build();

        final EventMentionArgument charge13Gupta = EventMentionArgument.builder(
                JusticeChargeIndict, Defendant, RajatGuptaMention2).setScore(1.0f).build();
        final EventMentionArgument charge13Crime = EventMentionArgument.builder(
                JusticeChargeIndict, Crime, providingTips).setScore(1.0f).build();
        final EventMention charge13 = EventMention.builder(JusticeChargeIndict)
                .setProvenance(EventText.builder(JusticeChargeIndict, GuptaAFormerEllipses2007And2008).build())
                .addArgument(charge13Gupta)
                .addArgument(charge13Crime)
                .build();

        final DocumentEventArgument RajatGuptaAsDefendantInDoc1 = DocumentEventArgument.builder(JusticeChargeIndict, Defendant, RajatGuptaEntity)
                .addProvenance(DocumentEventArgument.Provenance.fromArgumentOfEventMention(charge11, charge11Gupta))
                .addProvenance(DocumentEventArgument.Provenance.fromArgumentOfEventMention(charge13, charge13Gupta))
                .build();
        final DocumentEvent chargeEventInDoc1 = DocumentEvent.builder(JusticeChargeIndict)
                .addProvenanceFromEventMention(charge11)
                .addProvenanceFromEventMention(charge12)
                .addArgument(RajatGuptaAsDefendantInDoc1)
                .addArgument(DocumentEventArgument.builder(JusticeChargeIndict, Crime, illegallyTippingEntity)
                                    .addProvenance(DocumentEventArgument.Provenance.fromArgumentOfEventMention(charge11, charge11Crime))
                                    .build())
                .addArgument(DocumentEventArgument.builder(JusticeChargeIndict, Time, octoberEntity)
                                    .addProvenance(DocumentEventArgument.Provenance.fromArgumentOfEventMention(charge12, charge12time))
                                    .build())
                .addArgument(DocumentEventArgument.builder(JusticeChargeIndict, Crime, securitiesFraudEntity)
                                    .addProvenance(DocumentEventArgument.Provenance.fromArgumentOfEventMention(charge12, charge12Crime))
                                    .build())
                .addArgument(DocumentEventArgument.builder(JusticeChargeIndict, Crime, tipsEntity)
                                    .addProvenance(DocumentEventArgument.Provenance.fromArgumentOfEventMention(charge13, charge13Crime))
                                    .build())
                .build();
        
        assertEquals(JusticeChargeIndict, charge11.getEventType());
        assertEquals(2, charge12.getArguments().size());
        final EventMentionArgument arg1 = Iterables.getFirst(charge12.getArguments(), null);
        final EventMentionArgument arg2 = Iterables.get(charge12.getArguments(), 1);
        assertEquals(Time, arg1.getRole());
        assertEquals(Crime, arg2.getRole());
        assertEquals(october, arg1.getFiller());
        assertEquals(fiveCountsOfSecuritiesFraud, arg2.getFiller());
    }
}