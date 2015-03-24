package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.AudioOffset;

public class AudioOffsetFactory implements org.dozer.BeanFactory {
	public AudioOffset createBean(Object source, Class sourceClass, String targetBeanId) {
		return new AudioOffset(0, 0);
	}
}
