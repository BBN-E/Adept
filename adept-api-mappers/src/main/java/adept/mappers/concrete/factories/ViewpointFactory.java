package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.Viewpoint;

public class ViewpointFactory implements org.dozer.BeanFactory {
	public Viewpoint createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Viewpoint("", "");
	}
}
