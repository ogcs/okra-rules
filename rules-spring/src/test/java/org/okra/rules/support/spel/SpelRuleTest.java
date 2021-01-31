package org.okra.rules.support.spel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author TinyZ.
 * @version 2019.05.26
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/org/okra/rules/support/spel/spel-rules-test.xml")
public class SpelRuleTest implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Test()
    public void testConstructor1() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            SpelRule rule = (SpelRule) context.getBean("testConstructor1");
            SpelRuleContext ctx = new SpelRuleContext();
            rule.evaluate(ctx);
        });
    }

    @Test()
    public void testConstructor2() {
        Assertions.assertThrows(null, () -> {
            SpelRule rule = (SpelRule) context.getBean("testConstructor2");
            SpelRuleContext ctx = new SpelRuleContext();
            rule.evaluate(ctx);
        });
    }

    @Test()
    public void testConstructor3() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            SpelRule rule = (SpelRule) context.getBean("testConstructor3");
            SpelRuleContext ctx = new SpelRuleContext();
            rule.evaluate(ctx);
        });
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