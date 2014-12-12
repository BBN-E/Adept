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

package adept.mappers.thrift.converters;
import org.dozer.DozerBeanMapper;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import adept.common.BoundedList;
import adept.common.Pair;
import adept.common.Chunk;

import thrift.adept.common.ChunkUnion;

public class ArgumentConverter implements org.dozer.CustomConverter {
  
  public Object convert(Object destination, Object source, 
      Class destClass, Class sourceClass) {
    if (source == null) {
      return null;
    }
    DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList(new String[]{"adept/mappers/thrift/ThriftAdeptMappings.xml"}));
    if (source instanceof adept.common.Argument) {

		adept.common.Argument argument = (adept.common.Argument) source;

		thrift.adept.common.Argument retArgument = new thrift.adept.common.Argument(new thrift.adept.common.Type(argument.getArgumentType()));

		retArgument.setId(mapper.map(argument.getId(), thrift.adept.common.ID.class));
		retArgument.setValue(argument.getValue());
		retArgument.setAlgorithmName(argument.getAlgorithmName());

		Map<thrift.adept.common.ChunkUnion, Double> argumentDistribution = new HashMap<thrift.adept.common.ChunkUnion, Double>();

		for (Pair pair : argument.getArgumentDistribution()) {

			ChunkUnion chunkUnion = new ChunkUnion();
			chunkUnion = setChunkUnionField(chunkUnion, pair.getL(), mapper);
			argumentDistribution.put(chunkUnion, new Double(pair.getR().toString()));
		}

		retArgument.setArgumentDistribution(argumentDistribution);

		return retArgument;
   }
   else if (source instanceof ArrayList) {

		List<thrift.adept.common.Argument> retList = new ArrayList<thrift.adept.common.Argument>();
		List<Object> sourceList = (ArrayList<Object>) source;
		for (Object sourceObject : sourceList) {
			adept.common.Argument argument = (adept.common.Argument) sourceObject;

			thrift.adept.common.Argument retArgument = mapper.map(argument, thrift.adept.common.Argument.class);

			Map<thrift.adept.common.ChunkUnion, Double> argumentDistribution = new HashMap<thrift.adept.common.ChunkUnion, Double>();

			for (Pair pair : argument.getArgumentDistribution()) {
				ChunkUnion chunkUnion = new ChunkUnion();
				chunkUnion = setChunkUnionField(chunkUnion, pair.getL(), mapper);
				Float f = (Float) pair.getR();
				Double d = f.doubleValue();
				argumentDistribution.put(chunkUnion, d);
			}

			retArgument.setArgumentDistribution(argumentDistribution);

			retList.add(retArgument);
		}
		return retList;			
    }
   else {
      throw new org.dozer.MappingException("Converter ArgumentConverter "
          + "used incorrectly. Arguments passed in were:"
          + destination + " and " + source);
    }
  } 
	private ChunkUnion setChunkUnionField(ChunkUnion chunkUnion, Object obj, DozerBeanMapper mapper) {

		String className = obj.getClass().getSimpleName().toLowerCase();
		if (className.equals("committedbelief")) {
			chunkUnion.setCommittedBelief(mapper.map(obj, thrift.adept.common.CommittedBelief.class));
		} else if (className.equals("discourseunit")) {
			chunkUnion.setDiscourseUnit(mapper.map(obj, thrift.adept.common.DiscourseUnit.class));
		} else if (className.equals("entitymention")) {
			chunkUnion.setEntityMention(mapper.map(obj, thrift.adept.common.EntityMention.class));
		} else if (className.equals("opinion")) {
			chunkUnion.setOpinion(mapper.map(obj, thrift.adept.common.Opinion.class));
		} else if (className.equals("partofspeech")) {
			chunkUnion.setPartOfSpeech(mapper.map(obj, thrift.adept.common.PartOfSpeech.class));
		} else if (className.equals("passage")) {
			chunkUnion.setPassage(mapper.map(obj, thrift.adept.common.Passage.class));
		} else if (className.equals("prosodicphrase")) {
			chunkUnion.setProsodicPhrase(mapper.map(obj, thrift.adept.common.ProsodicPhrase.class));
		} else if (className.equals("sarcasm")) {
			chunkUnion.setSarcasm(mapper.map(obj, thrift.adept.common.Sarcasm.class));
		} else if (className.equals("sentence")) {
			chunkUnion.setSentence(mapper.map(obj, thrift.adept.common.Sentence.class));
		} else if (className.equals("story")) {
			chunkUnion.setStory(mapper.map(obj, thrift.adept.common.Story.class));
		} else if (className.equals("syntacticchunk")) {
			chunkUnion.setSyntacticChunk(mapper.map(obj, thrift.adept.common.SyntacticChunk.class));
		} else if (className.equals("utterance")) {
			chunkUnion.setUtterance(mapper.map(obj, thrift.adept.common.Utterance.class));
		} else if (className.equals("chunk")) {
			chunkUnion.setChunk(mapper.map(obj, thrift.adept.common.Chunk.class));
		}
		return chunkUnion;
	}
		
}
