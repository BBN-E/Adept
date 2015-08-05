package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.Triple;

public class TripleFactory implements org.dozer.BeanFactory {
	public Triple createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Triple(null, null, null);
	}
}
