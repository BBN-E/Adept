package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.Entailment;

public class EntailmentFactory implements org.dozer.BeanFactory {
	public Entailment createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Entailment(0);
	}
}
