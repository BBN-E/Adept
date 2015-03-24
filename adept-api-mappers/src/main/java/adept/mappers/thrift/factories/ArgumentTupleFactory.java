package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.ArgumentTuple;

public class ArgumentTupleFactory implements org.dozer.BeanFactory {
	public ArgumentTuple createBean(Object source, Class sourceClass, String targetBeanId) {
		final thrift.adept.common.ArgumentTuple argumentTuple = (thrift.adept.common.ArgumentTuple) source;

		return new adept.common.ArgumentTuple(null);
	}
}
