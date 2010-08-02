package org.richfaces.cdk.templatecompiler.builder.model;

import org.richfaces.cdk.templatecompiler.el.ParsingException;
import org.richfaces.cdk.templatecompiler.el.types.ELType;

public interface Variables {
    
    public ELType getVariable(String name) throws ParsingException;

    public boolean isDefined(String name) throws ParsingException;

    public ELType setVariable(String name, ELType type) throws ParsingException;

}
