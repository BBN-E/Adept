package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.Dependency;

public class DependencyFactory implements org.dozer.BeanFactory {
	public Dependency createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Dependency(null, null, "");
	}
}
