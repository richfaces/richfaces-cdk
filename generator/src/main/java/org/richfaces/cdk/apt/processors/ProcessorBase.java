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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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
        sourceUtils.setModelProperty(tagModel, tag, "targetClass", "handlerClass");
        if (tagModel.getTargetClass() == null || Object.class.equals(tagModel.getTargetClass().getName())) {
            sourceUtils.setModelProperty(tagModel, tag, "targetClass", "handler");
        }
        sourceUtils.setModelProperty(tagModel, tag, "baseClass");
        sourceUtils.setModelProperty(tagModel, tag, "generate");
        return tagModel;
    }

    protected void setClassNames(TypeElement componentElement, GeneratedFacesComponent modelElement, AnnotationMirror behavior) {
        SourceUtils sourceUtils = getSourceUtils();
        if (componentElement.getModifiers().contains(Modifier.ABSTRACT) || !sourceUtils.isDefaultValue(behavior, "generate")) {
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

        final Iterable<String> attributesFragments = getAttributesFragment(annotation);
        final Iterable<? extends TypeMirror> interfaces = getAllInterfaces(element, annotation);

        for (String atributesFragment : attributesFragments) {
            attributesProcessor.processXmlFragment(component, atributesFragment);
        }
        for (TypeMirror atributesInterface : interfaces) {
            processInterface(component, attributesProcessor, atributesInterface);
        }

        if (element != null && ElementKind.CLASS.equals(element.getKind())) {
            attributesProcessor.processType(component, (TypeElement) element);
        }
    }

    /**
     * Get fragments from 'attributes' attribute of annotation
     */
    private Iterable<String> getAttributesFragment(AnnotationMirror annotation) {
        final SourceUtils sourceUtils = getSourceUtils();
        return sourceUtils.getAnnotationValues(annotation, "attributes", String.class);
    }

    /**
     * Get interfaces from 'interfaces' attribute of annotation
     */
    private Iterable<? extends TypeMirror> getAttributeInterfaces(AnnotationMirror annotation) {
        final SourceUtils sourceUtils = getSourceUtils();
        return sourceUtils.getAnnotationValues(annotation, "interfaces", TypeMirror.class);
    }

    /**
     * Get interfaces list from the given class element
     */
    private Iterable<? extends TypeMirror> getImplementedInterfaces(Element element) {
        final SourceUtils sourceUtils = getSourceUtils();

        if (element == null) {
            return Lists.newLinkedList();
        }

        return sourceUtils.asTypeElement(element.asType()).getInterfaces();
    }

    private Iterable<? extends TypeMirror> getAllInterfaces(Element element, AnnotationMirror annotation) {
        return Iterables.concat(getImplementedInterfaces(element), getAttributeInterfaces(annotation));
    }

    private void processInterface(ModelElementBase component, AttributesProcessor attributesProcessor, TypeMirror mirror) {
        component.getInterfaces().add(ClassName.parseName(mirror.toString()));
        if (TypeKind.DECLARED.equals(mirror.getKind())) {
            attributesProcessor.processType(component, getSourceUtils().asTypeElement(mirror));
        } else {
            // TODO - record warning.
        }
    }

    protected String getAnnotationPropertyOrConstant(TypeElement element, AnnotationMirror annotation,
            String annotationAttribute, String fieldName) {
        SourceUtils utils = getSourceUtils();
        if (!utils.isDefaultValue(annotation, annotationAttribute)) {
            return utils.getAnnotationValue(annotation, annotationAttribute, String.class);
        }
        if (null != element) {
            Object value = utils.getConstant((TypeElement) element, fieldName);
            if (value != null) {
                return value.toString();
            }
        }
        return null;
    }
}
