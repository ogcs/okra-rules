package org.okra.rules.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Java bean reference {@link org.okra.rules.core.api.Rule}'s method.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Reference {

    /**
     * @return reflection {@link org.okra.rules.core.api.Rule}'s ref method.
     */
    RuleMethodName ref() default RuleMethodName.TOSTRING;

}
