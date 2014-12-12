/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

package adept.mappers.concrete.converters;
import org.dozer.DozerBeanMapper;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class IDConverter implements org.dozer.CustomConverter {
  
  public Object convert(Object destination, Object source, 
      Class destClass, Class sourceClass) {
    if (source == null) {
      return null;
    }
    DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList(new String[]{"adept/mappers/concrete/ConcreteAdeptMappings.xml"}));
    if (source instanceof adept.common.ID) {
	adept.common.ID id = (adept.common.ID) source;
	edu.jhu.hlt.concrete.Concrete.UUID uuid = edu.jhu.hlt.concrete.Concrete.UUID.getDefaultInstance();
	adept.mappers.concrete.ID tempId = new adept.mappers.concrete.ID();
	tempId.setId(id.getId());
	mapper.map(tempId, uuid);
	return uuid;
   } else if (source instanceof edu.jhu.hlt.concrete.Concrete.UUID) {
	edu.jhu.hlt.concrete.Concrete.UUID uuid = (edu.jhu.hlt.concrete.Concrete.UUID) source;
	adept.common.ID id = new adept.common.ID();
	adept.mappers.concrete.ID tempId = new adept.mappers.concrete.ID();
	tempId.setId(uuid.getHigh(), uuid.getLow());		
	mapper.map(tempId, id);
	return id;
    } else {
      throw new org.dozer.MappingException("Converter IDConverter "
          + "used incorrectly. Arguments passed in were:"
          + destination + " and " + source);
    }
  } 
}
