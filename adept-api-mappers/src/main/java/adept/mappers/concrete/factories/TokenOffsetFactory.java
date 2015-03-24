package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.TokenOffset;

public class TokenOffsetFactory implements org.dozer.BeanFactory {
	public TokenOffset createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.TokenOffset(0, 0);
	}
}
