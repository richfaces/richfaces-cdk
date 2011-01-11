    private StateHelper stateHelper = null;
        
    protected StateHelper getStateHelper() {
        if (stateHelper == null) {
            stateHelper = new PartialStateHolderHelper(this);
        }
        return stateHelper;
    }
    
    // ----------------------------------------------------- StateHolder Methods
    @Override
    public Object saveState(FacesContext context) {
        return stateHelper.saveState(context);
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        stateHelper.restoreState(context, state);
    }

    private boolean transientValue = false;

    @Override
    public boolean isTransient() {
        return this.transientValue;
    }

    @Override
    public void setTransient(boolean transientValue) {
        this.transientValue = transientValue;
    }

    private boolean initialState;

    @Override
    public void markInitialState() {
        initialState = true;
    }

    @Override
    public boolean initialStateMarked() {
        return initialState;
    }

    @Override
    public void clearInitialState() {
        initialState = false;
    }

