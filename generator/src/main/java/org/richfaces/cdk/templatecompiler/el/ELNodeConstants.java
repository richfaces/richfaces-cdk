/**
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
package org.richfaces.cdk.templatecompiler.el;

public final class ELNodeConstants {
    // operators
    public static final String AND_OPERATOR = " && ";
    public static final String DIV_OPERATOR = " / ";
    public static final String EQUALITY_OPERATOR = " == ";
    public static final String GREATER_THEN_OPERATOR = " > ";
    public static final String GREATER_THEN_OR_EQUALITY_OPERATOR = " >= ";
    public static final String LESS_THEN_OPERATOR = " < ";
    public static final String LESS_THEN_OR_EQUALITY_OPERATOR = " <= ";
    public static final String INEQUALITY_OPERATOR = " != ";
    public static final String MINUS_OPERATOR = " - ";
    public static final String MOD_OPERATOR = " % ";
    public static final String MULT_OPERATOR = " * ";
    public static final String OR_OPERATOR = " || ";
    public static final String PLUS_OPERATOR = " + ";
    //
    // constant values
    public static final String FALSE_VALUE = "false";
    public static final String NULL_VALUE = "null";
    public static final String TRUE_VALUE = "true";
    //
    public static final String COLON = " : ";
    public static final String COMMA = ",";
    public static final String IS_EQUAL_FUNCTION = "isEqual";
    public static final String CONVERT_TO_STRING_FUNCTION = "convertToString";
    public static final String CONVERT_TO_BOOLEAN_FUNCTION = "convertToBoolean";
    public static final String CONVERT_TO_INTEGER_FUNCTION = "convertToInteger";
    public static final String CONVERT_TO_BYTE_FUNCTION = "convertToByte";
    public static final String CONVERT_TO_SHORT_FUNCTION = "convertToShort";
    public static final String CONVERT_TO_LONG_FUNCTION = "convertToLong";
    public static final String CONVERT_TO_FLOAT_FUNCTION = "convertToFloat";
    public static final String CONVERT_TO_DOUBLE_FUNCTION = "convertToDouble";
    public static final String CONVERT_TO_CHAR_FUNCTION = "convertToChar";
    public static final String DOT = ".";
    public static final String DOUBLE_VALUE_OF_FUNCTION = "Double.valueOf";
    public static final String EXCLAMATION_MARK = "!";
    public static final String GET_FUNCTION = "get";
    public static final String LEFT_BRACKET = "(";
    public static final String LEFT_SQUARE_BRACKET = "[";
    public static final String NEGATIVE = "-";
    public static final String QUESTION_SIGN = " ? ";
    public static final String RIGHT_BRACKET = ")";
    public static final String RIGHT_SQUARE_BRACKET = "]";
    public static final String THIS_PREFIX = "this";
    public static final String SUPER_PREFIX = "super";
    public static final String GETTER_PREFIX = "get";
    public static final String IS_EMPTY_FUNCTION = "isEmpty";

    private ELNodeConstants() {
    }
}
