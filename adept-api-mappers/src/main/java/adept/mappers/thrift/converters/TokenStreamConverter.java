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

public class TokenStreamConverter implements org.dozer.CustomConverter {
  
  public Object convert(Object destination, Object source, 
      Class destClass, Class sourceClass) {
    if (source == null) {
      return null;
    }
    DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList(new String[]{"adept/mappers/thrift/ThriftAdeptMappings.xml"}));
    if (source instanceof adept.common.TokenStream) {

		adept.common.TokenStream tokenStream = (adept.common.TokenStream) source;

		thrift.adept.common.TokenStream retTokenStream = mapper.map(tokenStream, thrift.adept.common.TokenStream.class);	

		List<thrift.adept.common.Token> tokenList = new ArrayList<thrift.adept.common.Token>();

		for (int i=0; i<tokenStream.size(); i++) {
			adept.common.Token token = tokenStream.get(i);
			thrift.adept.common.Token thriftToken = mapper.map(token, thrift.adept.common.Token.class);
			tokenList.add(thriftToken);
		}

		retTokenStream.setTokenList(tokenList);

		return retTokenStream;
   }else {
      throw new org.dozer.MappingException("Converter TokenStreamConverter "
          + "used incorrectly. Arguments passed in were:"
          + destination + " and " + source);
    }
  } 
}