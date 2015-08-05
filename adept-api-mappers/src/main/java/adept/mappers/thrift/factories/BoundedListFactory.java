package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.BoundedList;

public class BoundedListFactory implements org.dozer.BeanFactory {
	public BoundedList createBean(Object source, Class sourceClass, String targetBeanId) {
		return new adept.common.BoundedList(0);
	}
}
