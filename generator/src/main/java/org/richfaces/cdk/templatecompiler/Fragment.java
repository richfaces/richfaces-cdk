package org.richfaces.cdk.templatecompiler;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.richfaces.cdk.templatecompiler.builder.model.Argument;
import org.richfaces.cdk.templatecompiler.el.types.ELType;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.model.CdkFragmentElement;
import org.richfaces.cdk.templatecompiler.model.CompositeAttribute;

import com.google.common.collect.Maps;

public class Fragment {

    private String methodName;

    private Map<String, Argument> arguments = Maps.newLinkedHashMap();

    public Fragment(CdkFragmentElement fragmentElement, TypesFactory typesFactory) {
        methodName = fragmentElement.getName();

        List<CompositeAttribute> attributes = fragmentElement.getFragmentInterface().getAttributes();

        for (CompositeAttribute attribute : attributes) {
            String name = attribute.getName();
            String typeName = attribute.getType();
            ELType type = typesFactory.getType(typeName);

            Argument argument = new Argument(name, type);
            arguments.put(name, argument);
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public Argument getArgumentByName(String argumentName) {
        return arguments.get(argumentName);
    }

    public Collection<Argument> getAllArguments() {
        return Collections.unmodifiableCollection(arguments.values());
    }
}
