package org.okra.rules.spring;

import org.okra.rules.annotation.Rule;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.Set;
import java.util.stream.Stream;

public class ClassPathRuleScanner extends ClassPathBeanDefinitionScanner {

    private RuleFactoryBean<?> factoryBean = new RuleFactoryBean<>();

    public ClassPathRuleScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public ClassPathRuleScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public ClassPathRuleScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    public ClassPathRuleScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

    public void registerFilters() {
        //  1. Rule annotation.
        this.addIncludeFilter((metadataReader, metadataReaderFactory) -> metadataReader.getAnnotationMetadata().hasAnnotation(Rule.class.getName()));
        //  2. this class implement Rule interface.
        this.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            if (metadataReader.getClassMetadata().isConcrete()
                    && metadataReader.getClassMetadata().hasSuperClass()) {
                return Stream.of(metadataReader.getClassMetadata().getInterfaceNames())
                        .allMatch(clzName -> org.okra.rules.core.api.Rule.class.getName().equalsIgnoreCase(clzName));
            }
            return false;
        });
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
        if (!holders.isEmpty()) {
//            processBeanDefinitions(holders);
        }
        return holders;
    }

    @Override
    public boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
        return super.checkCandidate(beanName, beanDefinition);
    }

    @Override
    protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
        super.registerBeanDefinition(definitionHolder, registry);
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();

            if (logger.isDebugEnabled()) {
                logger.debug("Creating MapperFactoryBean with name '" + holder.getBeanName()
                        + "' and '" + definition.getBeanClassName() + "' mapperInterface");
            }

            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is MapperFactoryBean
            definition.getConstructorArgumentValues().addGenericArgumentValue(definition.getBeanClassName()); // issue #59
            definition.setBeanClass(this.factoryBean.getClass());
//            definition.getPropertyValues().add("addToConfig", this.addToConfig);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        }
    }


}
