package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.Coreference;

public class CoreferenceFactory implements org.dozer.BeanFactory {
	public Coreference createBean(Object source, Class sourceClass, String targetBeanId) {
		return new adept.common.Coreference(0);
	}
}
