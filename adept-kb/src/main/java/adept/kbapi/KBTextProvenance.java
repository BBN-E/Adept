package adept.kbapi;

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

import com.google.common.base.Objects;
import com.google.common.collect.FluentIterable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import adept.common.Chunk;
import adept.common.KBID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A textual provenance for a KB Object.
 *
 * May involve multiple chunks of text.
 *
 * This object is not first-class in the KB, and may only be inserted or removed
 * as part of the insertion or removal of a first-class object which owns it.
 *
 *
 * @author dkolas
 */
public class KBTextProvenance extends KBProvenance {

	protected final float confidence;
	protected final String sourceAlgorithmName;
	protected final String contributingSiteName;
	protected final String value;
	protected final int beginOffset;
	protected final int endOffset;
	protected final String documentID;
	protected final String documentURI;
	protected final String documentPublicationDate;
	protected final String sourceLanguage;
	protected final String corpusType;
	protected final String corpusName;
	protected final String corpusURI;
	protected final String corpusID;

	protected KBTextProvenance(KBID kbID, float confidence, String sourceAlgorithmName,
			String contributingSiteName, String value, int beginOffset, int endOffset,
			String documentID, String documentURI, String documentPublicationDate,
			String sourceLanguage, String corpusType, String corpusName, String corpusURI,
			String corpusID) {
		super(kbID);
		this.confidence = confidence;
		this.sourceAlgorithmName = sourceAlgorithmName;
		this.contributingSiteName = contributingSiteName;
		this.value = value;
		this.beginOffset = beginOffset;
		this.endOffset = endOffset;
		this.documentID = documentID;
		this.documentURI = documentURI;
		this.documentPublicationDate = documentPublicationDate;
		this.sourceLanguage = sourceLanguage;
		this.corpusType = corpusType;
		this.corpusName = corpusName;
		this.corpusURI = corpusURI;
		this.corpusID = corpusID;
	}

	/**
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * @return the sourceAlgorithmName
	 */
	public String getSourceAlgorithmName() {
		return sourceAlgorithmName;
	}

	/**
	 * @return the contributingSiteName
	 */
	public String getContributingSiteName() {
		return contributingSiteName;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the beginOffset
	 */
	public int getBeginOffset() {
		return beginOffset;
	}

	/**
	 * @return the endOffset
	 */
	public int getEndOffset() {
		return endOffset;
	}

	/**
	 * @return the documentURI
	 */
	public String getDocumentURI() {
		return documentURI;
	}

	/**
	 * @return
	 */
	public String getDocumentID() {
		return documentID;
	}

	public String getCorpusID() {
		return corpusID;
	}

	/**
	 * @return the documentPublicationDate
	 */
	public String getDocumentPublicationDate() {
		return documentPublicationDate;
	}

	/**
	 * @return the sourceLanguage
	 */
	public String getSourceLanguage() {
		return sourceLanguage;
	}

	/**
	 * @return the corpusType
	 */
	public String getCorpusType() {
		return corpusType;
	}

	/**
	 * @return the corpusName
	 */
	public String getCorpusName() {
		return corpusName;
	}

	/**
	 * @return the corpusURI
	 */
	public String getCorpusURI() {
		return corpusURI;
	}

	/**
	 * @param chunk
	 * @param confidence
	 * @return
	 */
	public static InsertionBuilder builder(Chunk chunk, float confidence) {
		return new InsertionBuilder(chunk, confidence);
	}

	public static InsertionBuilder builder() {
		return new InsertionBuilder();
	}

	@Override
	public KBProvenance.InsertionBuilder modifiedCopyInsertionBuilder() {
		// note that the corresponding method in KBEntityMentionProvenance will need updates if this changes
		final KBTextProvenance.InsertionBuilder insertionBuilder = builder();
		insertionBuilder.setConfidence(confidence);
		insertionBuilder.setSourceAlgorithmName(sourceAlgorithmName);
		insertionBuilder.setContributingSiteName(contributingSiteName);
		insertionBuilder.setValue(value);
		insertionBuilder.setBeginOffset(beginOffset);
		insertionBuilder.setEndOffset(endOffset);
		insertionBuilder.setDocumentURI(documentURI);
		insertionBuilder.setDocumentID(documentID);
		insertionBuilder.setDocumentPublicationDate(documentPublicationDate);
		insertionBuilder.setSourceLanguage(sourceLanguage);
		insertionBuilder.setCorpusType(corpusType);
		insertionBuilder.setCorpusName(corpusName);
		insertionBuilder.setCorpusURI(corpusURI);
		insertionBuilder.setCorpusID(corpusID);
		return insertionBuilder;
	}


	public static class InsertionBuilder extends KBProvenance.InsertionBuilder {
		protected float confidence;
		protected String sourceAlgorithmName;
		protected String contributingSiteName;
		protected String value;
		protected int beginOffset;
		protected int endOffset;
		protected String documentURI;
		protected String documentID;
		protected String documentPublicationDate;
		protected String sourceLanguage;
		protected String corpusType;
		protected String corpusName;
		protected String corpusURI;
		protected String corpusID;
		protected String chunkId;

		public InsertionBuilder(Chunk chunk, float confidence) {
			checkNotNull(chunk);
			this.confidence = confidence;
			if (chunk.getSourceAlgorithm() != null) {
				this.sourceAlgorithmName = chunk.getSourceAlgorithm().getAlgorithmName();
				this.contributingSiteName = chunk.getSourceAlgorithm().getContributingSiteName();
			}
			this.value = chunk.getValue();
			this.beginOffset = chunk.getCharOffset().getBegin();
			this.endOffset = chunk.getCharOffset().getEnd();
			this.documentURI = chunk.getTokenStream().getDocument().getUri();
			this.documentID = chunk.getTokenStream().getDocument().getDocId();
			this.documentPublicationDate = chunk.getTokenStream().getDocument()
					.getPublicationDate();
			this.sourceLanguage = chunk.getTokenStream().getDocument().getSourceLanguage();
			if (chunk.getTokenStream().getDocument().getCorpus() != null) {
				this.corpusType = chunk.getTokenStream().getDocument().getCorpus().getType();
				this.corpusName = chunk.getTokenStream().getDocument().getCorpus().getName();
				this.corpusURI = chunk.getTokenStream().getDocument().getCorpus().getUri();
				this.corpusID = chunk.getTokenStream().getDocument().getCorpus().getCorpusId();
			}
			this.confidence = confidence;
		}

		public InsertionBuilder() {

		}

		protected KBTextProvenance build() {
			checkNotNull(kbid);
			return new KBTextProvenance(kbid, confidence, sourceAlgorithmName,
					contributingSiteName, value, beginOffset, endOffset, documentID, documentURI,
					documentPublicationDate, sourceLanguage, corpusType, corpusName, corpusURI,
					corpusID);
		}

		/**
		 * @return the confidence
		 */
		public float getConfidence() {
			return confidence;
		}

		/**
		 * @param confidence
		 *            the confidence to set
		 */
		public void setConfidence(float confidence) {
			this.confidence = confidence;
		}

		/**
		 * @return the sourceAlgorithmName
		 */
		public String getSourceAlgorithmName() {
			return sourceAlgorithmName;
		}

		/**
		 * @param sourceAlgorithmName
		 *            the sourceAlgorithmName to set
		 */
		public void setSourceAlgorithmName(String sourceAlgorithmName) {
			this.sourceAlgorithmName = sourceAlgorithmName;
		}

		/**
		 * @return the contributingSiteName
		 */
		public String getContributingSiteName() {
			return contributingSiteName;
		}

		/**
		 * @param contributingSiteName
		 *            the contributingSiteName to set
		 */
		public void setContributingSiteName(String contributingSiteName) {
			this.contributingSiteName = contributingSiteName;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value
		 *            the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * @return the beginOffset
		 */
		public int getBeginOffset() {
			return beginOffset;
		}

		/**
		 * @param beginOffset
		 *            the beginOffset to set
		 */
		public InsertionBuilder setBeginOffset(int beginOffset) {
			this.beginOffset = beginOffset;
			return this;
		}

		/**
		 * @return the endOffset
		 */
		public int getEndOffset() {
			return endOffset;
		}

		/**
		 * @param endOffset
		 *            the endOffset to set
		 */
		public InsertionBuilder setEndOffset(int endOffset) {
			this.endOffset = endOffset;
			return this;
		}

		/**
		 * @return the documentURI
		 */
		public String getDocumentURI() {
			return documentURI;
		}

		/**
		 * @param documentURI
		 *            the documentURI to set
		 */
		public InsertionBuilder setDocumentURI(String documentURI) {
			this.documentURI = documentURI;
			return this;
		}

		/**
		 * @return the documentID
		 */
		public String getDocumentID() {
			return documentID;
		}

		/**
		 * @param documentID
		 *            the documentID to set
		 */
		public InsertionBuilder setDocumentID(String documentID) {
			this.documentID = documentID;
			return this;
		}

		/**
		 * @return the documentPublicationDate
		 */
		public String getDocumentPublicationDate() {
			return documentPublicationDate;
		}

		/**
		 * @param documentPublicationDate
		 *            the documentPublicationDate to set
		 */
		public InsertionBuilder setDocumentPublicationDate(String documentPublicationDate) {
			this.documentPublicationDate = documentPublicationDate;
			return this;
		}

		/**
		 * @return the sourceLanguage
		 */
		public String getSourceLanguage() {
			return sourceLanguage;
		}

		/**
		 * @param sourceLanguage
		 *            the sourceLanguage to set
		 */
		public InsertionBuilder setSourceLanguage(String sourceLanguage) {
			this.sourceLanguage = sourceLanguage;
			return this;
		}

		/**
		 * @return the corpusType
		 */
		public String getCorpusType() {
			return corpusType;
		}

		/**
		 * @param corpusType
		 *            the corpusType to set
		 */
		public InsertionBuilder setCorpusType(String corpusType) {
			this.corpusType = corpusType;
			return this;
		}

		/**
		 * @return the corpusName
		 */
		public String getCorpusName() {
			return corpusName;
		}

		/**
		 * @param corpusName
		 *            the corpusName to set
		 */
		public InsertionBuilder setCorpusName(String corpusName) {
			this.corpusName = corpusName;
			return this;
		}

		/**
		 * @return the corpusURI
		 */
		public String getCorpusURI() {
			return corpusURI;
		}

		/**
		 * @param corpusURI
		 *            the corpusURI to set
		 */
		public InsertionBuilder setCorpusURI(String corpusURI) {
			this.corpusURI = corpusURI;
			return this;
		}

		/**
		 * @return the corpusID
		 */
		public String getCorpusID() {
			return corpusID;
		}

		/**
		 * @param corpusID
		 *            the corpusID to set
		 */
		public InsertionBuilder setCorpusID(String corpusID) {
			this.corpusID = corpusID;
			return this;
		}

		public String getChunkId() {
			if (chunkId == null) {
				chunkId = UUID.randomUUID().toString();
			}
			return chunkId;
		}
	}

  	public UpdateBuilder getUpdateBuilder(KBID sourceEntityKBID){
		return new UpdateBuilder(this.getKBID(),sourceEntityKBID);
	}

	/**
	 * Allows only the update of the KBID of the parent Entity of this KBTextProvenance. Think of this class as more of a ReassignSourceEntityBuilder
	 *
	 */
  	public class UpdateBuilder extends KBProvenance.UpdateBuilder{

	  KBID sourceEntityKBID;

	  protected UpdateBuilder(KBID provenanceKBID, KBID sourceEntityKBID){
	    setKBID(provenanceKBID);
	    checkNotNull(sourceEntityKBID);
	    this.sourceEntityKBID = sourceEntityKBID;
	  }

	  protected KBTextProvenance build() {
	    return new KBTextProvenance(getKBID(), confidence, sourceAlgorithmName,
		contributingSiteName, value, beginOffset, endOffset, documentID, documentURI,
		documentPublicationDate, sourceLanguage, corpusType, corpusName, corpusURI,
		corpusID);
	  }

	  protected void update(KB kb){
		//TODO: add update implementation in KB.java

	  }
		/**
		 * @return the KBID of entity that has this provenance
		 */
		public KBID getSourceEntityKBID() {
			return sourceEntityKBID;
		}

		/**
		 * @param kbid
		 *            the new KBID of the source entity to replace current one with
		 */
		public void setSourceEntityKBID(KBID kbid) {
			this.sourceEntityKBID = checkNotNull(kbid);
		}



		public String getDocumentID() {
			return documentID;
		}
  	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KBTextProvenance)) {
			return false;
		}
		KBTextProvenance that = (KBTextProvenance) o;
		return super.equals(that) && that.confidence == this.confidence
				&& Objects.equal(that.sourceAlgorithmName, this.sourceAlgorithmName)
				&& Objects.equal(that.contributingSiteName, this.contributingSiteName)
				&& Objects.equal(that.value, this.value) && that.beginOffset == this.beginOffset
				&& that.endOffset == this.endOffset
				&& Objects.equal(that.documentID, this.documentID)
				&& Objects.equal(that.documentURI, this.documentURI)
				&& Objects.equal(that.documentPublicationDate, this.documentPublicationDate)
				&& Objects.equal(that.sourceLanguage, this.sourceLanguage)
				&& Objects.equal(that.corpusType, this.corpusType)
				&& Objects.equal(that.corpusName, this.corpusName)
				&& Objects.equal(that.corpusURI, this.corpusURI)
				&& Objects.equal(that.corpusID, this.corpusID);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), confidence, sourceAlgorithmName,
				contributingSiteName, value, beginOffset, endOffset, documentID, documentURI,
				documentPublicationDate, sourceLanguage, corpusType, corpusName, corpusURI,
				corpusID);
	}

	public static Set<String> getDocumentIDsFromProvenances
			(Iterable<KBProvenance> provenances) {
  		checkNotNull(provenances);
		Set<String> documentIDs = new HashSet(FluentIterable.from
				(provenances).transform(
				(KBProvenance provenance) ->
						((KBTextProvenance)provenance)
								.getDocumentID())
				.toList());
		return documentIDs;

	}
}
