package org.okra.rules.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark rule action.
 * Annotated method must be public.
 *
 * @see org.okra.rules.core.api.Rule#execute(org.okra.rules.core.RuleContext)
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Action {

    /**
     * The order in which the action should be executed.
     *
     * @return the order in which the action should be executed
     */
    int order() default 0;

}
