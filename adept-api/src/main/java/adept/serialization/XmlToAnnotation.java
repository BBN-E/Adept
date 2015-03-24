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

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;

import adept.common.Argument;
import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.Chunk;
import adept.common.ContentType;
import adept.common.Coreference;
import adept.common.Dependency;
import adept.common.Document;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.EntityTypeFactory;
import adept.common.HltContentContainer;
import adept.common.IType;
import adept.common.MentionTypeFactory;
import adept.common.Pair;
import adept.common.PartOfSpeech;
import adept.common.PartOfSpeechTagFactory;
import adept.common.Relation;
import adept.common.Sentence;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenType;
import adept.common.TokenizerType;
import adept.common.TranscriptType;


// TODO: Auto-generated Javadoc
/**
 * The Class XmlToAnnotation.
 */
public class XmlToAnnotation 
{
	
	/** The Constant UTF8_CHARSET. */
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	
	/** The _ header. */
	private static String _Header;
	
	/** The _ footer. */
	private static String _Footer;
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException
	{
		String fileData = null;
		DataInputStream dis = null; 
		String inFile = args[0];
		try {
			fileData = readFile(inFile, UTF8_CHARSET);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
		 if ( dis != null) dis.close();
		 if ( fileData == null) throw new RuntimeException("No data read from file.");
		}
		XMLSerializer ser = new XMLSerializer(SerializationType.XML);
		HltContentContainer hltCC = (HltContentContainer) ser.deserializeString(fileData, HltContentContainer.class);
		// DEBUG code
//		String xmlFilename = inFile + ".debug.xml";
//		String xmlTemp = ser.serializeAsString(hltCC);
//		Writer.getInstance().writeToFile(xmlFilename,xmlTemp);
		String[] outList = writeHltCC(hltCC);		
		String outfile = args[1];
		PrintWriter writer = new PrintWriter(new FileWriter(outfile));
	    writer.println(_Header);
		for(int i=0;i<outList.length;i++)
		{
		    writer.println(outList[i]);
		}
	    writer.println(_Footer);
		writer.close();
	}
	
	/**
	 * Write hlt cc.
	 *
	 * @param hltCC the hlt cc
	 * @return the string[]
	 */
	static String[] writeHltCC( HltContentContainer hltCC)
	{
		String idHltCC = hltCC.getIdString();
		System.out.println("Read HltContentContainer with id= " + idHltCC);
		
		List<Coreference> corefList = hltCC.getCoreferences();
		//List<Dependency> dependList = hltCC.getDependencies();
		//List<Entity> entityListOut= hltCC
		List<EntityMention> entityList = hltCC.getEntityMentions();
		//List<EntityMention> entityMentionList = hltCC.getEntityMentions();
		//List<EntityMention> namedEntitiesList = hltCC.getNamedEntities();
		List<PartOfSpeech> partOfSpeechList = hltCC.getPartOfSpeechs();
		List<Sentence> sentenceList = hltCC.getSentences();		
		List<Relation> relationList = hltCC.getRelations();
		
		// HACK to get TokenStream.
		// Create output list with one token per line.
		TokenStream tokStream;
		if (corefList == null)
		{
			Relation rel = relationList.get(0);
			Argument arg = rel.getArguments().get(0);
			Pair<Chunk,Float> pair = arg.getArgumentDistribution().get(0);
			tokStream = pair.getL().getTokenStream();
		}
		else
		{
		Coreference coref = corefList.get(0);
		List<EntityMention> cMentList = coref.getResolvedMentions();
		if (cMentList == null)
		{
			// unused code?
			List<Entity> cEntList = coref.getEntities();
			Entity e = cEntList.get(0);
			EntityMention em = e.getCanonicalMention();
			tokStream = em.getTokenStream();
		}
		else 
		{
			EntityMention emTemp = cMentList.get(0);
			tokStream =  emTemp.getTokenStream();		
		}
		}
		int len = tokStream.size();
		String[] outList = new String[len];
		int index = 0;
		for ( Token t : tokStream)
		{
			// TODO - id must equal index
			long id = t.getSequenceId();
			String line = String.format("%d\t%s\t", id, t.getValue());
			System.out.println(line);
			outList[index] = line;
			++ index;
		}
		_Header = "#\tToken\t";
		_Footer = "\tTOTAL\t";
		// Suppress outputs - TODO improve this.
		boolean bPartOfSpeech = false;
		boolean bSentence = false;

		// PART OF SPEECH
		if ( bPartOfSpeech && partOfSpeechList != null)
		{
			int count = 0;
			for ( PartOfSpeech pos : partOfSpeechList)
			{
				IType typ = pos.getPosTag();
				if ( typ!=null) 
				{
					String posType = typ.getType();
					TokenOffset to = pos.getTokenOffset();
					String note;
					int begin = to.getBegin();
					int end = to.getEnd();
					if ( begin == end ) note = posType;
					else note = String.format("%s(%d-%d)",posType,begin, end);
					// Annotate only the first and last tokens (both if different).
					Integer[] temp = ( begin == end) ? new Integer[] {begin} : new Integer[] {begin, end};
					for(int i : temp)
					{
						if ( !outList[i].endsWith("\t")) outList[i] += ", ";
						outList[i] += note; 						
					}
					++ count;
				}
			}
			for ( int i=0; i< outList.length; ++i) outList[i] = outList[i]  + "\t"; 	
			_Header += "POS\t";
			_Footer += String.format("%d\t", count);
		}

		// ENTITY
		if ( entityList != null )
		{
			int count = 0;
			HashMap<String,Integer> dupElim = new HashMap<String,Integer>();
			for ( EntityMention em : entityList)
			{
				//String entId = makeEntId( em );
				IType typ = em.getEntityType();
				String typStr = (typ==null)?null:typ.getType();
				TokenOffset to = em.getTokenOffset();
				int begin = to.getBegin();
				int end = to.getEnd();
				String note = String.format("%s(%d-%d)",typStr,begin, end);
				if (!dupElim.containsKey(note) ) 
				{
					dupElim.put(note, 0);
					// Annotate only the first and last tokens (both if different).
					Integer[] temp = ( begin == end) ? new Integer[] {begin} : new Integer[] {begin, end};
					for(int i : temp)
					{
						if ( !outList[i].endsWith("\t")) outList[i] += ", ";
						outList[i] += note; 
					}
					++ count;
				}
			}		
			for ( int i=0; i< outList.length; ++i) outList[i] = outList[i]  + "\t"; 						
			_Header += "Named Entity\t";
			_Footer += String.format("%d\t", count);
		}

		// COREFERENCE
		if ( corefList != null )
		{
			int count = 0;
			HashMap<String,Integer> dupElim = new HashMap<String,Integer>();
			for ( Coreference c : corefList)
			{
				for ( EntityMention em : c.getResolvedMentions())
				{
					String entId = makeEntId( em );
					//long seqId = em.getSequenceId();
					IType eTyp = em.getEntityType();
					String eTypStr = (eTyp==null)?null:eTyp.getType();
					IType mTyp = em.getMentionType();
					TokenOffset to = em.getTokenOffset();
					int begin = to.getBegin();
					int end = to.getEnd();
					String note = String.format("%s=%s(%s,%d-%d)",
							eTypStr, entId, mTyp.getType(), begin, end);
					if (!dupElim.containsKey(note) ) 
					{
						dupElim.put(note, 0);
						// Annotate only the first and last tokens (both if different).
						Integer[] temp = ( begin == end) ? new Integer[] {begin} : new Integer[] {begin, end};
						for(int i : temp)
						{
							if ( !outList[i].endsWith("\t")) outList[i] += ", ";
							outList[i] += note; 
						}
						++ count;
					}
				}
			}
			for ( int i=0; i< outList.length; ++i) outList[i] = outList[i]  + "\t"; 
			_Header += "Coref Entity Mention\t";
			_Footer += String.format("%d\t", count);
		}

		// RELATION
		if ( relationList != null )
		{
			long relationId = 0;
			int count = 0;
			HashMap<String,Integer> dupElim = new HashMap<String,Integer>();
			for ( Relation relation : relationList)
			{
				for ( Argument argument : relation.getArguments())
				{
					String aTyp = argument.getArgumentType();
					Pair<Chunk,Float> pair = getBestArgument(argument);
					Chunk chunk = pair.getL();
					float confidence = pair.getR();
					// TODO - not yet implemented
//					Chunk chunk = argument.getBestArgument();
					TokenOffset to = chunk.getTokenOffset();
					int begin = to.getBegin();
					int end = to.getEnd();
					String note = String.format("%s(%d,%d-%d)(%.2f)",
							aTyp, relationId, begin, end, confidence);
					if (!dupElim.containsKey(note) ) 
					{
						dupElim.put(note, 0);
						// Annotate only the first and last tokens (both if different).
						Integer[] temp = ( begin == end) ? new Integer[] {begin} : new Integer[] {begin, end};
						for(int i : temp)
						{
							if ( !outList[i].endsWith("\t")) outList[i] += ", ";
							outList[i] += note; 
						}
						++ count;
					}
				}
				++ relationId;
			}
			for ( int i=0; i< outList.length; ++i) outList[i] = outList[i]  + "\t"; 
			_Header += "Relation argument\t";
			_Footer += String.format("%d\t", count);
		}		

		// SENTENCE
		if ( bSentence && sentenceList != null)
		{
			int count = 0;
			for ( Sentence sent : sentenceList)
			{
				TokenOffset to = sent.getTokenOffset();
				int i;
				i = to.getBegin();
				outList[i] = outList[i]  + "sentence_begin"; 						
				i = to.getEnd();
				outList[i] = outList[i]  + "sentence_end"; 						
				++ count;
			}
			for ( int i=0; i< outList.length; ++i) outList[i] = outList[i]  + "\t"; 
			_Header += "Sentence\t";
			_Footer += String.format("%d\t", count);
		}

		return outList;
	}

	// TODO - not implemented in Argument class.  Take highest confidence.
	/**
	 * Gets the best argument.
	 *
	 * @param argument the argument
	 * @return the best argument
	 */
	protected static Pair<Chunk,Float> getBestArgument(Argument argument)
	{
		Pair<Chunk,Float> maxPair = null;
		float max = -1;
		for ( Pair<Chunk,Float> pair : argument.getArgumentDistribution())
		{
			float f = pair.getR();
			if ( max < 0 || max < f )
			{
				max = f;
				maxPair = pair;
			}
		}
		return maxPair;
	}
	
	/**
	 * Make ent id.
	 *
	 * @param em the em
	 * @return the string
	 */
	protected static String makeEntId( EntityMention em )
	{
		String entId;
		Map<Long,Float> entDistrib = em.getEntityIdDistribution();
		if ( entDistrib == null)
		{
			entId="null";
		}
		else if ( entDistrib.size() == 1 )
		{
			Map.Entry<Long,Float> entry = entDistrib.entrySet().iterator().next();
			entId = String.format("%d", entry.getKey());
		}
		else
		{
			StringBuilder sb = new StringBuilder();
			Iterator it = entDistrib.entrySet().iterator();
			while (it.hasNext()) 
			{
				Map.Entry pairs = (Map.Entry)it.next();
				String ent = String.format("[%d,%f]",pairs.getKey(), pairs.getValue());
				sb.append(ent);
			}
			entId = sb.toString();
		}
		return entId;
	}
	
	/**
	 * Read file.
	 *
	 * @param path the path
	 * @param encoding the encoding
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	static String readFile(String path, Charset encoding) throws IOException 
	{
		File f = new File( path );
		byte[] encoded = Files.toByteArray(f);
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
}