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
public class ArgumentsResult {
	private String ID;
	private String entityMentionTarget;
	private String numberMention;
	private float confidence;
	private String relationMention;
	private String argType;
	private String argumentTarget;
	private String relationMentionTarget;
	private Integer strength;

	// parses the current row in the passed ResultSet and parses it. Does not
	// move the cursor forward in the result set
	public ArgumentsResult(ResultSet resultSet) throws SQLException {
		if (resultSet.getRow() > 0) {
			ID = resultSet.getString("ID");
			confidence = resultSet.getFloat("confidence");
			entityMentionTarget = resultSet.getString("entityMentionTarget");
			relationMention = resultSet.getString("relationMention");
			argType = resultSet.getString("argType");
			numberMention = resultSet.getString("numberMentionTarget");
			argumentTarget = resultSet.getString("argumentTarget");
			relationMentionTarget = resultSet.getString("relationMentionTarget");
			strength = resultSet.getInt("strength");
		}
	}

	public String getID() {
		return ID;
	}

	public float getConfidence() {
		return confidence;
	}

	public String getEntityMention() {
		return entityMentionTarget;
	}

	public String getRelationMention() {
		return relationMention;
	}

	public String getArgType() {
		return argType;
	}

	public String getNumberMention() {
		return numberMention;
	}

	public String getArgumentTarget() {
		return argumentTarget;
	}

	public String getRelationMentionTarget() {
		return relationMentionTarget;
	}

	public Integer getStrength() {
		return strength;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ArgumentsResult)) {
			return false;
		}
		ArgumentsResult that = (ArgumentsResult) o;
		return that.ID.equals(this.ID)
				&& Objects.equal(that.entityMentionTarget, this.entityMentionTarget)
				&& Objects.equal(that.numberMention, this.numberMention)
				&& that.confidence == this.confidence
				&& Objects.equal(that.relationMention, this.relationMention)
				&& Objects.equal(that.argType, this.argType)
				&& Objects.equal(that.argumentTarget, this.argumentTarget)
				&& Objects.equal(that.relationMentionTarget, this.relationMentionTarget)
				&& Objects.equal(that.strength, this.strength);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(ID, entityMentionTarget, numberMention, confidence,
				relationMention, argType, argumentTarget, relationMentionTarget, strength);
	}
}
