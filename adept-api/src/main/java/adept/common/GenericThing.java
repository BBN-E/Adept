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

import com.google.common.base.Preconditions;
import java.io.Serializable;

/**
 * Represents a generic thing, abstracted from its particular realization in text.
 *
 * This generic thing is intended to represent possibly coreferenced fillers
 * for event slots which are not entities, dates, or numbers (ex: crimes, money, etc.)
 *
 *
 */
public class GenericThing extends Item implements Serializable {

	private static final long serialVersionUID = -7936322902434709455L;
	private final IType type;


	public GenericThing(IType type, String value){
		super();
		Preconditions.checkNotNull(type);
		Preconditions.checkNotNull(value);
		this.value = value;
		this.type = type;
	}

    public IType getType(){
    	return type;
    }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final GenericThing that = (GenericThing) o;

    if(!type.equals(that.type)){
      return false;
    }

    return this.value.equals(that.value);

  }

  @Override
  public int hashCode() {
    int result = type.hashCode();
    result = 31 * result + value.hashCode();
    return result;
  }
}
