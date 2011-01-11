package org.richfaces.cdk.apt.processors;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import org.richfaces.cdk.Logger;
import org.richfaces.cdk.NamingConventions;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.cdk.apt.SourceUtils;
import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.DescriptionGroup;
import org.richfaces.cdk.model.GeneratedFacesComponent;
import org.richfaces.cdk.model.ModelCollection;
import org.richfaces.cdk.model.ModelElementBase;
import org.richfaces.cdk.model.TagModel;
import org.richfaces.cdk.model.ViewElement;

import com.google.inject.Inject;
import com.google.inject.Provider;

public abstract class ProcessorBase {

    @Inject
    protected Logger log;

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

    protected void setTagInfo(AnnotationMirror annotation, ViewElement model) {
        SourceUtils sourceUtils = getSourceUtils();
        ModelCollection<TagModel> tags = model.getTags();
        for (AnnotationMirror tag : sourceUtils.getAnnotationValues(annotation, "tag", AnnotationMirror.class)) {
            TagModel tagModel = processTag(tag);
            tags.add(tagModel);
        }
    }

    protected TagModel processTag(AnnotationMirror tag) {
        TagModel tagModel = new TagModel();
        SourceUtils sourceUtils = getSourceUtils();
        sourceUtils.setModelProperty(tagModel, tag, "name");
        tagModel.setType(sourceUtils.getAnnotationValue(tag, "type", TagType.class));
        sourceUtils.setModelProperty(tagModel, tag, "targetClass", "handler");
        sourceUtils.setModelProperty(tagModel, tag, "baseClass");
        sourceUtils.setModelProperty(tagModel, tag, "generate");
        return tagModel;
    }

    protected void setClassNames(TypeElement componentElement, GeneratedFacesComponent modelElement, AnnotationMirror behavior) {
        SourceUtils sourceUtils = getSourceUtils();
        if (componentElement.getModifiers().contains(Modifier.ABSTRACT)
            || !sourceUtils.isDefaultValue(behavior, "generate")) {
            modelElement.setGenerate(true);
            sourceUtils.setModelProperty(modelElement, behavior, "targetClass", "generate");
        } else {
            modelElement.setGenerate(false);
            modelElement.setTargetClass(ClassName.parseName(componentElement.getQualifiedName().toString()));
        }
        modelElement.setBaseClass(ClassName.parseName(componentElement.getQualifiedName().toString()));
    }

    protected String getDocComment(Element element) {
        return null != element ? getSourceUtils().getDocComment(element) : null;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
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

    protected void setDescription(DescriptionGroup model, AnnotationMirror annotation, String docComment) {
        descriptionProcessor.processDescription(model,
            getSourceUtils().getAnnotationValue(annotation, "description", AnnotationMirror.class), docComment);
    }

    protected void processAttributes(Element element, ModelElementBase component, AnnotationMirror annotation) {
        AttributesProcessor attributesProcessor = getAttributeProcessor();
        SourceUtils sourceUtils = getSourceUtils();
        for (String atributesFragment : sourceUtils.getAnnotationValues(annotation, "attributes", String.class)) {
            attributesProcessor.processXmlFragment(component, atributesFragment);
        }
        if (element != null && ElementKind.CLASS.equals(element.getKind())) {
            attributesProcessor.processType(component, (TypeElement) element);
        }
        for (TypeMirror atributesInterface : sourceUtils
            .getAnnotationValues(annotation, "interfaces", TypeMirror.class)) {
            processInterface(component, attributesProcessor, atributesInterface);
        }
    }

    private void processInterface(ModelElementBase component, AttributesProcessor attributesProcessor, TypeMirror mirror) {
        component.getInterfaces().add(ClassName.parseName(mirror.toString()));
        if (TypeKind.DECLARED.equals(mirror.getKind())) {
            attributesProcessor.processType(component, getSourceUtils().asTypeElement(mirror));
        } else {
            // TODO - record warning.
        }
    }

    protected String getAnnotationPropertyOrConstant(TypeElement element, AnnotationMirror annotation, String annotationAttribute, String fieldName) {
        SourceUtils utils = getSourceUtils();
        if (!utils.isDefaultValue(annotation, annotationAttribute)) {
            return utils.getAnnotationValue(annotation, annotationAttribute,String.class);
        }
        Object value = utils.getConstant((TypeElement) element, fieldName);
        if (value != null) {
            return value.toString();
        }
        return null;
    }
}
