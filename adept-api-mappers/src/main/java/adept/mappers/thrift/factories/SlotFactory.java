package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.Slot;

public class SlotFactory implements org.dozer.BeanFactory {
	public Slot createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Slot(0, "");
	}
}
