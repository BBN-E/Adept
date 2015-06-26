package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.Corpus;

public class CorpusFactory implements org.dozer.BeanFactory {
	public Corpus createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Corpus(null, null, null, null);
	}
}
