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
public class NumberMentionsResult {
	private String ID;
	private String kbNumberId;
	private String chunk;

	// parses the current row in the passed ResultSet and parses it. Does not
	// move the cursor forward in the result set
	public NumberMentionsResult(ResultSet resultSet) throws SQLException {
		if (resultSet.getRow() > 0) {
			ID = resultSet.getString("ID");
			kbNumberId = resultSet.getString("KBNumberId");
			chunk = resultSet.getString("chunk");
		}
	}

	public String getID() {
		return ID;
	}

	public String getKBNumberId() {
		return kbNumberId;
	}

	public String getChunk() {
		return chunk;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof NumberMentionsResult)) {
			return false;
		}
		NumberMentionsResult that = (NumberMentionsResult) o;
		return that.ID.equals(this.ID) && Objects.equal(that.kbNumberId, this.kbNumberId)
				&& Objects.equal(that.chunk, this.chunk);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(ID, kbNumberId, chunk);
	}
}
