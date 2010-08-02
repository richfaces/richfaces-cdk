<@util.import "java.util.TreeSet" />
        return new Attributes();
    }

    private enum Kind {
        BOOL,
        GENERIC,
        URI
    }

    private static final class ComponentAttribute implements Comparable<ComponentAttribute> {
    

    private final String htmlAttributeName;
    
    private String componentAttributeName;
    
    private Object defaultValue;
    

    private String[] eventNames = {};
    
    private Kind kind = Kind.GENERIC;
    
    public ComponentAttribute(String htmlAttributeName) {
        super();
        this.htmlAttributeName = htmlAttributeName;
    }
    
    public Kind getKind() {
        return this.kind;
    }


    public Object getDefaultValue() {
        return this.defaultValue;
    }


    
    public String getHtmlAttributeName() {
        return htmlAttributeName;
    }
    
    public String getComponentAttributeName() {
        return componentAttributeName;
    }

    
    public String[] getEventNames() {
        return eventNames;
    }
    
    
    public int compareTo(ComponentAttribute o) {
        return getHtmlAttributeName().compareTo(o.getHtmlAttributeName());
    }
}

    @SuppressWarnings("serial")
    public static final class Attributes extends TreeSet<ComponentAttribute> {
        
        private ComponentAttribute last;

        public void render(FacesContext context, UIComponent component) throws IOException {
            renderPassThroughAttributes(context, component, this);
        }

        public Attributes generic(String name, String componentAttribute, String... events) {
            ComponentAttribute attribute = createAttribute(name, componentAttribute);
            attribute.eventNames = events;
            attribute.kind=Kind.GENERIC;
            return this;
        }

        private ComponentAttribute createAttribute(String name, String componentAttribute) {
            ComponentAttribute attribute = new ComponentAttribute(name);
            attribute.componentAttributeName= componentAttribute;
            add(attribute);
            last = attribute;
            return attribute;
        }

        public Attributes uri(String name, String componentAttribute) {
            ComponentAttribute attribute = createAttribute(name, componentAttribute);
            attribute.kind =Kind.URI;
            return this;
        }

        public Attributes bool(String name, String componentAttribute) {
            ComponentAttribute attribute = createAttribute(name, componentAttribute);
            attribute.kind = Kind.BOOL;
            return this;
        }
        
        public Attributes defaultValue(Object value){
            last.defaultValue =value;
            return this;
        }

