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
