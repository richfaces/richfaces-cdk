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

import static org.junit.Assert.*;

import java.util.Collections;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.templatecompiler.builder.model.JavaField;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
@RunWith(CdkTestRunner.class)
public class WriteAttributesSetTest extends FreeMarkerTestBase {

    @Inject
    private WriteAttributesSetStatement statement;

    /**
     * Test method for {@link org.richfaces.cdk.templatecompiler.statements.FreeMarkerTemplateStatementBase#getCode()}.
     */
    @Test
    public void testGetCode() {
        WriteAttributesSetStatement.PassThrough passThrough = new WriteAttributesSetStatement.PassThrough();
        passThrough.name = QName.valueOf("foo");
        passThrough.type = "String";
        passThrough.componentAttribute = "bar";
        passThrough.defaultValue = "deflt";
        controller.replay();
        statement.setAttributes(Collections.singleton(passThrough));
        String code = statement.getCode();
        controller.verify();
        verifyCode(code, "!attributes()", "!defaultValue(", "!if(", "");
        verifyHelpers(statement, HelperMethod.CREATE_ATTRIBUTES, HelperMethod.RENDER_ATTRIBUTES_SET);
        JavaField javaField = Iterables.getOnlyElement(statement.getRequiredFields());
        verifyCode(javaField.getValue().getCode(), "attributes()", "generic(", "defaultValue(");
    }

}
