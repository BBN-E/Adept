package adept.mappers.concrete.factories;

import org.dozer.BeanFactory;
import adept.common.Paraphrase;

public class ParaphraseFactory implements org.dozer.BeanFactory {
	public Paraphrase createBean(Object source, Class sourceClass, String targetBeanId) {

		return new adept.common.Paraphrase("", 0);
	}
}
