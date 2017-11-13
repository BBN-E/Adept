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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides temporary instances used by the KB in retrieving ADEPT
 * objects from the KB.
 */
public class SourceDocumentsResult {
	private String ID;
	private String sourceLanguageId;
	private String corpus;
	private Date publicationDate;
	private String URI;

	// parses the current row in the passed ResultSet and parses it. Does not
	// move the cursor forward in the result set
	public SourceDocumentsResult(ResultSet resultSet) throws SQLException {
		if (resultSet.getRow() > 0) {
			ID = resultSet.getString("ID");
			sourceLanguageId = resultSet.getString("sourceLanguage");
			corpus = resultSet.getString("corpus");
			publicationDate = resultSet.getDate("publicationDate");
			URI = resultSet.getString("URI");
		}
	}

	public String getID() {
		return ID;
	}

	public String getSourceLanguageId() {
		return sourceLanguageId;
	}

	public String getCorpus() {
		return corpus;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public String getUri() {
		return URI;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SourceDocumentsResult)) {
			return false;
		}
		SourceDocumentsResult that = (SourceDocumentsResult) o;
		return that.ID.equals(this.ID)
				&& Objects.equal(that.sourceLanguageId, this.sourceLanguageId)
				&& Objects.equal(that.corpus, this.corpus)
				&& Objects.equal(that.publicationDate, this.publicationDate)
				&& Objects.equal(that.URI, this.URI);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(ID, sourceLanguageId, corpus, publicationDate, URI);
	}
}