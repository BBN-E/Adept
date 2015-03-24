package adept.mappers.concrete;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.IOException;
import adept.mappers.concrete.ConcreteAdeptMapper;
  
public class TimePhraseTest {

    public static void main(String[] args) {

	ConcreteAdeptMapper mapper = new ConcreteAdeptMapper();
	
	try {
	PrintWriter writer = new PrintWriter("./mapping_test_out.txt", "UTF-8");

	writer.println("Testing Adept to Concrete Conversion...");

        int errors = 0;

	adept.common.Corpus corpusAdept = new adept.common.Corpus("Corpus001", "AToTType", "AToT Corpus", "AtoT uri");

	adept.common.Document documentAdept = new adept.common.Document("Doc001", corpusAdept, "AToTDocType", "AToTDoc uri", "AToT English");
	//documentAdept.setAudioUri("ATtoTDoc audio uri");
	documentAdept.setGenre("AToT genre");
	documentAdept.setHeadline("AToT headlne");
	documentAdept.setValue("Token1 Token2");
	
	adept.common.AnomalousText anomalousTextAdept = new adept.common.AnomalousText((float)0.1, documentAdept);
	anomalousTextAdept.setExplanation("AToT explanation");

	adept.common.AudioOffset audioOffsetAdept = new adept.common.AudioOffset((float)0, (float)6);

	adept.common.CharOffset charOffsetAdept = new adept.common.CharOffset(0, 6);

	adept.common.TokenOffset tokenOffsetAdept = new adept.common.TokenOffset(0, 1);

	adept.common.Token token1Adept = new adept.common.Token(1, charOffsetAdept, "Token1");
	token1Adept.setConfidence((float)0.1);
	token1Adept.setLemma("AToT lemma 1");
	token1Adept.setAudioOffset(audioOffsetAdept);

	adept.common.Token token2Adept = new adept.common.Token(2, charOffsetAdept, "Token2");
	token2Adept.setConfidence((float)0.2);
	token2Adept.setLemma("AToT lemma 2");
	token2Adept.setAudioOffset(audioOffsetAdept);

	adept.common.TokenStream tokenStreamAdept = new adept.common.TokenStream(adept.common.TokenizerType.ADEPT, adept.common.TranscriptType.SOURCE, "AToT English", adept.common.ChannelName.MONO, adept.common.ContentType.TEXT, documentAdept);
	tokenStreamAdept.setAsrName(adept.common.AsrName.BBN);
//	tokenStreamAdept.setDocument(documentAdept);
	tokenStreamAdept.setSpeechUnit(adept.common.SpeechUnit.NONE);
	tokenStreamAdept.setTranslatorName(adept.common.TranslatorName.NONE);
	tokenStreamAdept.add(token1Adept);
	tokenStreamAdept.add(token2Adept);

	adept.common.EventPhrase eventPhrase = new adept.common.EventPhrase(tokenOffsetAdept, tokenStreamAdept, adept.common.OntTypeFactory.getInstance().getType("EVENTPHRASE", "PAST_TENSE"), adept.common.OntTypeFactory.getInstance().getType("EVENTPHRASE", "PROGRESSIVE_ASPECT"), adept.common.OntTypeFactory.getInstance().getType("EVENTPHRASE", "ASPECTUAL_CLASS"));


	adept.common.TimePhrase timePhrase = new adept.common.TimePhrase(tokenOffsetAdept, tokenStreamAdept, adept.common.OntTypeFactory.getInstance().getType("TACKBP", "Time"));

	edu.jhu.hlt.concrete.Concrete.Situation situation = mapper.map(eventPhrase);

	edu.jhu.hlt.concrete.Concrete.Entity entity = mapper.map(timePhrase);


	String s = eventPhrase.getTense().toLowerCase();
	s = s.substring(s.lastIndexOf("#") + 1);

        if (!s.toUpperCase().equals(situation.getTimemlTense().toString())) {
		errors++;
	}

	s = eventPhrase.getAspect().toLowerCase();
	s = s.substring(s.lastIndexOf("#") + 1);

        if (!s.toUpperCase().equals(situation.getTimemlAspect().toString())) {
		errors++;
	}

	s = eventPhrase.getEventClass().toLowerCase();
	s = s.substring(s.lastIndexOf("#") + 1);

        if (!s.toUpperCase().equals(situation.getTimemlClass().toString())) {
		errors++;
	}

	writer.println("Adept EventPhrase Tense, Aspect, and Class " + eventPhrase.getTense() + " " + eventPhrase.getAspect() + " " + eventPhrase.getEventClass());
	writer.println("Concrete Situation " + situation);

	writer.println("Adept Time Phrase Type " + timePhrase.getType());
	writer.println("Concrete Entity " + entity);

	situation = edu.jhu.hlt.concrete.Concrete.Situation.newBuilder(situation)
		.setTimemlTense(edu.jhu.hlt.concrete.Concrete.Situation.TimeMLTense.PRESENT_TENSE)
		.setTimemlClass(edu.jhu.hlt.concrete.Concrete.Situation.TimeMLClass.STATE_CLASS)
		.setTimemlAspect(edu.jhu.hlt.concrete.Concrete.Situation.TimeMLAspect.PERFECTIVE_ASPECT)
		.build();

	eventPhrase = mapper.mapEventPhrase(situation);

	s = eventPhrase.getTense().toLowerCase();
	s = s.substring(s.lastIndexOf("#") + 1);

        if (!s.toUpperCase().equals(situation.getTimemlTense().toString())) {
		errors++;
	}

	s = eventPhrase.getAspect().toLowerCase();
	s = s.substring(s.lastIndexOf("#") + 1);

        if (!s.toUpperCase().equals(situation.getTimemlAspect().toString())) {
		errors++;
	}

	s = eventPhrase.getEventClass().toLowerCase();
	s = s.substring(s.lastIndexOf("#") + 1);

        if (!s.toUpperCase().equals(situation.getTimemlClass().toString())) {
		errors++;
	}

	writer.println("Adept EventPhrase Tense, Aspect, and Class " + eventPhrase.getTense() + " " + eventPhrase.getAspect() + " " + eventPhrase.getEventClass());
	writer.println("Concrete Situation " + situation);
	
	writer.close();
	System.out.println("Number of conversion errors: " + errors + "\nSee mapping_test_out.txt for details");
	} catch (IOException e) {
		e.printStackTrace();
	}
		

    }
}

	

	

	
