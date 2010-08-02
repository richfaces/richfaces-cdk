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

package org.richfaces.cdk.test.functions;

import java.util.Collection;
import java.util.List;

import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.Function;
import org.richfaces.cdk.annotations.TagType;

/**
 * <p class="changed_added_4_0"></p>
 * @author asmirnov@exadel.com
 *
 */
public class TestFunctions {
    
    /**
     * <p class="changed_added_4_0"></p>
     * @param param
     * @return
     */
    @Function
    public static boolean testBoolean(String param){
        return false;
    }

    @Function(description=@Description(displayName="test string",value="Long, long description"))
    public static String testString(List<String> param, char delim){
        return null;
    }
    
    @Function(name="test")
    public static Collection<Integer> testCollection(String param,String...strings ){
        return null;
    }

    @Function(type=TagType.Jsp)
    public static boolean testJSPBoolean(String param){
        return false;
    }

}
