package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.Entity;

public class EntityFactory implements org.dozer.BeanFactory {
	public Entity createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Entity(0, null);
	}
}
