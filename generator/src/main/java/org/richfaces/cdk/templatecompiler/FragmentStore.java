package org.richfaces.cdk.templatecompiler;

import java.util.Map;

import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;
import org.richfaces.cdk.templatecompiler.model.CdkFragmentElement;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class FragmentStore {

    Map<String, Fragment> store = Maps.newHashMap();

    @Inject
    TypesFactory typesFactory;

    public Fragment getFragment(String fragmentName) {
        return store.get(fragmentName);
    }

    public Fragment addFragment(CdkFragmentElement fragmentElement) {
        String fragmentName = fragmentElement.getName();
        
        if (store.containsKey(fragmentName)) {
            throw new IllegalStateException("Fragment " + fragmentName + " is already defined.");
        }
        
        Fragment fragment = new Fragment(fragmentElement, typesFactory);
        store.put(fragmentName, fragment);
        
        return fragment;
    }
}
