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

import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents the output of a a 2014 TAC KBP event argument system on a document.
 */
public final class TACKBP2014EAOutput {

    private final String documentId;
    private final ImmutableMap<TACKBP2014EventArgument, Float> scoredResponses;

    private TACKBP2014EAOutput(String documentId,
                               Map<TACKBP2014EventArgument, Float> scoredResponses) {
        this.documentId = documentId;
        this.scoredResponses = ImmutableMap.copyOf(scoredResponses);
    }

    /**
     * @param documentId      May not be {@code null}.
     * @param scoredResponses May not have {@code null} keys or values. If any response has
     *                        a {@link TACKBP2014EventArgument#getDocumentID()} that does not
     *                        match {@code documentId}, an {@link java.lang.IllegalArgumentException}
     *                        will be thrown.
     * @return
     */
    public static TACKBP2014EAOutput create(String documentId,
                                            Map<TACKBP2014EventArgument, Float> scoredResponses) {
        checkArgument(!Strings.isNullOrEmpty(documentId));        
        checkArgument(scoredResponses!=null);
        for( Map.Entry<TACKBP2014EventArgument, Float> entry : scoredResponses.entrySet()) {
            checkArgument(entry.getKey()!=null);
            checkArgument(entry.getValue()!=null);
            checkArgument(documentId.equals(entry.getKey().getDocumentID()));
        }
        return new TACKBP2014EAOutput(documentId, scoredResponses);
    }

    /**
     * @return Will never be {@code null}.
     */
    public String getDocumentID() {
        return documentId;
    }

    public ImmutableMap<TACKBP2014EventArgument, Float> getScoredResponses() {
        return scoredResponses;
    }
}