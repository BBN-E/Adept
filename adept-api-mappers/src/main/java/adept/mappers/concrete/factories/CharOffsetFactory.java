package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.CharOffset;

public class CharOffsetFactory implements org.dozer.BeanFactory {
	public CharOffset createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.CharOffset(0, 0);
	}
}
