    <#list facets as facet>
      <#if facet.generate >
    public UIComponent ${facet.getterName}() {
        return getFacet("${facet.name}");
    }
    public void ${facet.setterName}(UIComponent facet) {
        getFacets().put("${facet.name}", facet);
    }
    </#if>
    </#list>
