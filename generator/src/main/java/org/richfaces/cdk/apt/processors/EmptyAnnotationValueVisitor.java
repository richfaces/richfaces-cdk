/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.cdk.apt.processors;

import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author akolonitsky
 * @since Apr 1, 2010
 */
public class EmptyAnnotationValueVisitor<R> implements AnnotationValueVisitor<R, Object> {
    @Override
    public R visit(AnnotationValue av, Object o) {
        System.out.println("-1- AttributeProcessor.visit(" + av + ", " + o + ")");
        return null;
    }

    @Override
    public R visit(AnnotationValue av) {
        System.out.println("-2- AttributeProcessor.visit(" + av + ")");
        return null;
    }

    @Override
    public R visitBoolean(boolean b, Object o) {
        System.out.println("-3- AttributeProcessor.visit(" + b + ", " + o + ")");
        return null;
    }

    @Override
    public R visitByte(byte b, Object o) {
        System.out.println("-4- AttributeProcessor.visit(" + b + ", " + o + ")");
        return null;
    }

    @Override
    public R visitChar(char c, Object o) {
        System.out.println("-5- AttributeProcessor.visit(" + c + ", " + o + ")");
        return null;
    }

    @Override
    public R visitDouble(double d, Object o) {
        System.out.println("-6- AttributeProcessor.visit(" + d + ", " + o + ")");
        return null;
    }

    @Override
    public R visitFloat(float f, Object o) {
        System.out.println("-7- AttributeProcessor.visit(" + f + ", " + o + ")");
        return null;
    }

    @Override
    public R visitInt(int i, Object o) {
        System.out.println("-8- AttributeProcessor.visit(" + i + ", " + o + ")");
        return null;
    }

    @Override
    public R visitLong(long i, Object o) {
        System.out.println("-9- AttributeProcessor.visit(" + i + ", " + o + ")");
        return null;
    }

    @Override
    public R visitShort(short s, Object o) {
        System.out.println("-10- AttributeProcessor.visit(" + s + ", " + o + ")");
        return null;
    }

    @Override
    public R visitString(String s, Object o) {
        System.out.println("-11- AttributeProcessor.visit(" + s + ", " + o + ")");
        return null;
    }

    @Override
    public R visitType(TypeMirror t, Object o) {
        System.out.println("-12- AttributeProcessor.visit(" + t + ", " + o + ")");
        return null;
    }

    @Override
    public R visitEnumConstant(VariableElement c, Object o) {
        System.out.println("-13- AttributeProcessor.visit(" + c + ", " + o + ")");
        return null;
    }

    @Override
    public R visitAnnotation(AnnotationMirror a, Object o) {
        System.out.println("-14- AttributeProcessor.visit(" + a + ", " + o + ")");
        return null;
    }

    @Override
    public R visitArray(List<? extends AnnotationValue> vals, Object o) {
        System.out.println("-15- AttributeProcessor.visit(" + vals + ", " + o + ")");
        return null;
    }

    @Override
    public R visitUnknown(AnnotationValue av, Object o) {
        System.out.println("-16- AttributeProcessor.visit(" + av + ", " + o + ")");
        return null;
    }
}
