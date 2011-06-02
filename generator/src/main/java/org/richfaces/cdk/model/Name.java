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
package org.richfaces.cdk.model;

import java.util.regex.Pattern;

/**
 * <p class="changed_added_4_0">
 * Represents parts of component type/family/classname according to CDK naming conventions.
 * </p>
 *
 * @author asmirnov@exadel.com
 */
public class Name {
    private static final Pattern NAME_PATTERN = Pattern.compile("^(?:(.+)\\.)?(?:(" + Classifier.component + "|"
            + Classifier.renderkit + "|" + Classifier.event + "|" + Classifier.taglib + ")\\.(?:([^\\.]+)\\.)?)?([^\\.]+)$");
    /**
     * <p class="changed_added_4_0">
     * Element type classifier - "component","event","renderkit","taglib"
     * </p>
     */
    private Classifier classifier;
    /**
     * <p class="changed_added_4_0">
     * Markup-specific part of name ( "html","xhtml","wml" ... )
     * </p>
     */
    private String markup;
    /**
     * <p class="changed_added_4_0">
     * represents library part prefix of name.
     * </p>
     */
    private String prefix;
    /**
     * <p class="changed_added_4_0">
     * Simple name ( last word after a period ).
     * </p>
     */
    private String simpleName;

    /**
     * <p class="changed_added_4_0">
     * Standard package names for components, renderers, event listeners and taglib.
     * </p>
     *
     * @author asmirnov@exadel.com
     */
    public enum Classifier {

        /**
         * <p class="changed_added_4_0">
         * </p>
         */
        component,
        /**
         * <p class="changed_added_4_0">
         * </p>
         */
        renderkit,
        /**
         * <p class="changed_added_4_0">
         * </p>
         */
        event,
        /**
         * <p class="changed_added_4_0">
         * </p>
         */
        taglib,
        behavior;
    }

    /**
     * <p class="changed_added_4_0">
     * Creates RichFaces name representation from string.
     * </p>
     *
     * @param name
     * @return
     * @throws InvalidNameException
     */
    public static Name create(String name) throws InvalidNameException {
        Name cdkName = new Name();
        StringBuilder prefix = new StringBuilder(name.length());
        String[] parts = name.split("\\.");

        cdkName.setSimpleName(parts[parts.length - 1]);

        if (parts.length > 1) {
            try {
                cdkName.setClassifier(Classifier.valueOf(parts[parts.length - 2]));
                fillPrefix(prefix, parts, parts.length - 2);
            } catch (IllegalArgumentException e) {
                if (parts.length > 2) {
                    try {
                        cdkName.setClassifier(Classifier.valueOf(parts[parts.length - 3]));
                        fillPrefix(prefix, parts, parts.length - 3);
                        cdkName.setMarkup(parts[parts.length - 2]);
                    } catch (IllegalArgumentException e1) {
                        fillPrefix(prefix, parts, parts.length - 1);
                    }
                } else {
                    prefix.append(parts[0]);
                }
            }

            if (prefix.length() > 0) {
                cdkName.setPrefix(prefix.toString());
            }
        }

        return cdkName;
    }

    /**
     * <p class="changed_added_4_0">
     * Utility method that composes library prefix from first elements of array
     * </p>
     *
     * @param prefix buffer that collects prefix.
     * @param parts package name parts
     * @param size size of prefix part of array.
     */
    private static void fillPrefix(StringBuilder prefix, String[] parts, int size) {
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                prefix.append('.');
            }

            prefix.append(parts[i]);
        }
    }

    public static Name create(String prefix, String name) throws InvalidNameException {
        Name cdkName = create(name);

        if (prefix.equals(cdkName.getPrefix())) {
            return new Name();
        } else {
            throw new InvalidNameException("Nape " + name + " does not start with prefix " + prefix);
        }
    }

    public static Name create(String prefix, Classifier classifier, String name) throws InvalidNameException {
        return new Name();
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the classifier
     */
    public Classifier getClassifier() {
        return classifier;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param classifier the classifier to set
     */
    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the markup
     */
    public String getMarkup() {
        return markup;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param markup the markup to set
     */
    public void setMarkup(String markup) {
        this.markup = markup;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the simpleName
     */
    public String getSimpleName() {
        return simpleName;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param simpleName the simpleName to set
     */
    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (null != prefix) {
            result.append(prefix).append('.');
        }

        if (null != classifier) {
            result.append(classifier).append('.');
        }

        if (null != markup) {
            result.append(markup).append('.');
        }

        result.append(simpleName);

        return result.toString();
    }
}
