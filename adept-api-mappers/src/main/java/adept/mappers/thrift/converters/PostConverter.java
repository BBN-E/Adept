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

/**
 * Because Thrift doesn't allow recursive structs, there is a slight difference with the Post and PostQuote classes, explained here:
 * Post has a list called allSubQuotes, which is a list of all the PostQuotes and their subquotes in that Post.
 * It also has a list called quotes, which is a list of integers that are indices into the allSubQuotes list.
 * Those indices point to the top level PostQuotes of that Post.
 * PostQuote has a postId attribute which points to the Post to which it belongs.
 * PostQuote also has a subQuotes list, which are indices into the Post's allSubQuotes list.
 * This means the PostQuote's subQuotes list is useless without the Post it belongs to.
*/

public class PostConverter implements org.dozer.CustomConverter {
  
  public Object convert(Object destination, Object source, 
      Class destClass, Class sourceClass) {
    if (source == null) {
      return null;
    }
    DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList(new String[]{"adept/mappers/thrift/ThriftAdeptMappings.xml"}));
    if (source instanceof adept.common.Post) {

		adept.common.Post post = (adept.common.Post) source;

		thrift.adept.common.Post retPost = new thrift.adept.common.Post(post.getSequenceId(), mapper.map(post.getTokenOffset(), thrift.adept.common.TokenOffset.class), mapper.map(post.getTokenStream(), thrift.adept.common.TokenStream.class), mapper.map(post.getOpenTag(), thrift.adept.common.Tag.class), mapper.map(post.getCloseTag(), thrift.adept.common.Tag.class));

		List<adept.common.PostQuote> allSubQuotes = post.getAllSubQuotes();
		Map<adept.common.PostQuote, Integer> quoteIndices = new HashMap<adept.common.PostQuote, Integer>();
		List<thrift.adept.common.PostQuote> allThriftSubQuotes = new ArrayList<thrift.adept.common.PostQuote>();
		int i = 0;

		if (allSubQuotes != null) {
			for (adept.common.PostQuote quote : allSubQuotes) {
				quoteIndices.put(quote, i);
				i ++;
				allThriftSubQuotes.add(mapper.map(quote, thrift.adept.common.PostQuote.class));
			}
	
			retPost.setAllSubQuotes(allThriftSubQuotes);

			List<Integer> quoteList = new ArrayList<Integer>();
			for (adept.common.PostQuote quote : post.getQuotes()) {
				if (quoteIndices.get(quote) != null) {
					quoteList.add(quoteIndices.get(quote));
				}
			}
			if (quoteList.size() > 0) {
				retPost.setQuotes(quoteList);
			}
		}

		retPost.setId(mapper.map(post.getId(), thrift.adept.common.ID.class));
		retPost.setValue(post.getValue());
		retPost.setAlgorithmName(post.getAlgorithmName());

		return retPost;
   }
   else if (source instanceof thrift.adept.common.Post) {

		thrift.adept.common.Post post = (thrift.adept.common.Post) source;

		adept.common.Post retPost = new adept.common.Post(post.getSequenceId(), mapper.map(post.getTokenOffset(), adept.common.TokenOffset.class), mapper.map(post.getTokenStream(), adept.common.TokenStream.class), mapper.map(post.getOpenTag(), adept.common.Tag.class), mapper.map(post.getCloseTag(), adept.common.Tag.class));

		List<thrift.adept.common.PostQuote> allThriftSubQuotes = post.getAllSubQuotes();
		for (int i : post.getQuotes()) {
			adept.common.PostQuote quote = mapper.map(allThriftSubQuotes.get(i), adept.common.PostQuote.class);
			retPost.addQuote(quote);
		}

		return retPost;
		
    }
   else if (source instanceof ArrayList) {

		List<thrift.adept.common.Post> retList = new ArrayList<thrift.adept.common.Post>();
		List<Object> sourceList = (ArrayList<Object>) source;
		for (Object sourceObject : sourceList) {
			adept.common.Post post = (adept.common.Post) sourceObject;

			thrift.adept.common.Post retPost = mapper.map(post, thrift.adept.common.Post.class);

			retList.add(retPost);
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