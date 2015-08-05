package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.Topic;

public class TopicFactory implements org.dozer.BeanFactory {
	public Topic createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Topic(0, "");
	}
}
