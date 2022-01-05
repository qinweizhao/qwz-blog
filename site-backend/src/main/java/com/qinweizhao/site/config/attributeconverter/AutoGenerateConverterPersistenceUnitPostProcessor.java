package com.qinweizhao.site.config.attributeconverter;

import com.qinweizhao.site.model.enums.ValueEnum;
import com.qinweizhao.site.model.properties.PropertyEnum;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.util.ClassUtils;

import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

/**
 * Attribute converter persistence unit post processor.
 *
 * @author johnniang
 */
class AutoGenerateConverterPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {

    private static final String PACKAGE_TO_SCAN = "com.qinweizhao.site";

    private final ConfigurableListableBeanFactory factory;

    public AutoGenerateConverterPersistenceUnitPostProcessor(
            ConfigurableListableBeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        var generator = new AttributeConverterAutoGenerator(factory.getBeanClassLoader());

        findValueEnumClasses()
                .stream()
                .map(generator::generate)
                .map(Class::getName)
                .forEach(pui::addManagedClassName);
    }

    private Set<Class<?>> findValueEnumClasses() {
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        // include ValueEnum class
        scanner.addIncludeFilter(new AssignableTypeFilter(ValueEnum.class));
        // exclude PropertyEnum class
        scanner.addExcludeFilter(new AssignableTypeFilter(PropertyEnum.class));

        return scanner.findCandidateComponents(PACKAGE_TO_SCAN)
                .stream()
                .filter(bd -> bd.getBeanClassName() != null)
                .map(bd -> ClassUtils.resolveClassName(bd.getBeanClassName(), null))
                .collect(toUnmodifiableSet());
    }
}
