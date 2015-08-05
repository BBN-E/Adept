package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.Relation;

public class RelationFactory implements org.dozer.BeanFactory {
	public Relation createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Relation(0,null);
	}
}
