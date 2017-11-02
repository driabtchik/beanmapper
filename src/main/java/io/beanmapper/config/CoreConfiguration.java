package io.beanmapper.config;

import java.util.ArrayList;
import java.util.List;

import io.beanmapper.annotations.BeanCollectionUsage;
import io.beanmapper.core.BeanMatchStore;
import io.beanmapper.core.collections.CollectionHandler;
import io.beanmapper.core.constructor.BeanInitializer;
import io.beanmapper.core.constructor.DefaultBeanInitializer;
import io.beanmapper.core.converter.BeanConverter;
import io.beanmapper.core.unproxy.BeanUnproxy;
import io.beanmapper.core.unproxy.DefaultBeanUnproxy;
import io.beanmapper.core.unproxy.SkippingBeanUnproxy;
import io.beanmapper.dynclass.ClassStore;
import io.beanmapper.exceptions.BeanConfigurationOperationNotAllowedException;

public class CoreConfiguration implements Configuration {

    /**
     * Initializes the beans.
     */
    private BeanInitializer beanInitializer = new DefaultBeanInitializer();

    /**
     * Removes any potential proxies of beans.
     */
    private SkippingBeanUnproxy beanUnproxy = new SkippingBeanUnproxy(new DefaultBeanUnproxy());

    /**
     * Contains all the handlers for collections. Also used within BeanMatchStore, so must be declared
     * here.
     */
    private CollectionHandlerStore collectionHandlerStore = new CollectionHandlerStore();

    /**
     * Contains a store of matches for source and target class pairs. A pair is created only
     * once and reused every time thereafter.
     */
    private BeanMatchStore beanMatchStore = new BeanMatchStore(collectionHandlerStore, beanUnproxy);

    /**
     * Contains a store of classes that are generated by using the MapToDynamicClassStrategy.
     * Every generated class is a downsized source class or a downsized target class.
     */
    private ClassStore classStore = new ClassStore();

    /**
     * The list of packages (and subpackages) containing classes which are eligible for mapping.
     */
    private List<String> packagePrefixes = new ArrayList<String>();

    /**
     * The list of converters that should be checked for conversions.
     */
    private List<BeanConverter> beanConverters = new ArrayList<BeanConverter>();

    /**
     * The list of converters that should be checked for conversions.
     */
    private List<BeanPair> beanPairs = new ArrayList<BeanPair>();

    /**
     * The value that decides whether a converter may be chosen, or direct mapping has to take place
     */
    private Boolean converterChoosable;

    private boolean addDefaultConverters = true;

    private StrictMappingProperties strictMappingProperties = StrictMappingProperties.defaultConfig();

    private CollectionFlusher collectionFlusher = new CollectionFlusher();

    private boolean flushEnabled = false;

    @Override
    public List<String> getDownsizeTarget() { return null; }

    @Override
    public List<String> getDownsizeSource() { return null; }

    @Override
    public Class getTargetClass() {
        return null;
    }

    @Override
    public Object getTarget() {
        return null;
    }

    @Override
    public Object getParent() { return null; }

    @Override
    public Class getCollectionClass() {
        return null;
    }

    @Override
    public CollectionHandler getCollectionHandlerForCollectionClass() {
        return null;
    }

    @Override
    public CollectionHandler getCollectionHandlerFor(Class<?> clazz) {
        return collectionHandlerStore.getCollectionHandlerFor(clazz, getBeanUnproxy());
    }

    @Override
    public BeanInitializer getBeanInitializer() {
        return this.beanInitializer;
    }

    @Override
    public BeanUnproxy getBeanUnproxy() {
        return this.beanUnproxy;
    }

    @Override
    public BeanMatchStore getBeanMatchStore() {
        return this.beanMatchStore;
    }

    @Override
    public ClassStore getClassStore() {
        return this.classStore;
    }

    @Override
    public List<String> getPackagePrefixes() {
        return this.packagePrefixes;
    }

    @Override
    public List<BeanConverter> getBeanConverters() {
        return this.beanConverters;
    }

    @Override
    public List<CollectionHandler> getCollectionHandlers() {
        return collectionHandlerStore.getCollectionHandlers();
    }

    @Override
    public List<BeanPair> getBeanPairs() {
        return this.beanPairs;
    }

    @Override
    public Boolean isConverterChoosable() {
        return converterChoosable == null ? false : converterChoosable;
    }

    @Override
    public void withoutDefaultConverters() {
        this.addDefaultConverters = false;
    }

    @Override
    public String getStrictSourceSuffix() {
        return strictMappingProperties.getStrictSourceSuffix();
    }

    @Override
    public String getStrictTargetSuffix() {
        return strictMappingProperties.getStrictTargetSuffix();
    }

    @Override
    public Boolean isApplyStrictMappingConvention() {
        return strictMappingProperties.isApplyStrictMappingConvention();
    }

    @Override
    public StrictMappingProperties getStrictMappingProperties() {
        this.strictMappingProperties.setBeanUnproxy(beanUnproxy);
        return this.strictMappingProperties;
    }

    @Override
    public BeanCollectionUsage getCollectionUsage() {
        return BeanCollectionUsage.CLEAR;
    }

    @Override
    public Class<?> getPreferredCollectionClass() {
        return null;
    }

    @Override
    public CollectionFlusher getCollectionFlusher() {
        return this.collectionFlusher;
    }

    @Override
    public boolean isFlushAfterClear() {
        return false;
    }

    @Override
    public boolean isFlushEnabled() {
        return this.flushEnabled;
    }

    @Override
    public boolean mustFlush() {
        return isFlushEnabled() && isFlushAfterClear();
    }

    @Override
    public void addConverter(BeanConverter converter) {
        this.beanConverters.add(converter);
    }

    @Override
    public void addCollectionHandler(CollectionHandler collectionHandler) {
        this.collectionHandlerStore.add(collectionHandler);
    }

    @Override
    public void addBeanPairWithStrictSource(Class source, Class target) {
        this.beanPairs.add(new BeanPair(source, target).withStrictSource());
    }

    @Override
    public void addBeanPairWithStrictTarget(Class source, Class target) {
        this.beanPairs.add(new BeanPair(source, target).withStrictTarget());
    }

    @Override
    public void addProxySkipClass(Class<?> clazz) {
        this.beanUnproxy.skip(clazz);
    }

    @Override
    public void addPackagePrefix(Class<?> clazz) {
        if (clazz.getPackage() != null) {
            addPackagePrefix(clazz.getPackage().getName());
        }
    }

    @Override
    public void addPackagePrefix(String packagePrefix) {
        this.packagePrefixes.add(packagePrefix);
    }

    @Override
    public void addAfterClearFlusher(AfterClearFlusher afterClearFlusher) {
        this.collectionFlusher.addAfterClearFlusher(afterClearFlusher);
    }

    @Override
    public void setBeanInitializer(BeanInitializer beanInitializer) {
        this.beanInitializer = beanInitializer;
    }

    @Override
    public void setBeanUnproxy(BeanUnproxy beanUnproxy) {
        this.beanUnproxy.setDelegate(beanUnproxy);
    }

    public boolean isAddDefaultConverters() {
        return this.addDefaultConverters;
    }

    @Override
    public void setConverterChoosable(boolean converterChoosable) {
        this.converterChoosable = converterChoosable;
    }

    @Override
    public void downsizeSource(List<String> includeFields) {
        throw new BeanConfigurationOperationNotAllowedException(
                "Illegal to set a include fields on the Core configuration, works only for override configurations");
    }

    @Override
    public void downsizeTarget(List<String> includeFields) {
        throw new BeanConfigurationOperationNotAllowedException(
                "Illegal to set a include fields on the Core configuration, works only for override configurations");
    }

    @Override
    public void setCollectionClass(Class collectionClass) {
        throw new BeanConfigurationOperationNotAllowedException(
                "Illegal to set a target instance on the Core configuration, works only for override configurations");
    }

    @Override
    public void setTargetClass(Class targetClass) {
        throw new BeanConfigurationOperationNotAllowedException(
                "Illegal to set target class on the Core configuration, works only for override configurations");
    }

    @Override
    public void setTarget(Object target) {
        throw new BeanConfigurationOperationNotAllowedException(
                "Illegal to set a target instance on the Core configuration, works only for override configurations");
    }

    @Override
    public void setParent(Object parent) {
        throw new BeanConfigurationOperationNotAllowedException(
                "Illegal to set a parent instance on the Core configuration, works only for override configurations");
    }

    @Override
    public boolean canReuse() {
        return false;
    }

    @Override
    public Class determineTargetClass() {
        return getTargetClass() == null ? getTarget().getClass() : getTargetClass();
    }

    @Override
    public void setStrictSourceSuffix(String strictSourceSuffix) {
        this.strictMappingProperties.setStrictSourceSuffix(strictSourceSuffix);
    }

    @Override
    public void setStrictTargetSuffix(String strictTargetSuffix) {
        this.strictMappingProperties.setStrictTargetSuffix(strictTargetSuffix);
    }

    @Override
    public void setApplyStrictMappingConvention(Boolean applyStrictMappingConvention) {
        this.strictMappingProperties.setApplyStrictMappingConvention(applyStrictMappingConvention);
    }

    @Override
    public void setCollectionUsage(BeanCollectionUsage collectionUsage) {
        throw new BeanConfigurationOperationNotAllowedException(
                "Illegal to set collection usage on the core configuration");
    }

    @Override
    public void setPreferredCollectionClass(Class<?> preferredCollectionClass) {
        throw new BeanConfigurationOperationNotAllowedException(
                "Illegal to set preferred collection class on the core configuration");
    }

    @Override
    public void setFlushAfterClear(boolean flushAfterClear) {
        throw new BeanConfigurationOperationNotAllowedException(
                "Illegal to set flush after clear on the core configuration");
    }

    @Override
    public void setFlushEnabled(boolean flushEnabled) {
        this.flushEnabled = flushEnabled;
    }

}
