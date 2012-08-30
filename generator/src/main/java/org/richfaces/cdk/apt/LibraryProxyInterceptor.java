package org.richfaces.cdk.apt;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.ComponentLibraryHolder;

import com.google.inject.Inject;

public class LibraryProxyInterceptor implements MethodInterceptor {

    @Inject
    private ComponentLibraryHolder holder;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        ComponentLibrary library = holder.getLibrary();
        return invocation.getMethod().invoke(library, invocation.getArguments());
    }

}
