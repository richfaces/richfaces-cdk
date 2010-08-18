package org.richfaces.cdk.templatecompiler.builder.model;


import java.util.Collections;

import com.google.common.base.Function;

public interface RequireImports {
    public static final Function<RequireImports, Iterable<JavaImport>> IMPORTS_TRANSFORM = new Function<RequireImports, Iterable<JavaImport>>() {
        
        @Override
        public Iterable<JavaImport> apply(RequireImports from) {
            if(null == from){
                return Collections.emptySet();
            }
            return from.getRequiredImports();
        }
    };

    public Iterable<JavaImport> getRequiredImports();

}