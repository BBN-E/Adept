package adept.kbapi.sql.objects;

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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides temporary instances used by the KB in retrieving ADEPT
 * objects from the KB.
 */
public class EntityMentionsResult {
	private String ID;
	private String mentionType;
	private String KBEntityId;
	private float confidence;
	private String chunk;
	private String sourceAlgorithm;

	// parses the current row in the passed ResultSet and parses it. Does not
	// move the cursor forward in the result set
	public EntityMentionsResult(ResultSet resultSet) throws SQLException {
		if (resultSet.getRow() > 0) {
			ID = resultSet.getString("ID");
			mentionType = resultSet.getString("mentionType");
			KBEntityId = resultSet.getString("KBEntityId");
			confidence = resultSet.getFloat("confidence");
			chunk = resultSet.getString("chunk");
			sourceAlgorithm = resultSet.getString("sourceAlgorithm");
		}
	}

	public String getID() {
		return ID;
	}

	public String getMentionType() {
		return mentionType;
	}

	public String getKBEntityId() {
		return KBEntityId;
	}

	public float getConfidence() {
		return confidence;
	}

	public String getChunk() {
		return chunk;
	}

	public String getSourceAlgorithm() {
		return sourceAlgorithm;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof EntityMentionsResult)) {
			return false;
		}
		EntityMentionsResult that = (EntityMentionsResult) o;
		return that.ID.equals(this.ID) && Objects.equal(that.mentionType, this.mentionType)
				&& Objects.equal(that.KBEntityId, this.KBEntityId)
				&& that.confidence == this.confidence && Objects.equal(that.chunk, this.chunk)
				&& Objects.equal(that.sourceAlgorithm, this.sourceAlgorithm);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(ID, mentionType, KBEntityId, confidence, chunk, sourceAlgorithm);
	}
}
