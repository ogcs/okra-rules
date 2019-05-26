package org.okra.rules.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.okra.rules.examples.autowired.MockComponent;
import org.okra.rules.examples.autowired.MockRule;
import org.okra.rules.support.spring.RuleScan;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author TinyZ.
 * @version 2019.05.26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringAutowiredTest.class)
@Configuration
@ComponentScan(basePackages = "org.okra.rules.examples.autowired")
@RuleScan(basePackages = "org.okra.rules.examples.autowired")
public class SpringAutowiredTest implements ApplicationContextAware {

    private ApplicationContext context;

    @Test
    public void test() {
        MockComponent component = context.getBean(MockComponent.class);
        System.out.println();
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        context.getBean(MockRule.class);
        this.context = context;
    }
}
