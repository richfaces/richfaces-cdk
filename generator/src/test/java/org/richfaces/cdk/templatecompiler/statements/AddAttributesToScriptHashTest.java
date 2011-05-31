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
package org.richfaces.cdk.templatecompiler.statements;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkClassLoader;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.PropertyBase;
import org.richfaces.cdk.model.PropertyModel;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactoryImpl;

import com.google.common.collect.Iterables;
import com.google.inject.Binder;
import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
@RunWith(CdkTestRunner.class)
public class AddAttributesToScriptHashTest extends FreeMarkerTestBase {
    @Inject
    private TypesFactory typesFactory;
    @Inject
    private ScriptObjectStatement parentStatement;
    @Inject
    private AddAttributesToScriptHashStatement statement;

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(TypesFactory.class).to(TypesFactoryImpl.class);
        binder.bind(CdkClassLoader.class).toInstance(createClassLoader());
    }

    /**
     * Test method for {@link org.richfaces.cdk.templatecompiler.statements.FreeMarkerTemplateStatementBase#getCode()}.
     */
    @Test
    public void testGetCode() {
        statement.setParent(parentStatement);
        parentStatement.setObject("hash", null);

        PropertyBase property = new PropertyModel();
        property.setName("bar");
        property.setDefaultValue("deflt");
        property.setType(ClassName.get(String.class));

        controller.replay();
        statement.setAttributes(Collections.singleton("bar"), Collections.singleton(property));
        String code = statement.getCode();
        controller.verify();
        verifyCode(code, "!attributes()", "!defaultValue(", "!if(", "");
        verifyHelpers(statement, HelperMethod.CREATE_ATTRIBUTES, HelperMethod.ADD_TO_SCRIPT_HASH_ATTRIBUTES);
        JavaField javaField = Iterables.getOnlyElement(statement.getRequiredFields());
        verifyCode(javaField.getValue().getCode(), "attributes()", "generic(", "defaultValue(");
    }
}
