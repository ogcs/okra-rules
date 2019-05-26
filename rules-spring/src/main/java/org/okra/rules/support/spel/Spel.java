package org.okra.rules.support.spel;

import org.okra.rules.core.RuleContext;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author TinyZ.
 * @version 2019.05.25
 */
public final class Spel {

    private static SpelParserConfiguration spelParserConfiguration;
    private static SpelExpressionParser spelParser;
    private static BeanResolver beanResolver;

    private Spel() {
        //  no-op
    }

    public static void checkIllegalSpelContext(RuleContext ctx) {
        if (ctx == null || ctx instanceof SpelRuleContext)
            return;
        throw new IllegalArgumentException("ctx not SpEL context. actual type:" + ctx.getClass());
    }

    public static SpelParserConfiguration getSpelParserConfiguration() {
        if (spelParserConfiguration == null) {
            spelParserConfiguration = new SpelParserConfiguration();
        }
        return spelParserConfiguration;
    }

    public static SpelExpressionParser getSpelParser() {
        if (null == spelParser) {
            spelParser = new SpelExpressionParser(getSpelParserConfiguration());
        }
        return spelParser;
    }

    public static StandardEvaluationContext newEvaluationContext() {
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setBeanResolver(getBeanResolver());
        return ctx;
    }

    public static void setBeanResolver(BeanResolver beanResolver) {
        Spel.beanResolver = beanResolver;
    }

    public static BeanResolver getBeanResolver() {
        return beanResolver;
    }

    public void test() {
        SpelExpressionParser spel = new SpelExpressionParser();
        SpelExpression expression = spel.parseRaw("");
        expression.compileExpression();
        Expression expression1 = spel.parseExpression("");
        spel.parseExpression("", new TemplateParserContext("", ""));
//            expression1.

    }

}
