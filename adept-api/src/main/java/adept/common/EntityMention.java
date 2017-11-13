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

package adept.common;

/*-
 * #%L
 * adept-api
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

import com.google.common.base.Optional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class EntityMention, which is a sequence of tokens
 * that refer to an Entity with mention type that is drawn
 * from an ontology
 */
public class EntityMention extends Chunk implements Serializable {
	private static final long serialVersionUID = -4843078720820591775L;

/** The mention type. */
  private IType mentionType;

  /** The entity type. */
  private IType entityType;

  /** The sequence id. */
  private final long sequenceId;

  /** Map containing document level entity IDs that this mention
   * resolves to, and associated confidences. */
  private Map<Long, Float> entityIdDistribution;

  /** The textual context. */
  protected Chunk context;

  /** The size of the textual context to be set. */
  protected int contextSize = 2;

  /** The doc id. */
  private String docId;

  /** The tokenizer type. */
  private TokenizerType tokenizerType;

  /** Additional attributes that don't come from a fixed Ontology */
  private Map< String, String > attributes;

  /**
   * TokenOffset for head of a mention
   */
  private TokenOffset headOffset;

  /**
   * Instantiates a new entity mention.
   *
   * @param sequenceId
   *            the sequence id
   * @param tokenOffset
   *            the token offset
   * @param tokenStream
   *            the token stream
   */
  public EntityMention(long sequenceId, TokenOffset tokenOffset,
      TokenStream tokenStream) {
    super(tokenOffset, tokenStream);
    this.sequenceId = sequenceId;
    this.docId = tokenStream.getDocument().getDocId();
    this.tokenizerType = tokenStream.getTokenizerType();
    this.entityIdDistribution = new HashMap<Long, Float>();
    this.attributes = new HashMap< String, String >();

    setContext();
  }

  /**
   * Sets the context.
   */
  protected void setContext()
  {
    int begin = Math.max(0,  getTokenOffset().getBegin()-contextSize);
    int end = Math.min(tokenStream.size()-1,getTokenOffset().getEnd() + contextSize);
    TokenOffset tokenOffset = new TokenOffset(begin,end);
    context = new Chunk(tokenOffset, tokenStream);
  }

  public Chunk getContext() {
    return context;
  }

  /**
   * Gets the entity id distribution.
   *
   * @return the entity id distribution
   */
  public Map<Long, Float> getEntityIdDistribution() {
    return entityIdDistribution;
  }

  /**
   * Sets the entity id distribution.
   *
   * @param entityIdDistribution
   *            the entity id distribution
   */
  public void setEntityIdDistribution(Map<Long, Float> entityIdDistribution) {
    checkArgument(entityIdDistribution!=null);
    this.entityIdDistribution = entityIdDistribution;
  }

  /**
   * Adds the entity confidence pair.
   *
   * @param entityId
   *            the entity id
   * @param confidence
   *            the confidence
   */
  public void addEntityConfidencePair(long entityId, float confidence) {
    this.entityIdDistribution.put(entityId, new Float(confidence));
  }

  /**
   * Gets the confidence.
   *
   * @param entityId
   *            the entity id
   * @return the confidence
   */
  public float getConfidence(long entityId) {
    return this.entityIdDistribution.get(entityId);
  }

  /**
   * Gets the mention type.
   *
   * @return the mention type
   */
  public IType getMentionType() {
    return mentionType;
  }

  /**
   * Sets the mention type.
   *
   * @param mentionType
   *            the new mention type
   */
  public void setMentionType(IType mentionType) {
    checkArgument(mentionType!=null);
    this.mentionType = mentionType;
  }

  /**
   * Gets the sequence id.
   *
   * @return the sequence id
   */
  public long getSequenceId() {
    return sequenceId;
  }

  /**
   * Gets the entity type.
   *
   * @return the entity type
   */
  public IType getEntityType() {
    return entityType;
  }

  /**
   * Sets the entity type.
   *
   * @param entityType
   *            the new entity type
   */
  public void setEntityType(IType entityType) {
    checkArgument(entityType!=null);
    this.entityType = entityType;
  }

  /**
   * Gets the best entity id.
   *
   * @return the best entity id. Returns -1 if empty.
   */
  public long getBestEntityId() {
    long entityId = -1;
    if (entityIdDistribution.size() == 1) {
      entityId = entityIdDistribution.entrySet().iterator().next().getKey();
    } else {
      float score = -1000f;
      for(Map.Entry<Long, Float> entrySet : entityIdDistribution.entrySet()) {
        if (entrySet.getValue() > score) {
          entityId = entrySet.getKey();
          score = entrySet.getValue();
        }
      }
    }
    return entityId;
  }

  /**
   * Sets the doc id.
   *
   * @param docId
   *            the new doc id
   */
  public void setDocId(String docId) {
    checkArgument(docId!=null && docId.trim().length()>0);
    this.docId = docId;
  }

  /**
   * Sets the doc id.
   *
   * @param tokenizerType the new tokenizer type
   */
  public void setTokenizerType(TokenizerType tokenizerType) {
    checkArgument(tokenizerType!=null);
    this.tokenizerType = tokenizerType;
  }

  /**
   * Gets the doc id.
   *
   * @return the doc id
   */
  public String getDocId() {
    return docId;
  }


  /**
   * Gets the tokenizer type.
   *
   * @return the tokenizer type
   */
  public TokenizerType getTokenizerType() {
    return tokenizerType;
  }

  /**
   * Set the TokenOffset for head of this mention
   */
  public void setHeadOffset(TokenOffset headOffset){
    this.headOffset = headOffset;
  }

  /**
   * Get the Optional TokenOffset for head of this mention
   */
  public Optional<TokenOffset> getHeadOffset(){
    return Optional.fromNullable(this.headOffset);
  }

  /**
   * Get the Optional head String for this mention
   */
  public Optional<String> getHead(){
    return Optional.fromNullable(this.headOffset==null?null:getValue(this.tokenStream,this.headOffset));
  }


  @Override
public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
	result = prime * result + ((context == null) ? 0 : context.hashCode());
	result = prime * result + contextSize;
	result = prime * result + ((docId == null) ? 0 : docId.hashCode());
	result = prime * result + ((entityIdDistribution == null) ? 0 : entityIdDistribution.hashCode());
	result = prime * result + ((entityType == null) ? 0 : entityType.hashCode());
	result = prime * result + ((mentionType == null) ? 0 : mentionType.hashCode());
	result = prime * result + (int) (sequenceId ^ (sequenceId >>> 32));
	result = prime * result + ((tokenizerType == null) ? 0 : tokenizerType.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (!super.equals(obj))
		return false;
	if (getClass() != obj.getClass())
		return false;
	EntityMention other = (EntityMention) obj;
	if (attributes == null) {
		if (other.attributes != null)
			return false;
	} else if (!attributes.equals(other.attributes))
		return false;
	if (context == null) {
		if (other.context != null)
			return false;
	} else if (!context.equals(other.context))
		return false;
	if (contextSize != other.contextSize)
		return false;
	if (docId == null) {
		if (other.docId != null)
			return false;
	} else if (!docId.equals(other.docId))
		return false;
	if (entityIdDistribution == null) {
		if (other.entityIdDistribution != null)
			return false;
	} else if (!entityIdDistribution.equals(other.entityIdDistribution))
		return false;
	if (entityType == null) {
		if (other.entityType != null)
			return false;
	} else if (!entityType.equals(other.entityType))
		return false;
	if (mentionType == null) {
		if (other.mentionType != null)
			return false;
	} else if (!mentionType.equals(other.mentionType))
		return false;
	if (sequenceId != other.sequenceId)
		return false;
	if (tokenizerType != other.tokenizerType)
		return false;
	return true;
}

/**
   * get mention attributes
   *
   * @return the attributes of this mention
   */ 
  public Map< String, String > getAttributes() {
    return attributes;
  }

  /**
   * set mention attributes
   *
   * @param attributes
   */

  public void setAttributes(Map< String, String > attributes) {
    checkArgument(attributes!=null);
    this.attributes = attributes;
  }

  /**
   * set mention attribute
   *
   * @param attribute
   * @param value
   */

  public void setAttribute(String attribute, String value)
  {
    checkArgument(attribute!=null && attribute.trim().length()>0);
    checkArgument(value!=null && value.trim().length()>0);
    this.attributes.put( attribute, value );
  }

  /**
   * get mention attribute
   *
   * @param attribute
   * @return
   */

  public String getAttribute( String attribute )
  {
    if(attributes.containsKey(attribute))
      return this.attributes.get(attribute);
    else return null;
  }

}