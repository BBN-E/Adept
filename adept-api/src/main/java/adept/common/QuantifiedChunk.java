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

/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/

package adept.common;


import java.util.Map;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.io.IOException;

/* 
 * Class to hold measurements of any kind including ratios,
 * ranges, date and time values, and any other numeric 
 * measurements characterized by values, bounds and units.
 */
public class QuantifiedChunk extends Chunk implements HasChunkAttributes, HasOntologizedAttributes
{
	/** For holding measurement type: length, time, ratio etc.  */
	private Map<IType,IType> ontologizedAttributes = new HashMap<IType,IType>();

	
	/** For example, Ratio may have attributes such as numerator, denominator
	 * and numerator and denominator bounds, while a Range may have upper and lower 
	 * bounds. They may both have a time of measurement.  */
	private Map<IType,Chunk> chunkAttributes = new HashMap<IType,Chunk>();
		
	/**
	 * Instantiates a QuantifiedChunk.
	 */
	public QuantifiedChunk(TokenOffset tokenOffset, TokenStream tokenStream)
	{
		super(tokenOffset, tokenStream);
	}
	
	
	/**
	 * Gets the chunk attributes map.
	 * 
	 * @return the chunkAttributes map.
	 */
	public Map<IType,Chunk> getChunkAttributes()
	{
		return chunkAttributes;
	}
	
	/**
	 * Sets chunk attributes map
	 */
	public void setChunkAttributes(Map<IType, Chunk> chunkAttributes)
	{
		this.chunkAttributes = chunkAttributes;
	}
	
	/**
	 * Gets the IType attributes map.
	 * 
	 * @return the ontologizedAttributes map.
	 */
	public Map<IType,IType> getOntologizedAttributes()
	{
		return ontologizedAttributes;
	}
	
	/**
	 * Sets IType attributes map
	 */
	public void setOntologizedAttributes(Map<IType, IType> ontologizedAttributes)
	{
		this.ontologizedAttributes = ontologizedAttributes;
	}
	
	
	
	
	/** Convenience methods. */
	
	/**
	 * Gets the measurement type.
	 * 
	 * @return the measurement type
	 */
	public IType getMeasurementType()
	{
		try
		{
			return ontologizedAttributes.get(QuantifiedChunkAttributesTypeFactory.getInstance().getType("MEASUREMENT TYPE"));
		}
		catch(InvalidPropertiesFormatException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Sets the measurement type.
	 */
	public void setMeasurementType(IType measurementType)
	{
		try
		{
			ontologizedAttributes.put(QuantifiedChunkAttributesTypeFactory.getInstance().getType("MEASUREMENT TYPE"), measurementType);
		}
		catch(InvalidPropertiesFormatException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Gets the measurement unit.
	 * 
	 * @return the measurement type
	 */
	public Chunk getMeasurementUnit()
	{
		try
		{
			return chunkAttributes.get(QuantifiedChunkAttributesTypeFactory.getInstance().getType("MEASUREMENT UNIT"));
		}
		catch(InvalidPropertiesFormatException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Sets the measurement unit.
	 */
	public void setMeasurementUnit(TokenOffset tokenOffset)
	{
		try
		{
			chunkAttributes.put(QuantifiedChunkAttributesTypeFactory.getInstance().getType("MEASUREMENT UNIT"), new Chunk(tokenOffset, this.tokenStream));
		}
		catch(InvalidPropertiesFormatException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets the measurement unit.
	 * 
	 * @return the measurement type
	 */
	public Chunk getMeasurementValue()
	{
		try
		{
			return chunkAttributes.get(QuantifiedChunkAttributesTypeFactory.getInstance().getType("MEASUREMENT VALUE"));
		}
		catch(InvalidPropertiesFormatException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Sets the measurement unit.
	 */
	public void setMeasurementValue(TokenOffset tokenOffset)
	{
		try
		{
			chunkAttributes.put(QuantifiedChunkAttributesTypeFactory.getInstance().getType("MEASUREMENT VALUE"), new Chunk(tokenOffset, this.tokenStream));
		}
		catch(InvalidPropertiesFormatException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
	}

	
	
}