package org.okra.rules.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark rule condition.
 * Annotated method must be public and returns a boolean value.
 *
 * @see org.okra.rules.core.api.Rule#evaluate(org.okra.rules.core.RuleContext)
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Condition {

}
