package org.okra.rules.core;


import org.okra.rules.annotation.Action;
import org.okra.rules.annotation.Condition;
import org.okra.rules.annotation.Param;
import org.okra.rules.annotation.Priority;
import org.okra.rules.annotation.Rule;
import org.okra.rules.util.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * This component validates that an annotated rule object is well defined.
 */
public final class RuleDefinitionValidator {

    private RuleDefinitionValidator() {
        //  no-op
    }

    public static RuleDefinitionValidator create() {
        return new RuleDefinitionValidator();
    }

    public void validateRuleDefinition(final Object rule) {
        checkRuleClass(rule);
        checkConditionMethods(rule);
        checkActionMethods(rule);
        checkPriorityMethod(rule);
    }

    private void checkRuleClass(final Object rule) {
        if (!validateRuleClass(rule)) {
            throw new IllegalArgumentException(String.format("Rule '%s' is not annotated with '%s'", rule.getClass().getName(), Rule.class.getName()));
        }
        if (!Modifier.isPublic(rule.getClass().getModifiers())) {
            throw new IllegalArgumentException(String.format("Rule '%s' is not public class", rule.getClass().getName()));
        }
    }

    private void checkConditionMethods(final Object rule) {
        List<Method> conditionMethods = AnnotationUtils.getMethodsAnnotatedWith(Condition.class, rule, true);
        if (conditionMethods.isEmpty()) {
            throw new IllegalArgumentException(String.format("Rule '%s' must have a public method annotated with '%s'", rule.getClass().getName(), Condition.class.getName()));
        }
        if (conditionMethods.size() > 1) {
            throw new IllegalArgumentException(String.format("Rule '%s' must have exactly one method annotated with '%s'", rule.getClass().getName(), Condition.class.getName()));
        }
        Method conditionMethod = conditionMethods.get(0);
        if (!validateConditionMethod(conditionMethod)) {
            throw new IllegalArgumentException(String.format("Condition method '%s' defined in rule '%s' must be public, may have parameters annotated with @Param (and/or exactly one parameter of type or extending Facts) and return boolean type.", conditionMethod, rule.getClass().getName()));
        }
    }

    private void checkActionMethods(final Object rule) {
        List<Method> actionMethods = AnnotationUtils.getMethodsAnnotatedWith(Action.class, rule, true);
        if (actionMethods.isEmpty()) {
            throw new IllegalArgumentException(String.format("Rule '%s' must have at least one public method annotated with '%s'", rule.getClass().getName(), Action.class.getName()));
        }
        for (Method actionMethod : actionMethods) {
            if (!validateActionMethod(actionMethod)) {
                throw new IllegalArgumentException(String.format("Action method '%s' defined in rule '%s' must be public, must return void type and may have parameters annotated with @Param (and/or exactly one parameter of type or extending Facts).", actionMethod, rule.getClass().getName()));
            }
        }
    }

    private void checkPriorityMethod(final Object rule) {
        List<Method> priorityMethods = AnnotationUtils.getMethodsAnnotatedWith(Priority.class, rule, true);
        if (priorityMethods.isEmpty()) {
            return;
        }
        if (priorityMethods.size() > 1) {
            throw new IllegalArgumentException(String.format("Rule '%s' must have exactly one method annotated with '%s'", rule.getClass().getName(), Priority.class.getName()));
        }
        Method priorityMethod = priorityMethods.get(0);
        if (!validatePriorityMethod(priorityMethod)) {
            throw new IllegalArgumentException(String.format("Priority method '%s' defined in rule '%s' must be public, have no parameters and return integer type.", priorityMethod, rule.getClass().getName()));
        }
    }

    private boolean validateRuleClass(final Object rule) {
        return AnnotationUtils.isAnnotationPresent(Rule.class, rule.getClass());
    }

    private boolean validatePriorityMethod(final Method method) {
        return Modifier.isPublic(method.getModifiers())
                && method.getReturnType().equals(Integer.TYPE)
                && method.getParameterTypes().length == 0;
    }

    private boolean validateConditionMethod(final Method method) {
        return Modifier.isPublic(method.getModifiers())
                && method.getReturnType().equals(Boolean.TYPE)
                && validParameters(method);
    }

    private boolean validateActionMethod(final Method method) {
        return Modifier.isPublic(method.getModifiers())
                && (
                method.getReturnType().equals(Void.TYPE)
                        || method.getReturnType().equals(Boolean.class)
                        || method.getReturnType().equals(boolean.class)
        )
                && validParameters(method);
    }

    private boolean validParameters(final Method method) {
        int notAnnotatedParameterCount = 0;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (Annotation[] annotations : parameterAnnotations) {
            if (annotations.length == 0) {
                notAnnotatedParameterCount += 1;
            } else {
                //Annotation types has to be Param
                for (Annotation annotation : annotations) {
                    if (!annotation.annotationType().equals(Param.class)) {
                        return false;
                    }
                }
            }
        }
        if (notAnnotatedParameterCount > 1) {
            return false;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1 && notAnnotatedParameterCount == 1) {
            return RuleContext.class.isAssignableFrom(parameterTypes[0]);
        }
        return true;
    }
}
