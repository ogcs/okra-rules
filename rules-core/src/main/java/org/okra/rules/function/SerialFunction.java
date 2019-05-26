package org.okra.rules.function;

import org.okra.rules.util.Lambdas;

import java.beans.Introspector;
import java.io.Serializable;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * @author TinyZ.
 * @version 2019.05.02
 */
@FunctionalInterface
public interface SerialFunction<T, R> extends Function<T, R>, Serializable {

    /**
     * serialize lambda function.
     */
    default SerializedLambda serialized() {
        if (!getClass().isSynthetic())
            throw new IllegalArgumentException("clz is not synthetic");
        return Lambdas.get(getClass()).orElseGet(() -> {
            try {
                Method method = getClass().getDeclaredMethod("writeReplace");
                method.trySetAccessible();
                SerializedLambda serializedLambda = (SerializedLambda) method.invoke(this);
                Lambdas.put(getClass(), serializedLambda);
                return serializedLambda;
            } catch (Exception e) {
                throw new RuntimeException("serialized failed.", e);
            }
        });
    }

    /**
     * Lambda function reference method name.
     */
    default String lambdaFieldName(T bean) {
        SerializedLambda serializedLambda = serialized();
        if (MethodHandleInfo.REF_invokeVirtual != serializedLambda.getImplMethodKind()) {
            throw new RuntimeException("unsupported lambda impl method kind " + serializedLambda.getImplMethodKind()
                    + ". Only work for [Class::Method] struct's lambda");
        }
        String getter = serializedLambda.getImplMethodName();
        String fieldName = getter;
        if (getter.startsWith("get")) {
            fieldName = Introspector.decapitalize(getter.substring(3));
        } else if (getter.startsWith("is")) {
            String guessFieldName = Introspector.decapitalize(getter.substring(2));
            if (null == bean) {
                fieldName = guessFieldName;
            } else {
                try {
                    bean.getClass().getDeclaredField(getter);
                    fieldName = getter;
                } catch (NoSuchFieldException e) {
                    //  ignore substring when the field name is "isXxx"
                    fieldName = guessFieldName;
                }
            }
        }
        return fieldName;
    }
}
