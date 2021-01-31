/*
 *
 *
 *          Copyright (c) 2021. - TinyZ.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.okra.rules.support.spel;

import org.okra.rules.core.RuleContext;
import org.okra.rules.core.api.Rule;
import org.okra.rules.core.api.Watcher;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;

/**
 * @author TinyZ.
 * @version 2019.05.25
 */
public class SpelWatcher implements Watcher {

    private ParserContext parserContext;

    private Expression beforeEvaluate;
    private Expression afterEvaluate;
    private Expression beforeExecute;
    private Expression onSuccess;
    private Expression onFailure;
    private Expression onException;

    public SpelWatcher() {
        //  empty
    }

    public SpelWatcher(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public boolean beforeEvaluate(Rule rule, RuleContext context) {
        if (beforeEvaluate == null)
            return true;
        Spel.checkIllegalSpelContext(context);
        Boolean result = beforeEvaluate.getValue(((SpelRuleContext) context).getEvaluationContext(), Boolean.class);
        return result != null && result;
    }

    @Override
    public void afterEvaluate(Rule rule, RuleContext context, boolean evaluationResult) {
        if (null == afterEvaluate)
            return;
        Spel.checkIllegalSpelContext(context);
        afterEvaluate.getValue(((SpelRuleContext) context).getEvaluationContext());
    }

    @Override
    public void beforeExecute(Rule rule, RuleContext context) {
        if (null == beforeExecute)
            return;
        Spel.checkIllegalSpelContext(context);
        beforeExecute.getValue(((SpelRuleContext) context).getEvaluationContext());
    }

    @Override
    public void onSuccess(Rule rule, RuleContext context) {
        if (null == onSuccess)
            return;
        Spel.checkIllegalSpelContext(context);
        onSuccess.getValue(((SpelRuleContext) context).getEvaluationContext());
    }

    @Override
    public void onFailure(Rule rule, RuleContext context) {
        if (null == onFailure)
            return;
        Spel.checkIllegalSpelContext(context);
        onFailure.getValue(((SpelRuleContext) context).getEvaluationContext());
    }

    @Override
    public void onException(Rule rule, RuleContext context, Exception exception) {
        if (null == onException)
            return;
        Spel.checkIllegalSpelContext(context);
        onException.getValue(((SpelRuleContext) context).getEvaluationContext());
    }

    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void setParserContext(ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    public void setBeforeEvaluate(Expression beforeEvaluate) {
        this.beforeEvaluate = beforeEvaluate;
    }

    public void setAfterEvaluate(Expression afterEvaluate) {
        this.afterEvaluate = afterEvaluate;
    }

    public void setBeforeExecute(Expression beforeExecute) {
        this.beforeExecute = beforeExecute;
    }

    public void setOnSuccess(Expression onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void setOnFailure(Expression onFailure) {
        this.onFailure = onFailure;
    }

    public void setOnException(Expression onException) {
        this.onException = onException;
    }

    public void setBeforeEvaluateExpression(String expression) {
        this.beforeEvaluate = parseExpression(expression);
    }

    public void setAfterEvaluateExpression(String expression) {
        this.afterEvaluate = parseExpression(expression);
    }

    public void setBeforeExecuteExpression(String expression) {
        this.beforeExecute = parseExpression(expression);
    }

    public void setOnSuccessExpression(String expression) {
        this.onSuccess = parseExpression(expression);
    }

    public void setOnFailureExpression(String expression) {
        this.onFailure = parseExpression(expression);
    }

    public void setOnExceptionExpression(String expression) {
        this.onException = parseExpression(expression);
    }

    private Expression parseExpression(String expression) {
        ParserContext context = this.parserContext == null
                ? Spel.getParserContext()
                : this.parserContext;
        return Spel.getSpelParser().parseExpression(expression, context);
    }
}
