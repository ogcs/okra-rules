package org.okra.rules.support.spel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.HashMap;
import java.util.Map;


/**
 * @author TinyZ.
 * @version 2019.06.07
 */
public class SpelTest {

    @Test
    public void testAction() throws Exception {
        SpelRuleContext context = new SpelRuleContext();
        {
            Calculator calculator = new Calculator();
            context.with("calculator", calculator);
        }
        ParserContext parserContext = Spel.getParserContext();

        Map<String, Object> expressionMap = new HashMap<>();
        expressionMap.put("hello world", "hello world");
        expressionMap.put("@{1+4}", 5);
        expressionMap.put("my age is @{1+4}", "my age is 5");
        expressionMap.put("calc 1+2 result is @{calculator.plus(1, 2)}", "calc 1+2 result is 3");
        expressionMap.put("@{calculator.plus(1, 2) + calculator.plus(1, 2)}", 6);
        for (Map.Entry<String, Object> entry : expressionMap.entrySet()) {
            Expression expression = Spel.getSpelParser().parseExpression(entry.getKey(), parserContext);
            Object value = expression.getValue(context.getEvaluationContext());
            Assertions.assertEquals(entry.getValue(), value);
        }
    }

    public static class Calculator {
        public int plus(int var0, int var1) {
            return var0 + var1;
        }
    }

    @Test
    public void test() throws Exception {
        SpelRuleContext context = new SpelRuleContext();
        SpelExpressionParser parser = Spel.getSpelParser();
        ParserContext parserContext = Spel.getParserContext();

        {
            String str = "hello world";
//            Expression expression = parser.parseExpression(str, parserContext);
//            Object value = expression.getValue(context);
            SpelRule rule = new SpelRule();
//            rule.setParserContext(parserContext);
            rule.setActionExpressions(new String[]{str, str, str});
            rule.execute(context);
//            Assert.assertEquals(str, value);
        }

    }

}