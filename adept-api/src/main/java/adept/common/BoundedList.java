/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.common;

import java.util.ArrayList;
import java.util.Collection;

// TODO: Auto-generated Javadoc
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
