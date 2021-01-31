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

import org.okra.rules.annotation.Action;
import org.okra.rules.annotation.Condition;
import org.okra.rules.annotation.Param;
import org.okra.rules.annotation.Priority;
import org.okra.rules.annotation.Reference;
import org.okra.rules.annotation.Rule;
import org.okra.rules.annotation.RuleMethodName;
import org.okra.rules.annotation.Watchers;
import org.okra.rules.exception.NoSuchParameterException;
import org.okra.rules.util.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @see DefaultRule
 */
public class BasicRuleProxy implements InvocationHandler, org.okra.rules.core.api.Rule {

    private static final Logger LOG = LoggerFactory.getLogger(BasicRuleProxy.class);
    private static final long serialVersionUID = 477657902202548571L;
    private final Object obj;
    private String identify;
    private String description;
    private int priority;
    private Map<String, Object> basicMethodMap = new HashMap<>();
    private Map<String, Object> defaultMethodMap = new HashMap<>();
    private Map<String, Object> otherMethodMap = new HashMap<>();

    public BasicRuleProxy(final Object obj) {
        Objects.requireNonNull(obj, "obj");
        this.obj = obj;
    }

    /**
     * initialize.
     */
    public void initialize() {
        Objects.requireNonNull(this.obj);
        lookupReferenceAnnotation();
        lookupPriorityAnnotation();
        lookupRuleAnnotation();
        lookupConditionAnnotation();
        lookupActionAnnotation();
        lookupWatchersAnnotation();
        registerMissingAnnotationMethodByMethodName();

        registerObjectCustomMethod();

        replaceObjectUnImplementsMethodByLocalImpl();
    }

    private void lookupReferenceAnnotation() {
        //  Look up reference
        Stream.of(obj.getClass().getDeclaredMethods())
                .filter((method) -> method.isAnnotationPresent(Reference.class))
                .forEach((method) -> {
                    Reference annotation = method.getAnnotation(Reference.class);
                    if (null != annotation) {
                        basicMethodMap.putIfAbsent(annotation.ref().name(), method);
                    }
                });
    }

    private void lookupPriorityAnnotation() {
        //  Look up priority method.
        Stream.of(obj.getClass().getMethods())
                .filter((method) -> method.isAnnotationPresent(Priority.class))
                .findFirst()
                .ifPresent((method) -> basicMethodMap.putIfAbsent(RuleMethodName.GETPRIORITY.name(), method));
    }

    private void lookupRuleAnnotation() {
        Rule rule = AnnotationUtils.findAnnotation(Rule.class, this.obj.getClass());
        if (rule != null) {
            if (!org.okra.rules.core.api.Rule.DEFAULT_IDENTIFY.equals(rule.identify())) {
                this.identify = rule.identify();
            }
            if (!org.okra.rules.core.api.Rule.DEFAULT_DESCRIPTION.equals(rule.description())) {
                this.description = rule.description();
            }
            if (org.okra.rules.core.api.Rule.DEFAULT_PRIORITY != rule.priority()) {
                this.priority = rule.priority();
            }
        } else {
            this.identify = this.obj.getClass().getSimpleName();
            this.description = this.obj.getClass().toString();
            this.priority = org.okra.rules.core.api.Rule.DEFAULT_PRIORITY;
        }
    }

    private void lookupConditionAnnotation() {
        //  Look up condition method
        Stream.of(obj.getClass().getMethods())
                .filter((method) -> method.isAnnotationPresent(Condition.class))
                .findFirst()
                .ifPresent((method) -> basicMethodMap.putIfAbsent(RuleMethodName.EVALUATE.name(), method));
    }

    private void lookupActionAnnotation() {
        //  Look up action method
        List<RuleMethodBean> list = Stream.of(obj.getClass().getMethods())
                .filter((method) -> method.isAnnotationPresent(Action.class))
                .map((method) -> {
                    Action annotation = method.getAnnotation(Action.class);
                    return new RuleMethodBean(method, annotation.order());
                })
                .sorted(Comparator.comparingInt(RuleMethodBean::getOrder).reversed())
                .collect(Collectors.toList());
        if (!list.isEmpty()) {
            basicMethodMap.putIfAbsent(RuleMethodName.EXECUTE.name(), list);
        }
    }

    private void lookupWatchersAnnotation() {
        //  Look up watchers method
        Stream.of(obj.getClass().getMethods())
                .filter((method) -> method.isAnnotationPresent(Watchers.class))
                .findFirst()
                .ifPresent((method) -> basicMethodMap.putIfAbsent(RuleMethodName.GETWATCHERS.name(), method));
    }

    private void registerMissingAnnotationMethodByMethodName() {
        //   Look up method by basic method name
        Stream.of(obj.getClass().getMethods())
                .filter((method) -> Stream.of(RuleMethodName.values())
                        .anyMatch(methodName -> methodName.name().equalsIgnoreCase(method.getName())))
                .forEach((method) -> basicMethodMap.putIfAbsent(method.getName().toUpperCase(), method));
    }

    private void replaceObjectUnImplementsMethodByLocalImpl() {
        //  use default rule method replace missing undefined method.
        Stream<Method> stream = Stream.of(this.getClass().getMethods());
        stream.filter((method) -> Stream.of(RuleMethodName.values())
                .anyMatch((methodName) -> methodName.name().equalsIgnoreCase(method.getName())))
                .forEach((method) -> defaultMethodMap.putIfAbsent(method.getName().toUpperCase(), method));
    }

    /**
     * Register the obj's custom method.
     */
    private void registerObjectCustomMethod() {
        Set<String> objectMethoNameSet = Stream.of(Object.class.getMethods())
                .map(Method::getName)
                .collect(Collectors.toSet());
        Stream.of(obj.getClass().getMethods())
                .filter(method -> !objectMethoNameSet.contains(method.getName()))
                .filter(method -> !basicMethodMap.containsKey(method.getName().toUpperCase()))
                .forEach(method -> {
                    Object prevMethod = otherMethodMap.putIfAbsent(method.getName().toUpperCase(), method);
                    if (prevMethod != null) {
                        LOG.warn("exists has duplicate method name:{}.", prevMethod);
                    }
                });
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName().toUpperCase();
        Object basicMethod = basicMethodMap.get(methodName);
        if (basicMethod != null) {
            return invokeMethod(this.obj, methodName, basicMethod, args);
        }

        Object proxyDefaultMethod = defaultMethodMap.get(methodName);
        if (proxyDefaultMethod != null) {
            return invokeMethod(this, methodName, proxyDefaultMethod, args);
        }

        Object customMethod = otherMethodMap.get(methodName);
        if (customMethod != null) {
            return invokeMethod(this.obj, methodName, customMethod, args);
        }

        return null;
    }

    private Object invokeMethod(Object instance, String methodName, Object method, Object[] args) {
        if (method instanceof Method) {
            RuleContext context = null;
            if (null != args
                    && args.length == 1
                    && args[0] instanceof RuleContext) {
                context = (RuleContext) args[0];
            }
            Object[] parameters = getActualParameters((Method) method, context);
            try {
                return ((Method) method).invoke(instance, parameters);
            } catch (Exception e) {
                LOG.error("instance:{}, methodName:{}, method:{}, args:{}", instance, methodName, method, Arrays.toString(args), e);
                return null;
            }
        } else if (method instanceof List
                && RuleMethodName.EXECUTE.name().equalsIgnoreCase(methodName)) {
            try {
                RuleContext context = (RuleContext) args[0];
                for (Object subObj : (List) method) {
                    if (subObj instanceof RuleMethodBean) {
                        Method subMethod = ((RuleMethodBean) subObj).getMethod();
                        Object[] parameters = getActualParameters(subMethod, context);
                        subMethod.invoke(instance, parameters);
                    }
                }
            } catch (Exception e) {
                LOG.error("instance;{}, methodName:{}, obj:{}, args:{}", instance, methodName, method, Arrays.toString(args), e);
            }
        }
        return null;
    }

    private Object[] getActualParameters(Method method, RuleContext context) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length <= 0) {
            return null;
        }
        List<Object> list = new ArrayList<>();
        for (Parameter parameter : parameters) {
            //  get parameter if parameter is RuleContext
            Class<?> parameterType = parameter.getType();
            if (RuleContext.class.isAssignableFrom(parameterType)
                    && parameterType.isInstance(context)) {
                list.add(parameterType.cast(context));
                continue;
            }
            if (context != null) {
                //  get parameter by parameter's Param annotation
                Param annotation = parameter.getAnnotation(Param.class);
                if (null != annotation) {
                    list.add(context.get(annotation.value(), parameterType));
                    continue;
                }
                //  get parameter by parameter's name.
                if (context.containsKey(parameter.getName())) {
                    list.add(context.get(parameter.getName(), parameterType));
                    continue;
                }
            }
            throw new NoSuchParameterException("No such the parameter:" + parameter.getName() + ", context:" +
                    context, parameter.getName());
        }
        return list.toArray();
    }

    /// <editor-fold desc="Default Rule Implements">

    @Override
    public String id() {
        return this.identify;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public boolean evaluate(RuleContext context) {
        throw new UnsupportedOperationException("unimplemented method: evaluate()");
    }

    @Override
    public void execute(RuleContext context) {
        throw new UnsupportedOperationException("unimplemented method: execute()");
    }

    @Override
    public int compareTo(org.okra.rules.core.api.Rule o) {
        return this.getPriority() - o.getPriority();
    }
    /// </editor-fold>

    class RuleMethodBean {

        private final Method method;
        private final int order;

        RuleMethodBean(Method method, int order) {
            this.method = method;
            this.order = order;
        }

        public Method getMethod() {
            return method;
        }

        public int getOrder() {
            return order;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RuleMethodBean that = (RuleMethodBean) o;
            return order == that.order &&
                    Objects.equals(method, that.method);
        }

        @Override
        public int hashCode() {
            return Objects.hash(method, order);
        }
    }

}
