<#assign listener=event.listenerInterface.simpleName>
    public void add${listener}(${listener} listener) {

        addFacesListener(listener);

    }
    
    public ${listener}[] get${listener}s() {

        ${listener} al[] = (${listener} [])
        getFacesListeners(${listener}.class);
        return (al);

    }

    public void remove${listener}(${listener} listener) {

        removeFacesListener(listener);

    }
