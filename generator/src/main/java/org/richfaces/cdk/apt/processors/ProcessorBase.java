package org.richfaces.cdk.apt.processors;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import org.richfaces.cdk.NamingConventions;
import org.richfaces.cdk.annotations.Description;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.DescriptionGroup;
import org.richfaces.cdk.model.ModelElementBase;
import org.richfaces.cdk.model.TagModel;
import org.richfaces.cdk.util.Strings;

import com.google.inject.Inject;
import com.google.inject.Provider;

public abstract class ProcessorBase  {

    
    @Inject
    private Provider<SourceUtils> sourceUtils;

    @Inject
    private AttributesProcessor attributeProcessor;

    @Inject
    private NamingConventions namingConventions;
    
    @Inject
    private DescriptionProcessor descriptionProcessor;

    public SourceUtils getSourceUtils() {
        return this.sourceUtils.get();
    }

    protected void setTagInfo(Tag tag, ModelElementBase model) {

        if (!TagType.None.equals(tag.type())) {
            TagModel tagModel = processTag(tag);

            model.getTags().add(tagModel);

        }
    }

    protected TagModel processTag(Tag tag) {
        TagModel tagModel = new TagModel();
        String name = tag.name();
        tagModel.setName(name);
        tagModel.setType(tag.type());
        tagModel.setTargetClass(ClassName.parseName(tag.handler()));
        tagModel.setBaseClass(ClassName.parseName(tag.baseClass()));
        tagModel.setGenerate(tag.generate());
        return tagModel;
    }

    protected void setClassNames(TypeElement componentElement, ModelElementBase modelElement,
        String generatedClass) {
        
        if (componentElement.getModifiers().contains(Modifier.ABSTRACT) || !Strings.isEmpty(generatedClass)) {
            modelElement.setGenerate(true);
            modelElement.setTargetClass(ClassName.parseName(generatedClass));
        } else {
            modelElement.setGenerate(false);
        }
        modelElement.setBaseClass(ClassName.parseName(componentElement.getQualifiedName().toString()));
    }


    protected String getDocComment(Element element) {
        return null != element ? getSourceUtils().getDocComment(element) : null;
    }

    /**
     * <p class="changed_added_4_0"></p>
     * @return the attributeProcessor
     */
    protected AttributesProcessor getAttributeProcessor() {
        return this.attributeProcessor;
    }

    public NamingConventions getNamingConventions() {
        return namingConventions;
    }

    public void setNamingConventions(NamingConventions namingConventions) {
        this.namingConventions = namingConventions;
    }

    protected void setDescription(DescriptionGroup model, Description description, String docComment) {
        descriptionProcessor.processDescription(model, description, docComment);        
    }
}
