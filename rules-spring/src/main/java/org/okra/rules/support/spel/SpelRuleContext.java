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
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.expression.spel.support.StandardTypeLocator;

/**
 * @author TinyZ.
 * @version 2019.05.25
 */
public class SpelRuleContext extends RuleContext {

    private StandardEvaluationContext sec;
    /**
     * Is necessary initialize SpEL's evaluation context when the sec is null ?
     */
    private boolean notInitSec = false;
    /**
     * Springframework application context
     */
    private ConfigurableApplicationContext applicationContext;

    public SpelRuleContext() {
        //  empty
    }

    public SpelRuleContext(boolean notInitSec) {
        this(notInitSec, null);
    }

    public SpelRuleContext(ApplicationContext applicationContext) {
        this(false, applicationContext);
    }

    public SpelRuleContext(boolean notInitSec, ApplicationContext applicationContext) {
        this.notInitSec = notInitSec;
        if (applicationContext instanceof ConfigurableApplicationContext) {
            this.applicationContext = (ConfigurableApplicationContext) applicationContext;
        } else {
            GenericApplicationContext context = new GenericApplicationContext(applicationContext);
            context.refresh();
            this.applicationContext = context;
        }
    }

    public EvaluationContext getEvaluationContext() {
        if (notInitSec) {
            return sec;
        }
        if (sec == null) {
            sec = new StandardEvaluationContext(params());
            sec.setVariables(params());
            sec.addPropertyAccessor(new MapAccessor());

            //  Springframework support.
            if (applicationContext != null) {
                ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
                sec.addPropertyAccessor(new BeanExpressionContextAccessor());
                sec.addPropertyAccessor(new BeanFactoryAccessor());
                sec.addPropertyAccessor(new EnvironmentAccessor());
                sec.setBeanResolver(new BeanFactoryResolver(beanFactory));
                sec.setTypeLocator(new StandardTypeLocator(beanFactory.getBeanClassLoader()));
                ConversionService conversionService = beanFactory.getConversionService();
                if (conversionService != null) {
                    sec.setTypeConverter(new StandardTypeConverter(conversionService));
                }
            }
        }
        return sec;
    }
}
