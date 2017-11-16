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
public class TextProvenanceResult {
	private String ID;
	private String sourceDocument;
	private String value;
	private String serializedTokenStream;
	private int beginOffset;
	private int endOffset;

	// parses the current row in the passed ResultSet and parses it. Does not
	// move the cursor forward in the result set
	public TextProvenanceResult(ResultSet resultSet) throws SQLException {
		if (resultSet.getRow() > 0) {
			ID = resultSet.getString("ID");
			sourceDocument = resultSet.getString("sourceDocument");
			value = resultSet.getString("value");
			serializedTokenStream = resultSet.getString("serializedTokenStream");
			beginOffset = resultSet.getInt("beginOffset");
			endOffset = resultSet.getInt("endOffset");
		}
	}

	public String getID() {
		return ID;
	}

	public String getSourceDocument() {
		return sourceDocument;
	}

	public String getValue() {
		return value;
	}

	public String getSerializedTokenStream() {
		return serializedTokenStream;
	}

	public int getBeginOffset() {
		return beginOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TextProvenanceResult)) {
			return false;
		}
		TextProvenanceResult that = (TextProvenanceResult) o;
		return that.ID.equals(this.ID) && Objects.equal(that.sourceDocument, this.sourceDocument)
				&& Objects.equal(that.value, this.value)
				&& Objects.equal(that.serializedTokenStream, this.serializedTokenStream)
				&& that.beginOffset == this.beginOffset && that.endOffset == this.endOffset;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(ID, sourceDocument, value, serializedTokenStream, beginOffset,
				endOffset);
	}
}
