package org.okra.rules.util;

import java.lang.invoke.SerializedLambda;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * @author TinyZ.
 * @version 2019.05.02
 */
public abstract class Lambdas {

    private static final Map<Class<?>, SerializedLambda> cache = new WeakHashMap<>();

    private Lambdas() {
        //  no-op
    }

    public static Optional<SerializedLambda> get(Class<?> clz) {
        return Optional.ofNullable(cache.get(clz));
    }

    public static SerializedLambda put(Class<?> clz, SerializedLambda serializedLambda) {
        return cache.put(clz, serializedLambda);
    }

}
