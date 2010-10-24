<@util.require "RENDER_ATTRIBUTES_SET"/>

<@util.attributesField "${fieldName}" attributes />

${RENDER_ATTRIBUTES_SET}(${facesContextVariable}, ${componentVariable}, 
	${fieldName});