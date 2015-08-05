package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.AnomalousText;

public class AnomalousTextFactory implements org.dozer.BeanFactory {
	public AnomalousText createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.AnomalousText(0, null);
	}
}
