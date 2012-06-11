package org.richfaces.cdk.templatecompiler.el.types;

public class GeneratedType extends ReferencedType {

    private ELType superType;

    public GeneratedType(String classCodeString, ELType superType) {
        super(classCodeString);
        this.superType = superType;
    }

    public ELType getSuperType() {
        return superType;
    }

}
