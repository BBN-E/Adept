package adept.kbapi;

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
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import java.util.HashSet;
import java.util.Set;

import adept.common.KBID;
import adept.common.TemporalResolution;
import adept.common.TimexValue;
import adept.common.XSDDate;

/**
 * Class for representing Dates in the KB.
 *
 * Currently this implementation supports date strings in either Timex2 or
 * XSDDate format. Both formats will be available if conversion is possible.
 *
 * Dates are intended to be canonical within the KB; only one copy of a
 * particular date should ever exist.
 *
 * @author dkolas
 */
public class KBDate extends KBThing {

	/**
	 * The timex2 representation of the date
	 */
	private final String timexString;

	/**
	 * The XSDDate representation of the date
	 */
	private final Optional<String> xsdDateString;

	/**
	 * @param kb
	 * @param kbID
	 * @param provenances
	 * @param timexString
	 * @param xsdDateString
	 */
	protected KBDate(KB kb, KBID kbID, Optional<Set<KBProvenance>> provenances, String timexString,
			Optional<String> xsdDateString) {
		super(kb, kbID, provenances, timexString);
		this.timexString = timexString;
		this.xsdDateString = xsdDateString;
	}

	/**
	 * Get the timex2 representation of the date. This should always be
	 * available.
	 *
	 * @return
	 */
	public String getTimexString() {
		return timexString;
	}

	/**
	 * Get the XSDDate representation of the date. This will be available if the
	 * date was created with an XSDDate representation, or if the timex2 value
	 * could be successfully converted.
	 *
	 * Otherwise this method will return {@link Optional#absent()}
	 *
	 * @return
	 */
	public Optional<String> xsdDateString() {
		return xsdDateString;
	}

	/**
	 * Create an insertionBuilder based on a Timex string.
	 *
	 * Provenances can then be added.
	 *
	 * @param timexString
	 * @return
	 */
	public static InsertionBuilder timexInsertionBuilder(String timexString) {
		Preconditions.checkNotNull(timexString);
		return new InsertionBuilder(timexString, timexToXSD(timexString));
	}

	/**
	 * Private method for converting timex2 strings to XSDDate strings
	 *
	 * @param timexString
	 * @return
	 */
	public static Optional<String> timexToXSD(String timexString) {

		if (timexString.matches("\\d{4}-\\d{2}-\\d{2}(Z|((\\+|\\-)\\d{2}:\\d{2}))?")) {
			return Optional.of(timexString);
		} else {
			return Optional.absent();
		}
	}

	/**
	 * Create an insertionBuilder based on an XSDDate string.
	 *
	 * Provenances can then be added.
	 *
	 * @param xsdDateString
	 * @return
	 */
	public static InsertionBuilder xsdDateInsertionBuilder(String xsdDateString) {
		Preconditions.checkNotNull(xsdDateString);
		return new InsertionBuilder(xsdToTimex(xsdDateString), Optional.of(xsdDateString));
	}

	/**
	 * Private method for converting XSDDate strings to Timex strings
	 *
	 * @param xsdDateString
	 * @return
	 */
	public static String xsdToTimex(String xsdDateString) {
		return xsdDateString;
	}

	/**
	 * Create an insertion builder from a {@link TimexValue}.
	 *
	 * Any temporal resolutions in the provided list that are linked to the
	 * provided timexValue are used to add provenances to the builder.
	 *
	 * @param timexValue
	 * @param temporalResolutions
	 * @return the new InsertionBuilder
	 */
	public static InsertionBuilder timexInsertionBuilder(TimexValue timexValue,
			Set<TemporalResolution> temporalResolutions) {
		Preconditions.checkNotNull(timexValue);
		Preconditions.checkNotNull(temporalResolutions);
		InsertionBuilder builder = new InsertionBuilder(timexValue.asString(),
				timexToXSD(timexValue.asString()));
		for (TemporalResolution temporalResolution : temporalResolutions) {
			if (temporalResolution.getResolvedTemporalValue().equals(timexValue)) {
				builder.addProvenance(KBTextProvenance.builder(temporalResolution.getTimePhrase(),
						temporalResolution.getConfidence()));
			}
		}
		return builder;
	}

	/**
	 * Create an insertion builder from a {@link XSDDate}.
	 *
	 * Any temporal resolutions in the provided list that are linked to the
	 * provided xsdDate are used to add provenances to the builder.
	 *
	 * @param xsdDate
	 * @param temporalResolutions
	 * @return
	 */
	public static InsertionBuilder xsdDateInsertionBuilder(XSDDate xsdDate,
			Set<TemporalResolution> temporalResolutions) {
		Preconditions.checkNotNull(xsdDate);
		Preconditions.checkNotNull(temporalResolutions);
		InsertionBuilder builder = new InsertionBuilder(xsdToTimex(xsdDate.asString()),
				Optional.of(xsdDate.asString()));
		for (TemporalResolution temporalResolution : temporalResolutions) {
			if (temporalResolution.getResolvedTemporalValue().equals(xsdDate)) {
				builder.addProvenance(KBTextProvenance.builder(temporalResolution.getTimePhrase(),
						temporalResolution.getConfidence()));
			}
		}
		return builder;
	}

	/**
	 * Class for inserting KBDates into the KB.
	 *
	 * @author dkolas
	 */
	public static class InsertionBuilder extends
			KBPredicateArgument.InsertionBuilder<InsertionBuilder, KBDate> {

		private final String timexString;
		private final Optional<String> xsdDateString;

		public String getTimexDate() {
			return timexString;
		}

		public Optional<String> getXSDDate() {
			return xsdDateString;
		}

		/**
		 * Private constructor for the builder, to be called only by
		 * insertionBuilder methods.
		 *
		 * @param timexString
		 * @param xsdDateString
		 */
		private InsertionBuilder(String timexString, Optional<String> xsdDateString) {
			super();
			this.timexString = timexString;
			this.xsdDateString = xsdDateString;
		}

		/**
		 * Method to insert the contents of this builder into the KB.
		 *
		 * @param kb
		 * @return
		 *
		 */
		@Override
		public KBDate insert(KB kb) throws KBUpdateException {
			kb.insertDateValue(this);
			return build(kb, false);
		}

		protected KBDate build(KB kb, boolean deferProvenances) {
			Optional<Set<KBProvenance>> provenances = buildProvenances(deferProvenances);
			return new KBDate(kb, kbid, provenances, timexString, xsdDateString);
		}

		/**
		 *
		 * @return
		 *
		 * @see adept.kbapi.KBPredicateArgument.InsertionBuilder#me()
		 */
		@Override
		protected InsertionBuilder me() {
			return this;
		}
	}

	/**
	 * Get an update builder for this object.
	 *
	 * Only provenances are updatable.
	 *
	 * @return
	 *
	 */
	@Override
	public UpdateBuilder updateBuilder() {
		return new UpdateBuilder();
	}

	/**
	 * Class for updating a KBDate.
	 *
	 * Only provenances are updatable.
	 *
	 * @author dkolas
	 */
	public class UpdateBuilder extends KBPredicateArgument.UpdateBuilder<UpdateBuilder, KBDate> {

		@Override
		public KBDate update(KB kb) throws KBUpdateException {

			Set<KBProvenance> oldProvenances = null;
			try{
				oldProvenances = getProvenances();
				checkProvenancesToRemove();
			}catch(KBQueryException e){
				throw new KBUpdateException("Could not load provenances for original object",e);
			}
			kb.updateKBPredicateArgumentProvenances(this);
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
			for (KBProvenance kbProvenance : oldProvenances) {
				if (!getProvenancesToRemove().contains(kbProvenance)) {
					provenances.add(kbProvenance);
				}
			}
			return new KBDate(kb, getKBID(), Optional.of(provenances), getTimexString(),
					xsdDateString());
		}

		/**
		 *
		 * @return
		 *
		 * @see adept.kbapi.KBPredicateArgument.UpdateBuilder#me()
		 */
		@Override
		protected UpdateBuilder me() {
			return this;
		}

	}

	@Override
	public boolean equals(Object o) {
		if (null == o || (getClass() != o.getClass())) {
			return false;
		}
		KBDate that = (KBDate) o;
		return super.equals(that) && Objects.equal(that.timexString, this.timexString)
				&& Objects.equal(that.xsdDateString, this.xsdDateString);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), timexString, xsdDateString);
	}
}
