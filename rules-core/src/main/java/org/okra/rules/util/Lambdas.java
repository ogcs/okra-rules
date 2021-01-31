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
