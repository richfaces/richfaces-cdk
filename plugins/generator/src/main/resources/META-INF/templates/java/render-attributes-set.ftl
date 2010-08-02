        for (ComponentAttribute knownAttribute : attributes) {
                renderAttributeAndBehaviors(context, component, knownAttribute);
            }
        }
    
    public static void renderAttributeAndBehaviors(FacesContext facesContext, UIComponent component,
        ComponentAttribute componentAttribute) throws IOException {
    