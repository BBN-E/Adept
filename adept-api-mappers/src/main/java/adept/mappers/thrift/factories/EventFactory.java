package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.Event;

public class EventFactory implements org.dozer.BeanFactory {
	public Event createBean(Object source, Class sourceClass, String targetBeanId) {
		final thrift.adept.common.Event event = (thrift.adept.common.Event) source;

		return new adept.common.Event(0, null);
	}
}
