package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.SentenceSimilarity;

import java.util.Map;
import java.util.HashMap;

public class SentenceSimilarityFactory implements org.dozer.BeanFactory {
	public SentenceSimilarity createBean(Object source, Class sourceClass, String targetBeanId) {
        
		Map<String,Float> similarityscore = new HashMap<String,Float>();
		//similarityscore.put("SCORE", (Float)0);
		return new adept.common.SentenceSimilarity(similarityscore, null, null);
	}
}
