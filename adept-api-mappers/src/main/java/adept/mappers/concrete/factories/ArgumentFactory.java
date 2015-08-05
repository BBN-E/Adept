package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.Argument;

public class ArgumentFactory implements org.dozer.BeanFactory {
	public Argument createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Argument(null, 0);
	}
}
