package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.TokenStream;

public class TokenStreamFactory implements org.dozer.BeanFactory {
	public TokenStream createBean(Object source, Class sourceClass, String targetBeanId) {

		adept.common.Document document = new adept.common.Document("", null, "", "", "");
		document.setValue("");
		return new adept.common.TokenStream(null, null, "", null, null, document);
	}
}
