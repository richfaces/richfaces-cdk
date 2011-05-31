package org.richfaces.cdk.templatecompiler.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.richfaces.cdk.util.Strings;

public class NormalizedStringAdapter extends XmlAdapter<String, String> {
    @Override
    public String marshal(String text) {
        return Strings.isEmpty(text) ? null : text.trim();
    }

    @Override
    public String unmarshal(String v) throws Exception {
        return Strings.isEmpty(v) ? null : v.trim();
    }
}