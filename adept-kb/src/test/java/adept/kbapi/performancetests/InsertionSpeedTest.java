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

package adept.kbapi.performancetests;

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


import com.hp.hpl.jena.query.ResultSet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import adept.common.KBID;
import adept.common.OntType;
import adept.common.Pair;
import adept.kbapi.KB;
import adept.kbapi.KBConfigurationException;
import adept.kbapi.KBEntity;
import adept.kbapi.KBEntityMentionProvenance;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBParameters;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 * @author dkolas
 */
public class InsertionSpeedTest {


	private static final int PROVENANCES_PER_DOC = 25;
	private static OntType weapon = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Person");

	/**
	 * @param args
	 * @throws IOException
	 * @throws KBConfigurationException
	 * @throws InvalidPropertiesFormatException
	 * @throws KBUpdateException
	 * @throws InterruptedException
	 * @throws KBQueryException
	 * @throws ExecutionException
	 */
	public static void main(String[] args) throws InvalidPropertiesFormatException, KBConfigurationException, IOException, KBUpdateException, InterruptedException, KBQueryException, ExecutionException {
		String runName = "PerfRun"+System.currentTimeMillis()+"-";
		KB kb = new KB(new KBParameters());
//		System.out.println("Removing orphan metadata");
		//kb.removeOrphanMetaData();

		System.out.println("Deleting old objects");
		deleteOld(kb);
		long manyInsertStart = System.currentTimeMillis();
		System.out.println("Inserting many objects");
		List<KBID> insertedObjects = new ArrayList<KBID>();
		int numObjects = 1000;

		ExecutorService service = Executors.newFixedThreadPool(4);
		List<Callable<Pair<KBID, Long>>> tasks = new ArrayList<Callable<Pair<KBID,Long>>>();

		for (int i=0; i<numObjects; i++){
			tasks.add(new TimeInsertion(1, runName, kb, "-"+i));
		}
		List<Future<Pair<KBID, Long>>> insertManyResults = service.invokeAll(tasks);
		long total = 0l;
		for (Future<Pair<KBID, Long>> result : insertManyResults){
			insertedObjects.add(result.get().getL());
			total += result.get().getR();
		}

		long manyInsertTime = System.currentTimeMillis() - manyInsertStart;


		int[] numbersToTime = new int[]{1,1000,2000,3000,4000,5000,6000,7000,8000,9000,10000, 1};

//		int[] numbersToTime = new int[]{1000};
		List<Pair<KBID, Long>> results = new ArrayList<Pair<KBID,Long>>();
		long[] queryTimes = new long[numbersToTime.length];
		for (int i = 0; i < numbersToTime.length; i++) {
			int numProvenances = numbersToTime[i];
			System.out.println("Timing: "+numProvenances);
			Pair<KBID, Long> result = timeInsertion(numProvenances, runName, kb, "");
			System.out.println(result.getR());
			System.out.println("Querying...");
			long start = System.currentTimeMillis();
			kb.getGenericThingByID(result.getL());
			queryTimes[i] = System.currentTimeMillis() - start;
			System.out.println(queryTimes[i]);
			kb.deleteKBObject(result.getL());
			results.add(result);
		}

		File outputFile = new File(args[0]);
		Writer writer = new BufferedWriter(new FileWriter(outputFile));
		writer.write("Time to insert ("+numObjects+" objects): "+manyInsertTime+"\n\n\n");
		writer.write("Avg individual insert time ("+numObjects+" objects): "+(((double)total)/numObjects)+"\n\n\n");
		writer.write("Avg total insert time ("+numObjects+" objects): "+(((double)manyInsertTime)/numObjects)+"\n\n\n");
		writer.write("NumProvenances,InsertTime (ms),QueryTime (ms)\n");
		for (int i = 0; i < results.size(); i++) {
			Pair<KBID, Long> result = results.get(i);
			writer.write(numbersToTime[i]+","+result.getR()+","+queryTimes[i]+"\n");
		}
		writer.close();

		System.out.println("Cleaning up...");
		for (Pair<KBID, Long> result : results){
			kb.deleteKBObject(result.getL());
		}
		for (KBID result : insertedObjects){
			kb.deleteKBObject(result);
		}
		kb.close();

	}

	/**
	 * @throws KBUpdateException
	 *
	 */
	private static void deleteOld(KB kb) throws KBUpdateException {
		ResultSet resultSet = kb.executeSelectQuery("PREFIX adept-kb: <http://adept-kb.bbn.com/adept-core#> \r\n" +
				"SELECT * WHERE { ?x a adept-kb:Weapon}");
		while (resultSet.hasNext()){
			String id = resultSet.next().get("?x").asResource().getURI();
			deleteObj(id.substring(id.indexOf("#")+1), kb);
		}
	}

	/**
	 * @param string
	 * @throws KBUpdateException
	 */
	private static void deleteObj(String string, KB kb) throws KBUpdateException {
		kb.deleteKBObject(new KBID(string, KBOntologyModel.DATA_INSTANCES_PREFIX));
	}

	private static Pair<KBID, Long> timeInsertion(int numProvenances, String runName, KB kb, String additionalNamePart) throws KBUpdateException{

		//String name = runName+numProvenances+additionalNamePart;
		KBEntity.InsertionBuilder thing = KBEntity.entityInsertionBuilder(
				Collections.singletonMap(weapon, .1f),
				generateProvenance(numProvenances, 0, "NOMINAL"),
				.1f, .1f);
		for (int i=0; i<numProvenances; i++){
			thing.addProvenance(generateProvenance(numProvenances, i));
		}
		long start = System.currentTimeMillis();
		KBEntity insertedThing = thing.insert(kb);
		return new Pair<KBID, Long>(insertedThing.getKBID(), System.currentTimeMillis()-start);
	}

	protected static KBTextProvenance.InsertionBuilder generateProvenance(int numProvenances, int index) {
		int docNum = index / PROVENANCES_PER_DOC;
		KBTextProvenance.InsertionBuilder builder = KBTextProvenance.builder();
		builder.setBeginOffset(0);
		builder.setEndOffset(1);
		builder.setConfidence(.5f);
		builder.setContributingSiteName("Contributing Site ");
		builder.setCorpusID("CorpusID");
		builder.setCorpusName("Corpus" );
		builder.setCorpusType("CorpusType" );
		builder.setCorpusURI("CorpusURI" );
		builder.setDocumentID("DocumentID" + docNum);
		builder.setDocumentPublicationDate("2015-11-18");
		builder.setDocumentURI("DocumentURI" + docNum);
		builder.setSourceAlgorithmName("SourceAlgorithM");
		builder.setSourceLanguage("SourceLanguage");
		builder.setValue("ProvenanceValue"+index);
		return builder;
	}

	protected static KBEntityMentionProvenance.InsertionBuilder generateProvenance(int numProvenances,
			int index, String entityMentionType){
			checkNotNull(entityMentionType);
			KBTextProvenance.InsertionBuilder builder = generateProvenance
					(numProvenances,index);
			KBEntityMentionProvenance.InsertionBuilder entityMentionBuilder =
					new KBEntityMentionProvenance.InsertionBuilder(builder);
			entityMentionBuilder.setType(entityMentionType);
			return entityMentionBuilder;
	}

	private static class TimeInsertion implements Callable<Pair<KBID, Long>>{



		private String additionalNamePart;
		private KB kb;
		private String runName;
		private int numProvenances;

		/**
		 * @param i
		 * @param runName
		 * @param kb
		 * @param string
		 */
		public TimeInsertion(int i, String runName, KB kb, String string) {
			numProvenances = i;
			this.runName = runName;
			this.kb = kb;
			this.additionalNamePart = string;
		}

		@Override
		public Pair<KBID, Long> call() throws Exception {
			return timeInsertion(numProvenances, runName, kb, additionalNamePart);
		}

	}

}