/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
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

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This represents event arguments produced by a TAC KBP 2014 Event Argument
 * evaluation system.  Please see <a href="http://www.nist.gov/tac/2014/KBP/Event/index.html">
 * the evaluation site</a> for more details.  This class is primarily for loading
 * the output of non-ADEPT systems.  ADEPT-based event argument systems
 * should prefer to populate {@link adept.common.DocumentEventArgument}s directly.
 * <p/>
 * <p></p>No fields will ever be null or contain null.</p>
 */
public final class TACKBP2014EventArgument {

    /**
     * This represents the realis distinctions from the 2014 TAC KBP
     * event argument evaluation. Do not use it as a general realis type.
     */
    public enum Realis {
        ACTUAL, GENERIC, OTHER
    }

    private final String documentID;
    private final IType eventType;
    private final IType argumentRole;
    private final Realis realis;
    private final TokenOffset canonicalArgumentChunk;
    private final String canonicalArgumentString;
    private final TokenOffset baseFiller;
    private final ImmutableSet<TokenOffset> predicateJustifications;
    private final ImmutableSet<TokenOffset> additionalArgumentJustifications;

    private TACKBP2014EventArgument(String documentID,
                                    IType eventType,
                                    IType argumentRole,
                                    Realis realis,
                                    TokenOffset canonicalArgumentChunk,
                                    String canonicalArgumentString,
                                    TokenOffset baseFiller,
                                    Iterable<TokenOffset> predicateJustifications,
                                    Iterable<TokenOffset> additionalArgumentJustifications) {
        this.documentID = documentID;
        this.eventType = eventType;
        this.argumentRole = argumentRole;
        this.realis = realis;
        this.canonicalArgumentChunk = canonicalArgumentChunk;
        this.canonicalArgumentString = canonicalArgumentString;
        this.baseFiller = baseFiller;
        this.predicateJustifications = ImmutableSet.copyOf(predicateJustifications);
        this.additionalArgumentJustifications = ImmutableSet.copyOf(additionalArgumentJustifications);
    }

    /**
     * Creates a {@code TACK2014EventArgument}. No arguments may be null or contain null. All
     * {@link adept.common.TokenOffset} arguments are in terms of character offsets.
     *
     * @param predicateJustifications May not be empty.
     */
    public static TACKBP2014EventArgument create(String documentID,
                                                 IType eventType,
                                                 IType argumentRole,
                                                 Realis realis,
                                                 TokenOffset canonicalArgumentChunk,
                                                 String canonicalArgumentString,
                                                 TokenOffset baseFiller,
                                                 Iterable<TokenOffset> predicateJustifications,
                                                 Iterable<TokenOffset> additionalArgumentJustifications) {

        checkArgument(!Strings.isNullOrEmpty(documentID));
        checkArgument(eventType!=null);
        checkArgument(argumentRole!=null);
        checkArgument(realis!=null);
        checkArgument(canonicalArgumentChunk!=null);
        checkArgument(!Strings.isNullOrEmpty(canonicalArgumentString));
        checkArgument(baseFiller!=null);
        checkArgument(predicateJustifications!=null);
        for( TokenOffset arg : predicateJustifications) {
            checkArgument(arg!=null);
        }
        checkArgument(additionalArgumentJustifications!=null);
        for( TokenOffset arg : additionalArgumentJustifications) {
            checkArgument(arg!=null);
        }

        return new TACKBP2014EventArgument(documentID,
                eventType,
                argumentRole,
                realis,
                canonicalArgumentChunk,
                canonicalArgumentString,
                baseFiller,
                predicateJustifications,
                additionalArgumentJustifications);
    }

    /**
     * Gets the source document for the event argument.
     *
     * @return Will never be {@code null}.
     */
    public String getDocumentID() {
        return documentID;
    }

    /**
     * @return Will never be {@code null}.
     */
    public IType getEventType() {
        return eventType;
    }

    /**
     * @return Will never be {@code null}.
     */
    public IType getArgumentRole() {
        return argumentRole;
    }

    /**
     * The character offsets of the the local string representing the event argument.
     *
     * @return Will never be {@code null}.
     */
    public TokenOffset getBaseFiller() {
        return baseFiller;
    }

    /**
     * @return Will never be {@code null}.
     */
    public Realis getRealis() {
        return realis;
    }

    /**
     * This is the character offsets of the canonical argument string, which may
     * be a more representative string for the argument entity than the base filler.
     *
     * @return Will never be {@code null}.
     */
    public TokenOffset getCanonicalArgumentChunk() {
        return canonicalArgumentChunk;
    }

    /**
     * Gets the string value of the canonical argument string. This may differ from the text
     * at the specified character offsets due to normalization, time-resolution, etc.
     *
     * @return Will never be @{code null}.
     */
    public String getCanonicalArgumentString() {
        return canonicalArgumentString;
    }

    /**
     * Gets the character offsets of the text spans which justify that the base filler fills
     * the specified argument role of an event of the specified type.  The base filler
     * will almost always be contained within one of these strings, but this is not
     * strictly required. Will never be empty.
     */
    public ImmutableSet<TokenOffset> getPredicateJustifications() {
        return (ImmutableSet<TokenOffset>) predicateJustifications;
    }

    /**
     * Gets the character offsets of the text spans which justify the claim that, given the
     * the base filler occupies the specified argument slot, so does the CAS.  Evidence of
     * coreference is excluded, so typically this will be empty. It will be filled in cases
     * of e.g. membership inference.
     *
     * @return
     */
    public ImmutableSet<TokenOffset> getAdditionalArgumentJustifications() {
        return (ImmutableSet<TokenOffset>) additionalArgumentJustifications;
    }
}