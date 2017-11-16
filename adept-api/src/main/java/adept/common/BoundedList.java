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

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * An extension class of ArrayList which has a fixed size/capacity. Once the
 * list is full, no new elements can be added to the list.
 * 
 * @param <E>
 *            the element type
 */
public class BoundedList<E> extends ArrayList<E> {

	/** The max size. */
	private final int maxSize;

	/**
	 * Instantiates a new bounded list.
	 * 
	 * @param maxSize
	 *            the max size
	 */
	public BoundedList(int maxSize) {
            checkArgument((maxSize > 0),
                    "A BoundedList's maxSize must be greater than 0, "
                    +"but got maxSize %s",
                    maxSize);
		this.maxSize = maxSize;
	}

	/**
	 * Gets the max size.
	 * 
	 * @return the max size
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		if (super.size() < this.maxSize) {
			super.add(e);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		if ((super.size() + c.size()) < this.maxSize) {
			super.addAll(c);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#addAll(int, java.util.Collection)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if ((super.size() + c.size()) < this.maxSize) {
			super.addAll(index, c);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#ensureCapacity(int)
	 */
	@Override
	public void ensureCapacity(int minCapacity) {
		// TODO Auto-generated method stub
		super.ensureCapacity(minCapacity);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

}
