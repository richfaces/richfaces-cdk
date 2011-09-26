package org.richfaces.cdk;

import org.richfaces.application.ServiceException;
import org.richfaces.application.ServicesFactory;
import org.richfaces.resource.ResourceLibraryFactory;
import org.richfaces.resource.ResourceLibraryFactoryImpl;

public class MockServiceFactory implements ServicesFactory {

    private ResourceLibraryFactory resourceLibraryFactory = new ResourceLibraryFactoryImpl();

    @Override
    public <T> T getInstance(Class<T> type) throws ServiceException {
        if (type == ResourceLibraryFactory.class) {
            return type.cast(resourceLibraryFactory);
        }
        throw new IllegalArgumentException("factory of following type is not supported: " + type);
    }

    @Override
    public <T> void setInstance(Class<T> type, T instance) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void release() {
        resourceLibraryFactory = null;
    }

}
