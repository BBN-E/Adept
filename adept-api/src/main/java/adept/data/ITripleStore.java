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
package adept.data;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface IKnowledgeBase.
 */
public interface ITripleStore {

	/**
	 * Gets the values.
	 * 
	 * @param entity
	 *            the entity
	 * @param slot
	 *            the slot
	 * @return the values
	 * @throws AdeptDataException
	 *             the adept data exception
	 */
	public List<IValue> getValues(IEntity entity, ISlot slot)
			throws AdeptDataException;

	/**
	 * Gets the slots.
	 * 
	 * @param entity
	 *            the entity
	 * @param value
	 *            the value
	 * @return the slots
	 * @throws AdeptDataException
	 *             the adept data exception
	 */
	public List<ISlot> getSlots(IEntity entity, IValue value)
			throws AdeptDataException;

	/**
	 * Gets the entities.
	 * 
	 * @param slot
	 *            the slot
	 * @param value
	 *            the value
	 * @return the entities
	 * @throws AdeptDataException
	 *             the adept data exception
	 */
	public List<IEntity> getEntities(ISlot slot, IValue value)
			throws AdeptDataException;
}
