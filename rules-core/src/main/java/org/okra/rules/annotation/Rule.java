package org.okra.rules.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Rule {

    /**
     * The rule identify which must be unique within an rules registry.
     *
     * @return The rule identify
     */
    String identify() default "";

    /**
     * The rule description.
     *
     * @return The rule description
     */
    String description() default "";

    /**
     * The rule priority.
     *
     * @return The rule priority
     */
    int priority() default 0;

}
