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
public class SourceAlgorithmsResult {
	private String algorithmName;
	private String contributingSiteName;

	// parses the current row in the passed ResultSet and parses it. Does not
	// move the cursor forward in the result set
	public SourceAlgorithmsResult(ResultSet resultSet) throws SQLException {
		if (resultSet.getRow() > 0) {
			algorithmName = resultSet.getString("algorithmName");
			contributingSiteName = resultSet.getString("contributingSiteName");
		}
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public String getContributingSiteName() {
		return contributingSiteName;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SourceAlgorithmsResult)) {
			return false;
		}
		SourceAlgorithmsResult that = (SourceAlgorithmsResult) o;
		return Objects.equal(that.algorithmName, this.algorithmName)
				&& Objects.equal(that.contributingSiteName, this.contributingSiteName);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(algorithmName, contributingSiteName);
	}
}
