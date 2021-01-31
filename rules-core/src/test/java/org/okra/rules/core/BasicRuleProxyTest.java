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

package org.okra.rules.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.okra.rules.annotation.Action;
import org.okra.rules.annotation.Condition;
import org.okra.rules.annotation.Param;
import org.okra.rules.annotation.Rule;
import org.okra.rules.exception.NoSuchParameterException;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class BasicRuleProxyTest {

    public static final AtomicInteger TEMP = new AtomicInteger();

    @BeforeEach()
    public void before() {
        TEMP.set(0);
    }

    @Test()
    public void asRule_() {
        org.okra.rules.core.api.Rule mockRule = Rules.asRule(new A());
        RulesEngine engine = new RulesEngine();
        engine.addRule(mockRule);
        engine.fire("a");
        //  assert
        Assertions.assertNotNull(mockRule);
        Assertions.assertEquals(1, TEMP.get());
    }

    @Rule(identify = "a", description = "this is a's rule", priority = 1)
    public static class A {

        @Condition
        public boolean isTrue() {
            return true;
        }

        @Action()
        public void doAction() {
            System.out.println("xxx");
            TEMP.getAndIncrement();
        }
    }

    @Test()
    public void asRule_constructor_notThrowAnyException() {
        //  assert
        assertDoesNotThrow(() -> Rules.asRule(new PoorRule()), "Poor Rule");
        assertDoesNotThrow(() -> Rules.asRule(new MinSimpleRule()), "Simple Rule");
        assertDoesNotThrow(() -> {
            org.okra.rules.core.api.Rule rule = Rules.asRule(new CustomInterfaceRule());
            assertTrue(rule instanceof EmptyInterface);
            assertTrue(((EmptyInterface) rule).doNothing());
        }, "Serializable Rule");

        assertThrows(NoSuchParameterException.class, () -> {
            org.okra.rules.core.api.Rule rule = Rules.asRule(new SingleParameterConditionRule());
            assertTrue(rule.evaluate(null));
        }, "Single Parameter Condition Rule");

        assertDoesNotThrow(() -> {
            org.okra.rules.core.api.Rule rule = Rules.asRule(new SingleParameterConditionRule());
            assertTrue(rule.evaluate(new RuleContext()));
        }, "Single Parameter Condition Rule");

        assertThrows(NoSuchParameterException.class, () -> {
            org.okra.rules.core.api.Rule rule = Rules.asRule(new MultipleParameterConditionRule());
            assertFalse(rule.evaluate(null));
        }, "Multiple Parameter Condition Rule");
        assertDoesNotThrow(() -> {
            org.okra.rules.core.api.Rule rule = Rules.asRule(new MultipleParameterConditionRule());
            RuleContext context = new RuleContext();
            context.with("lv", 99);
            assertFalse(rule.evaluate(context));
        }, "Multiple Parameter Condition Rule");
        assertDoesNotThrow(() -> {
            org.okra.rules.core.api.Rule rule = Rules.asRule(new MultipleParameterConditionRule());
            RuleContext context = new RuleContext();
            context.with("lv", 101);
            assertTrue(rule.evaluate(context));
        }, "Multiple Parameter Condition Rule");
    }

    @Rule()
    public static class PoorRule {

        @Condition
        public boolean isTrue() {
            return true;
        }

        @Action()
        public void doAction() {
        }
    }

    @Rule(identify = "a", description = "this is a's rule", priority = 1)
    public static class MinSimpleRule {

        @Condition
        public boolean isTrue() {
            return true;
        }

        @Action()
        public void doAction() {
        }
    }

    @Rule()
    public static class CustomInterfaceRule implements EmptyInterface {

        @Condition
        public boolean isTrue() {
            return true;
        }

        @Action()
        public void doAction() {
        }

        @Override
        public boolean doNothing() {
            return true;
        }
    }

    @Rule()
    public static class SingleParameterConditionRule {
        @Condition
        public boolean isTrue(RuleContext ctx) {
            return true;
        }

        @Action()
        public void doAction() {
        }
    }

    @Rule()
    public static class MultipleParameterConditionRule {
        @Condition
        public boolean isTrue(RuleContext ctx, @Param("lv") int lv) {
            return lv > 100;
        }

        @Action()
        public void doAction() {
        }
    }

    public static class ExtendRuleImpl extends BasicRule {

        @Condition
        public boolean isTrue(RuleContext ctx, @Param("lv") int lv) {
            return lv > 100;
        }

        @Action()
        public void doAction() {
        }

        @Override
        public void execute(RuleContext context) throws Exception {

        }
    }

    public interface EmptyInterface {
        boolean doNothing();
    }


}