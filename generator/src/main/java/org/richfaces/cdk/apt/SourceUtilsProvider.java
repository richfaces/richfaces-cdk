/*
 * $Id$
 *
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */
package org.richfaces.cdk.apt;

import javax.annotation.processing.ProcessingEnvironment;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@Singleton
public class SourceUtilsProvider implements Provider<SourceUtils> {
    private SourceUtils utils;
    private final ReflectionUtils defaultUtils;
    private final Injector injector;

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param defaultUtils
     */
    @Inject
    public SourceUtilsProvider(ReflectionUtils defaultUtils, Injector injector) {
        this.defaultUtils = defaultUtils;
        this.utils = defaultUtils;
        this.injector = injector;
    }

    @Override
    public SourceUtils get() {
        return utils;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param processingEnv the processingEnv to set
     */
    public void setProcessingEnv(ProcessingEnvironment processingEnv) {
        if (null != processingEnv) {
            this.utils = new AptSourceUtils(processingEnv);
            injector.injectMembers(this.utils);
        } else {
            this.utils = this.defaultUtils;
        }
    }
}
