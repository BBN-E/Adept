package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import thrift.adept.common.Document;

public class ThriftDocumentFactory implements org.dozer.BeanFactory {
	public Document createBean(Object source, Class sourceClass, String targetBeanId) {
		final adept.common.Document document = (adept.common.Document) source;

		thrift.adept.common.Corpus corpus = new thrift.adept.common.Corpus("", "", "", "");

		return new thrift.adept.common.Document("", corpus, "", "", "");
	}
}
