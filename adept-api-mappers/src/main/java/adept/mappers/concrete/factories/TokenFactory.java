package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.Token;

public class TokenFactory implements org.dozer.BeanFactory {
	public Token createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Token(0, null, "");
	}
}
