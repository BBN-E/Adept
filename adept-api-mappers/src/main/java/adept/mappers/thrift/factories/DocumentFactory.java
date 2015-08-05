package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.Document;

public class DocumentFactory implements org.dozer.BeanFactory {
	public Document createBean(Object source, Class sourceClass, String targetBeanId) {

		adept.common.Corpus corpus = new adept.common.Corpus("", null, null, null);

		return new adept.common.Document("", null, "", "", "");
	}
}
