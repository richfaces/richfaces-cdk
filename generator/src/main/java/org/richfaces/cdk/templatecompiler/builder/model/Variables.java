package org.richfaces.cdk.templatecompiler.builder.model;

import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;

public interface Variables {
    ELType getVariable(String name) throws ParsingException;

    boolean isDefined(String name) throws ParsingException;

    ELType setVariable(String name, ELType type) throws ParsingException;
}
