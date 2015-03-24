package adept.mappers.thrift.factories;

import org.dozer.BeanFactory;
import adept.common.Entailment.Judgment;

public class EntailmentJudgmentFactory implements org.dozer.BeanFactory {
	public Judgment createBean(Object source, Class sourceClass, String targetBeanId) {
		return adept.common.Entailment.Judgment.ENTAILS;
	}
}
