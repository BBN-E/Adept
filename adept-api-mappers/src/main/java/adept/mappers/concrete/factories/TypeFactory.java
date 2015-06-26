package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.Type;

public class TypeFactory implements org.dozer.BeanFactory {
	public Type createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Type("");
	}
}
