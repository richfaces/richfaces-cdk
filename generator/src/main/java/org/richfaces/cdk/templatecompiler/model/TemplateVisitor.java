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
package org.richfaces.cdk.templatecompiler.model;

import org.richfaces.cdk.CdkException;

/**
 * <p class="changed_added_4_0">
 * </p>
 *
 * @author asmirnov@exadel.com
 * @author Lukas Fryc
 */
public interface TemplateVisitor {

    void visitElement(CdkCallElement cdkCallElement) throws CdkException;

    void visitElement(String child) throws CdkException;

    void startElement(AnyElement anyElement) throws CdkException;

    void endElement(AnyElement anyElement) throws CdkException;

    void startElement(CdkBodyElement cdkBodyElement) throws CdkException;

    void endElement(CdkBodyElement cdkBodyElement) throws CdkException;

    void startElement(CdkIfElement cdkIfElement);

    void endElement(CdkIfElement cdkIfElement);

    void startElement(CdkWhenElement cdkWhenElement);

    void endElement(CdkWhenElement cdkWhenElement);

    void startElement(CdkChooseElement cdkChooseElement);

    void endElement(CdkChooseElement cdkChooseElement);

    void startElement(CdkOtherwiseElement cdkOtherwiseElement);

    void endElement(CdkOtherwiseElement cdkOtherwiseElement);

    void visitElement(CdkObjectElement cdkObjectElement);

    void startElement(CdkForEachElement cdkForEachElement);

    void endElement(CdkForEachElement cdkForEachElement);

    void startElement(CdkSwitchElement cdkSwitchElement);

    void endElement(CdkSwitchElement cdkSwitchElement);

    void startElement(CdkCaseElement cdkCaseElement);

    void endElement(CdkCaseElement cdkCaseElement);

    void startElement(CdkDefaultElement cdkDefaultElement);

    void endElement(CdkDefaultElement cdkDefaultElement);

    void preProcess(CompositeImplementation compositeImplementation);

    void postProcess(CompositeImplementation compositeImplementation);

    void startElement(CdkScriptObjectElement cdkScriptObjectElement);

    void endElement(CdkScriptObjectElement cdkScriptObjectElement);

    void visitElement(CdkScriptOptionElement cdkScriptOptionElement);

    void startElement(CompositeRenderFacet compositeRenderFacetElement) throws CdkException;

    void endElement(CompositeRenderFacet compositeRenderFacetElement) throws CdkException;

    void preProcess(CdkFragmentElement fragmentElement);

    void postProcess(CdkFragmentElement fragmentElement);

    void visitElement(CdkRenderFragmentElement cdkRenderFragmentElement) throws CdkException;
}
