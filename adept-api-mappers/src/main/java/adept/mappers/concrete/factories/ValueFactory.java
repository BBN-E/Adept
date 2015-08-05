package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.Value;

public class ValueFactory implements org.dozer.BeanFactory {
	public Value createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Value(0);
	}
}
