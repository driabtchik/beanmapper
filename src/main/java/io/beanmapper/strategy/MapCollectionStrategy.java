package io.beanmapper.strategy;

import io.beanmapper.BeanMapper;
import io.beanmapper.config.Configuration;
import io.beanmapper.core.collections.CollectionHandler;

@SuppressWarnings("unchecked")
public class MapCollectionStrategy extends AbstractMapStrategy {

    public MapCollectionStrategy(BeanMapper beanMapper, Configuration configuration) {
        super(beanMapper, configuration);
    }

    @Override
    public Object map(Object source) {

        CollectionHandler collectionHandler = getConfiguration().getCollectionHandlerForCollectionClass();

        Object targetItems = collectionHandler.getTargetCollection(
            this.getConfiguration().getCollectionUsage(),
            this.getConfiguration().getPreferredCollectionClass(),
            this.getConfiguration().getTarget(),
            this.getConfiguration().getCollectionFlusher(),
            this.getConfiguration().mustFlush()
        );

        targetItems = collectionHandler.copy(
                getBeanMapper(),
                getConfiguration().getTargetClass(),
                source,
                targetItems);

        return targetItems;
    }

}
