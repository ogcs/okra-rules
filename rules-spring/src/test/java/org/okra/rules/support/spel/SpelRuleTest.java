package org.okra.rules.support.spel;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author TinyZ.
 * @version 2019.05.26
 */
public class SpelRuleTest {

    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("example-rules.xml");
        context.start();
        SpelRule rule = context.getBean(SpelRule.class);

        SpelRuleContext ctx = new SpelRuleContext();
//        ctx.getEvaluationContext().setVariable();

        boolean execute = rule.execute(ctx);


        System.out.println();


    }

    public static boolean invokeCustomMethod(String world) {
        System.out.println("Output:" + world);
        return true;
    }


}