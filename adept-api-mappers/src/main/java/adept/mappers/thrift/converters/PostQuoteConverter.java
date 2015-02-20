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

package adept.mappers.thrift.converters;
import org.dozer.DozerBeanMapper;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import adept.common.BoundedList;
import adept.common.Pair;
import adept.common.Chunk;

import thrift.adept.common.ChunkUnion;

public class PostQuoteConverter implements org.dozer.CustomConverter {
  
  public Object convert(Object destination, Object source, 
      Class destClass, Class sourceClass) {
    if (source == null) {
      return null;
    }
    DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList(new String[]{"adept/mappers/thrift/ThriftAdeptMappings.xml"}));
    if (source instanceof adept.common.PostQuote) {

		adept.common.PostQuote quote = (adept.common.PostQuote) source;

		thrift.adept.common.PostQuote retQuote = new thrift.adept.common.PostQuote(quote.getSequenceId(), mapper.map(quote.getTokenOffset(), thrift.adept.common.TokenOffset.class), mapper.map(quote.getTokenStream(), thrift.adept.common.TokenStream.class), mapper.map(quote.getOpenTag(), thrift.adept.common.Tag.class), mapper.map(quote.getCloseTag(), thrift.adept.common.Tag.class));

		List<adept.common.PostQuote> allPostSubQuotes = quote.getPost().getAllSubQuotes();
		Map<adept.common.PostQuote, Integer> quoteIndices = new HashMap<adept.common.PostQuote, Integer>();
		
		int i = 0;
		for (adept.common.PostQuote subQuote : allPostSubQuotes) {
			quoteIndices.put(subQuote, i);
			i ++;
		}

		List<Integer> quoteList = new ArrayList<Integer>();
		for (adept.common.PostQuote subQuote : quote.getSubQuotes()) {
			quoteList.add(quoteIndices.get(subQuote));
		}
		
		retQuote.setPostId(quote.getPost().getIdString());

		retQuote.setSubQuotes(quoteList);

		retQuote.setId(mapper.map(quote.getId(), thrift.adept.common.ID.class));
		retQuote.setValue(quote.getValue());
		retQuote.setAlgorithmName(quote.getAlgorithmName());

		return retQuote;
   }
   else if (source instanceof thrift.adept.common.PostQuote) {

		thrift.adept.common.PostQuote quote = (thrift.adept.common.PostQuote) source;

		adept.common.PostQuote retQuote = new adept.common.PostQuote(quote.getSequenceId(), mapper.map(quote.getTokenOffset(), adept.common.TokenOffset.class), mapper.map(quote.getTokenStream(), adept.common.TokenStream.class), mapper.map(quote.getOpenTag(), adept.common.Tag.class), mapper.map(quote.getCloseTag(), adept.common.Tag.class));

		return retQuote;
		
    }
   else if (source instanceof ArrayList) {

		List<thrift.adept.common.PostQuote> retList = new ArrayList<thrift.adept.common.PostQuote>();
		List<Object> sourceList = (ArrayList<Object>) source;
		for (Object sourceObject : sourceList) {
			adept.common.PostQuote quote = (adept.common.PostQuote) sourceObject;

			thrift.adept.common.PostQuote retQuote = mapper.map(quote, thrift.adept.common.PostQuote.class);

			retList.add(retQuote);
		}
		return retList;			
    }
   else {
      throw new org.dozer.MappingException("Converter ArgumentConverter "
          + "used incorrectly. Arguments passed in were:"
          + destination + " and " + source);
    }
  }
		
}