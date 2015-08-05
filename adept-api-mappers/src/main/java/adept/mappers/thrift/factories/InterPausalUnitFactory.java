package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.InterPausalUnit;

public class InterPausalUnitFactory implements org.dozer.BeanFactory {
	public InterPausalUnit createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.InterPausalUnit(0, null);
	}
}
