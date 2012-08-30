package org.richfaces.cdk.model;

import java.io.Serializable;
import java.util.Collection;

import com.google.common.collect.Lists;

/**
 * <p class="changed_added_4_0">
 * Base class for the most JSF components taht have description attributes and support extensions in faces-config.
 * </p>
 *
 * @author asmirnov@exadel.com
 *
 */
public class ModelElementBase extends BeanModelBase implements GeneratedFacesComponent, ViewElement, Serializable {
    private FacesId id;
    private ClassName targetClass;
    private ClassName baseClass;
    private final ModelCollection<TagModel> tags = ModelSet.<TagModel>create();
    private final Collection<ClassName> interfaces = Lists.newArrayList();
    /**
     * <p class="changed_added_4_0">
     * Is that bean property generate in the class or should be generated ?
     * </p>
     */
    private Boolean generate;

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.model.ViewElement#getTags()
     */
    @Override
    public ModelCollection<TagModel> getTags() {
        return this.tags;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the interfaces
     */
    public Collection<ClassName> getInterfaces() {
        return this.interfaces;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.model.GeneratedFacesComponent#getId()
     */
    public FacesId getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.model.GeneratedFacesComponent#setId(org.richfaces.cdk.model.FacesId)
     */
    public void setId(FacesId id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.model.GeneratedFacesComponent#setTargetClass(org.richfaces.cdk.model.ClassName)
     */
    public void setTargetClass(ClassName targetClass) {
        this.targetClass = targetClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.model.GeneratedFacesComponent#getTargetClass()
     */
    public ClassName getTargetClass() {
        return targetClass;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param baseClass the baseClass to set
     */
    @Override
    public void setBaseClass(ClassName baseClass) {
        this.baseClass = baseClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.cdk.model.GeneratedFacesComponent#getBaseClass()
     */
    public ClassName getBaseClass() {
        return baseClass;
    }

    @Merge
    public Boolean getGenerate() {
        return generate;
    }

    public void setGenerate(Boolean generate) {
        this.generate = generate;
    }
}
