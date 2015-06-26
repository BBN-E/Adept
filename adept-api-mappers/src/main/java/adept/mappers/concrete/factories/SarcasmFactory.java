package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.Sarcasm;

public class SarcasmFactory implements org.dozer.BeanFactory {
	public Sarcasm createBean(Object source, Class sourceClass, String targetBeanId) {
		adept.common.Document document = new adept.common.Document("", null, "", "", "");
		document.setValue("");
		adept.common.TokenStream tokenStream = new adept.common.TokenStream(null, null, null, null, null, document);
		tokenStream.add(new adept.common.Token(0, new adept.common.CharOffset(0,0), null));
		return new adept.common.Sarcasm(0, new adept.common.TokenOffset(0, 0), tokenStream, null);
	}
}
