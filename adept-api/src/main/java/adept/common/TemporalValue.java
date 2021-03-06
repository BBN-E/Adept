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

/**
 * Represents a temporal value, abstracted from its particular realization in text.
 * This is placeholder which may be elaborated in the future.  Implementations are strongly
 * urged to be immutable.  Implementations are only required to implement {@code hashCode()}
 * and {@code equals(Object)} in terms of the structure of that sort of temporal value and are
 * not required to attempt to determine which descriptions refer to equivalent real-world values.
 */
public interface TemporalValue{
    /**
     * @return May never be null.
     */
    public String asString();
}
