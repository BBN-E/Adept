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

package adept.utilities;

import adept.common.*;
import adept.serialization.*;
import adept.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.List;



// A Class that prints HltContent in tab-separated format
/**
 * The Class printHltContent.
 */
public class printHltContent {

	/** The hcc. */
	private static HltContentContainer hcc;

	/** The deserializer. */
	private static XMLSerializer deserializer = new XMLSerializer(
			SerializationType.XML);

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		String input_path = args[0];
		File input_directory = new File(input_path);

		for (File hcc_file : input_directory.listFiles()) {
			try {
				// Ignore subfolders.
				if (hcc_file.isDirectory()) continue;
				String name = hcc_file.getName();
				String filename = input_path + name;
				System.out.println("Working on file: " + filename);
				printOneFile( filename);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Write file.
	 *
	 * @param inFile the in file
	 * @param outFile the out file
	 */
	public static void writeFile( String inFile, String outFile)
	{
		PrintStream console = System.out;
		try {
			File out = new File(outFile);
			System.setOut(new PrintStream(out));
			System.setErr(new PrintStream(out));
			printOneFile(inFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			System.setErr(console);
			System.setOut(console);
		}		
	}
	
	/**
	 * Prints the one file.
	 *
	 * @param filename the filename
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	protected static void 	printOneFile(String filename) throws UnsupportedEncodingException {
		String content = Reader.getInstance().readFileIntoString(filename);

		if (content != null) {
			hcc = (HltContentContainer) deserializer.deserializeString(
					content, HltContentContainer.class);
		} else {
			System.out.println("Could not read data...ending!");
			return;
		}

		// Get Document 
		Document document = hcc.getDocument();		
		
		if(document !=null)
		{
			System.out.println("Printing document attributes..");
			System.out.println("IdString: " + document.getIdString() );
			System.out.println("CaptureDate: " + document.getCaptureDate());
			System.out.println("DocId: " + document.getDocId());
			System.out.println("DocType: " + document.getDocType());
			System.out.println("Genre: " + document.getGenre());
			System.out.println("Headline: " + document.getHeadline());
			System.out.println("Language: " + document.getLanguage());
			System.out.println("Name: " + document.getName());
			System.out.println("PublicationDate: " + document.getPublicationDate());
			System.out.println("SourceLanguage: " + document.getSourceLanguage());
			System.out.println("SourceUri: " + document.getSourceUri());
			System.out.println("SplitId: " + document.getSplitId());
			System.out.println("Uri: " + document.getUri());
			System.out.println("");
			System.out.println("[" + document.getValue() + "]\n");			
		}		
		
		if (hcc.getEntityMentions() != null) {
			System.out.println("Printing Entity Mentions:");
			System.out
			//.println("Value\tentityType\tmentionType\tbegin\tend");
			.println("Value\tentityType\tmentionType\tbegin\tend\tDocumentID");
			for (EntityMention em : hcc.getEntityMentions()) {

				String entityType = "UNKNOWN";
				if (em.getEntityType() != null) {
					entityType = em.getEntityType().getType();
				}
				String mentionType = "UNKNOWN";
				if (em.getMentionType() != null) {
					mentionType = em.getMentionType().getType();
				}
				System.out.println(em.getValue() + "\t" + entityType + "\t"
						+ mentionType + "\t"
						+ em.getTokenOffset().getBegin() + "\t"
						//+ em.getTokenOffset().getEnd());
						+ em.getTokenOffset().getEnd() + "\t"
						+ em.getTokenStream().getDocument().getId());
			}
		}
		
        if(hcc.getCoreferences()!=null)
        {
        	System.out.println("Printing Entity with their Cluster IDs:");
    		System.out.println("Value\tentityType\tcanonicalMentionValue\tmentionType\tentityClusterID");
    		for (Entity e : hcc.getCoreferences().get(0).getEntities()) {
    			String entityType = "UNKNOWN";
    			if (e.getEntityType() != null) {
    				entityType = e.getEntityType().getType();
    			}
    			String mentionType = "UNKNOWN";
    			if (e.getCanonicalMention().getMentionType() != null) {
    				mentionType = e.getCanonicalMention().getMentionType()
    						.getType();
    			}
    			System.out.println(e.getValue() + "\t" + entityType + "\t"
    					+ e.getCanonicalMention().getValue() + "\t"
    					+ mentionType + "\t" + e.getEntityId());
    		}

    		System.out.println("Printing Resolved Entity Mentions to respective Entity Cluster IDs:");
    		System.out.println("Value\tentityType\tmentionType\tbegin\tend\tentityClusterID\tDocumentID");
    		for (Coreference cor : hcc.getCoreferences()) {    			    			
	    		for (EntityMention em : cor
	    				.getResolvedMentions()) {
	    			String entityType = "UNKNOWN";
	    			if (em.getEntityType() != null) {
	    				entityType = em.getEntityType().getType();
	    			}
	    			String mentionType = "UNKNOWN";
	    			if (em.getMentionType() != null) {
	    				mentionType = em.getMentionType().getType();
	    			}
	
	    			System.out.println(em.getValue()
	    					+ "\t"
	    					+ entityType
	    					+ "\t"
	    					+ mentionType
	    					+ "\t"
	    					+ em.getTokenOffset().getBegin()
	    					+ "\t"
	    					+ em.getTokenOffset().getEnd()
	    					+ "\t"
	    					+ em.getEntityIdDistribution().keySet().iterator()
	    					.next()
	    					+ "\t"
	    					+ em.getTokenStream().getDocument().getId());
	    		}
	        }
        }		

		if (hcc.getRelations() != null) {
			System.out.println("Printing Relations with associated arguments");
			System.out.println("arg1Value\targ1Type\targ1Begin\targ1End\t"
					+ "arg2Value\targ2Type\targ2Begin\targ2End\tRelationType");
			for (Relation r : hcc.getRelations()) {

				String relationType = "UNKNOWN";
				if (r.getRelationType() != null) {
					relationType = r.getRelationType();
				}

				List<Argument> arguments = r.getArguments();
				Argument arg1 = arguments.get(0);
				Argument arg2 = arguments.get(1);

				String arg1Type = "UNKNOWN";
				if (arg1.getArgumentType() != null) {
					arg1Type = arg1.getArgumentType();
				}
				String arg2Type = "UNKNOWN";
				if (arg2.getArgumentType() != null) {
					arg2Type = arg2.getArgumentType();
				}

				EntityMention arg1Em = null, arg2Em = null;

				Chunk arg1Chunk = arg1.getArgumentDistribution().get(0)
						.getL();
				if (arg1Chunk instanceof EntityMention) {
					arg1Em = (EntityMention) arg1Chunk;
				}

				Chunk arg2Chunk = arg2.getArgumentDistribution().get(0)
						.getL();
				if (arg2Chunk instanceof EntityMention) {
					arg2Em = (EntityMention) arg2Chunk;
				}
				if (arg1Em != null && arg2Em != null) {

					String arg1EmEntityType = arg1Type;
					if (arg1Em.getEntityType() != null) {
						arg1EmEntityType = arg1Em.getEntityType()
								.getType();
					}
					String arg2EmEntityType = arg2Type;
					if (arg2Em.getEntityType() != null) {
						arg2EmEntityType = arg2Em.getEntityType()
								.getType();
					}
					System.out.println(arg1Em.getValue() + "\t"
							+ arg1EmEntityType + "\t"
							+ arg1Em.getTokenOffset().getBegin() + "\t"
							+ arg1Em.getTokenOffset().getEnd() + "\t"
							+ arg2Em.getValue() + "\t" + arg2EmEntityType
							+ "\t" + arg2Em.getTokenOffset().getBegin()
							+ "\t" + arg2Em.getTokenOffset().getEnd()
							+ "\t" + relationType);
				} else {
					System.out.println(arg1Chunk.getValue() + "\t"
							+ arg1Type + "\t"
							+ arg1Chunk.getTokenOffset().getBegin() + "\t"
							+ arg1Chunk.getTokenOffset().getEnd() + "\t"
							+ arg2Chunk.getValue() + "\t" + arg2Type + "\t"
							+ arg2Chunk.getTokenOffset().getBegin() + "\t"
							+ arg2Chunk.getTokenOffset().getEnd() + "\t"
							+ relationType);
				}
			}
		}		
		
		if(hcc.getSarcasms()!=null)
		{
			System.out.println("Printing Sarcasm instances with their confidence values");
			System.out.println("SarcasmId\tValue\tBeginOffset\tEndOffset\tJudgment\tConfidence\n");
			
			for(Sarcasm s : hcc.getSarcasms())
			{
				System.out.println(s.getSarcasmId() + "\t"
						+ s.getValue() + "\t" +
						+ s.getTokenOffset().getBegin() + "\t"
						+ s.getTokenOffset().getEnd() + "\t" +
						s.getJudgment() + "\t" + 
						s.getConfidence() + "\n");
			}
		}		
		
		if(hcc.getOpinions()!=null)
		{
			System.out.println("Printing opinion instances..");
			System.out.println("OpinionId\tValue\tBeginOffset\tEndOffset\tPolarity\tSubjectivity\n");
			
			for(Opinion o : hcc.getOpinions())
			{
				System.out.println(o.getIdString() + "\t"
						+ o.getValue() + "\t" +
						+ o.getTokenOffset().getBegin() + "\t"
						+ o.getTokenOffset().getEnd() + "\t" +
						o.getPolarity() + "\t" + 
						o.getSubjectivity() + "\n");
			}
		}		
		
		if(hcc.getPassages()!=null)
		{
			System.out.println("Printing passage instances..");
			System.out.println("PassageId\t\tBeginOffset\tEndOffset\tContentType\tSequenceId\n");
			
			for(Passage passage : hcc.getPassages())
			{
				System.out.println(passage.getIdString() + "\t"
						+ passage.getTokenOffset().getBegin() + "\t"
						+ passage.getTokenOffset().getEnd() + "\t" 
						+ passage.getContentType() + "\t"
						+ passage.getSequenceId() + "\t"
						+ passage.getContentType());
				System.out.println("[" + passage.getValue() + "]\n");
			}
		}
		
		System.out.println("-------------------------------------------------");
	}
}