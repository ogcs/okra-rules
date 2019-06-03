package org.okra.rules.support.spel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author TinyZ.
 * @version 2019.05.26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/org/okra/rules/support/spel/spel-rules-test.xml")
public class SpelRuleTest implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor1() {
        SpelRule rule = (SpelRule) context.getBean("testConstructor1");
        SpelRuleContext ctx = new SpelRuleContext();
        rule.evaluate(ctx);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor2() {
        SpelRule rule = (SpelRule) context.getBean("testConstructor2");
        SpelRuleContext ctx = new SpelRuleContext();
        rule.evaluate(ctx);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor3() {
        SpelRule rule = (SpelRule) context.getBean("testConstructor3");
        SpelRuleContext ctx = new SpelRuleContext();
        rule.evaluate(ctx);
    }

    @Test
    public void test() throws Exception {
        SpelRule rule = (SpelRule) context.getBean("testBeanMethod");

        SpelRuleContext ctx = new SpelRuleContext(context);
        ctx.with("apple", new Apple(6));
        rule.execute(ctx);
    }

    public static class Apple {

        private int color;

        public Apple(int color) {
            this.color = color;
        }

        public boolean isGreen() {
            return color < 10;
        }
    }

    public static boolean invokeCustomMethod(String world) {
        System.out.println("Output:" + world);
        return true;
    }


}