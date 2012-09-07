package org.richfaces.cdk.model;

/**
 * That class represents JSF component in the CDK. That is mapped to faces-config "component" element.
 *
 * @author asmirnov@exadel.com
 *
 */
public final class ComponentModel extends ModelElementBase implements ModelElement<ComponentModel>, Cacheable {
    private static final long serialVersionUID = 2297349356280370771L;
    /**
     * <p class="changed_added_4_0">
     * Facets recognised by the component
     * </p>
     */
    private final ModelCollection<FacetModel> facets = ModelSet.<FacetModel>create();
    /**
     * <p class="changed_added_4_0">
     * Application level events fired by the component
     * </p>
     */
    private final ModelCollection<EventModel> events = ModelSet.<EventModel>create();
    /**
     * <p class="changed_added_4_0">
     * JsfRenderer for the final component. This is bidirectional many to many relation.
     * </p>
     */
    private FacesId family;
    private FacesId rendererType;
    private String rendererTemplate;
    private ComponentModel parent;

    private boolean changed = true;
    private boolean changeTracking = true;

    public ComponentModel(FacesId key) {
        this.setId(key);
    }

    public ComponentModel() {

    }

    public <R, D> R accept(Visitor<R, D> visitor, D data) {
        return visitor.visitComponent(this, data);

        // TODO ??? see at render kit
        // for (RendererModel rendererType : renderers) {
        // rendererType.accept(visitor);
        // }
    }

    @Override
    public void merge(ComponentModel otherComponent) {

        if (this.changeTracking) {
            this.changed = true;
        }

        // merge facets, renderers, events ...
        ComponentLibrary.merge(getAttributes(), otherComponent.getAttributes());
        ComponentLibrary.merge(getFacets(), otherComponent.getFacets());
        ComponentLibrary.merge(getEvents(), otherComponent.getEvents());
        ComponentLibrary.merge(this, otherComponent);
    }

    @Override
    public boolean same(ComponentModel other) {
        if (null != getId() && null != other.getId()) {
            // Both types not null, compare them.
            return getId().equals(other.getId());
        }

        // one or both types are null, compare classes.
        return null != getTargetClass() && getTargetClass().equals(other.getTargetClass());
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the rendererType
     */
    @Merge
    public FacesId getRendererType() {
        return this.rendererType;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param renderer the rendererType to set
     */
    public void setRendererType(FacesId renderer) {
        this.rendererType = renderer;
    }

    /**
     * @return the rendererTemplate
     */
    @Merge
    public String getRendererTemplate() {
        return rendererTemplate;
    }

    /**
     * @param rendererTemplate the rendererTemplate to set
     */
    public void setRendererTemplate(String rendererTemplate) {
        this.rendererTemplate = rendererTemplate;
    }

    /**
     * <p class="changed_added_4_0">
     * Reepresent a component family. In the faces-config element that property encoded as
     * <component><component-extension><cdk:component-family>....
     * </p>
     *
     * @return the family
     */
    @Merge
    public FacesId getFamily() {
        return family;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @param family the family to set
     */
    public void setFamily(FacesId family) {
        this.family = family;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the facets
     */
    public ModelCollection<FacetModel> getFacets() {
        return facets;
    }

    public FacetModel getFacet(final String name) {
        return facets.find(new Named.NamePredicate(name));
    }

    public FacetModel getOrCreateFacet(String name) {
        FacetModel facet = getFacet(name);
        if (null == facet) {
            facet = new FacetModel();
            facet.setName(name);
            facets.add(facet);
        }
        return facet;
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the events
     */
    public ModelCollection<EventModel> getEvents() {
        return events;
    }

    public EventModel addEvent(ClassName className) {

        EventModel event = new EventModel();
        event.setType(className);
        events.add(event);
        return event;
    }

    public EventModel addEvent(String className) {
        return addEvent(ClassName.get(className));
    }

    @Override
    public String toString() {
        return "Component {type: " + getId() + ", family: " + getFamily() + "}";
    }

    public void setParent(ComponentModel parentComponent) {
        this.parent = parentComponent;
    }

    @Merge
    public ComponentModel getParent() {
        return this.parent;
    }

    @Override
    public void markUnchanged() {
        this.changed = false;
    }

    @Override
    public boolean hasChanged() {
        return changed;
    }

    @Override
    public void stopTrackingChanges() {
        this.changeTracking = false;
    }
}
