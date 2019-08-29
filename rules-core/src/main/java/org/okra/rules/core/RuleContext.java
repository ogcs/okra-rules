package org.okra.rules.core;

import org.okra.rules.function.SerialFunction;
import org.okra.rules.util.ConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Rule Engine Context. Non-ThreadSafe
 */
public class RuleContext {

    private static final Logger LOG = LoggerFactory.getLogger(RuleContext.class);
    /**
     * rule context parameter map.
     */
    private final Map<String, Object> params = new HashMap<>();
    /**
     * with key's prefix.
     */
    private volatile String prefix;

    /**
     * Put  key and value.
     *
     * @param key the param's key
     * @param obj the param's value
     * @see #with(String, Object)
     */
    @Deprecated(since = "2.1.0")
    public void put(String key, Object obj) {
        with(key, obj);
    }

    public RuleContext withNotNull(String key, Object obj) {
        Objects.requireNonNull(key, "key");
        return with(key, obj);
    }

    public RuleContext with(String key, Object obj) {
        String param = resolvePrefixName(key);
        Object prevObj = params.putIfAbsent(param, obj);
        if (prevObj != null) {
            LOG.debug("the field:{} has exists. val:{}", param, params.get(key));
        }
        return this;
    }

    private String resolvePrefixName(String name) {
        if (null == name) {
            return prefix == null ? "" : prefix;
        } else {
            return prefix == null ? name : prefix + "." + name;
        }
    }

    /// <editor-fold desc="--With  Bean--" defaultstate="collapsed">

    public RuleContext prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public RuleContext end() {
        this.prefix = null;
        return this;
    }

    /**
     * the prefix is bean class's simple name.
     *
     * @param bean the bean instance.
     */
    public RuleContext withBean(Object bean) {
        Objects.requireNonNull(bean, "bean");
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.trySetAccessible()) {
                    with(field.getName(), field.get(bean));
                }
            } catch (Exception e) {
                LOG.error("given params failure. field:{}", field.getName(), e);
            }
        }
        return with(null, bean);
    }

    public <T> RuleContext withProperty(T bean, SerialFunction<T, ?> func) {
        if (null == func)
            return this;
        String fieldName = func.lambdaFieldName(bean);
        return with(fieldName, func.apply(bean));
    }

    /// End

    public RuleContext withBeanEnd(Object bean) {
        return withBeanEnd(Introspector.decapitalize(bean.getClass().getSimpleName()), bean);
    }

    /**
     * Put all java bean 's field into params.
     *
     * @param prefix the java bean's map key.
     * @param bean   the java bean instance.
     */
    public RuleContext withBeanEnd(String prefix, Object bean) {
        Objects.requireNonNull(bean, "bean");
        return prefix(prefix)
                .withBean(bean)
                .end();
    }

    public <T> RuleContext withPropertyEnd(T bean, SerialFunction<T, ?>... fields) {
        return withPropertyEnd(Introspector.decapitalize(bean.getClass().getSimpleName()), bean, fields);
    }

    public <T> RuleContext withPropertyEnd(String prefix, T bean, SerialFunction<T, ?>... fields) {
        if (null == fields)
            return this;
        prefix(prefix);
        for (SerialFunction<T, ?> function : fields)
            withProperty(bean, function);
        return end();
    }

    /// </editor-fold>

    /**
     * this method is not type-safe.
     *
     * @see #get(String, Class)
     */
    @SuppressWarnings("unchecked")
    @Deprecated(since = "2.1.0")
    public <T> T get(String key) {
        return (T) params.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> requiredType) {
        return (T) ConverterUtil.covert(params.get(key), requiredType);
    }

    public boolean containsKey(String key) {
        return params.containsKey(key);
    }

    public Object remove(String key) {
        Objects.requireNonNull(key);
        return params.remove(key);
    }

    public Map<String, Object> params() {
        return this.params;
    }

    @Override
    public String toString() {
        return "RuleContext{" +
                "params=" + params +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}
