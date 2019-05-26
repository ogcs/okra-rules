package org.okra.rules.spring;

import org.okra.rules.annotation.Rule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;


public class RuleEngineAware implements SmartInstantiationAwareBeanPostProcessor {


//    @Override
//    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
//        Map<String, Object> rules = ctx.getBeansWithAnnotation(Rule.class);
//
//
//        System.out.println();
//
//    }


}
