/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.kbapi.sql.objects;

import com.google.common.base.Objects;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides temporary instances used by the KB in retrieving ADEPT
 * objects from the KB.
 */
public class CorpusResult {
	private String ID;
	private String URI;
	private String type;
	private String name;

	// parses the current row in the passed ResultSet and parses it. Does not
	// move the cursor forward in the result set
	public CorpusResult(ResultSet resultSet) throws SQLException {
		if (resultSet.getRow() > 0) {
			ID = resultSet.getString("ID");
			URI = resultSet.getString("URI");
			type = resultSet.getString("type");
			name = resultSet.getString("name");
		}
	}

	public String getID() {
		return ID;
	}

	public String getURI() {
		return URI;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CorpusResult)) {
			return false;
		}
		CorpusResult that = (CorpusResult) o;
		return that.ID.equals(this.ID) && Objects.equal(that.URI, this.URI)
				&& Objects.equal(that.type, this.type) && Objects.equal(that.name, this.name);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(ID, URI, type, name);
	}
}
