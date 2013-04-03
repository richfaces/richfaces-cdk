package org.richfaces.renderkit;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.render.Renderer;

@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "testComponent.js"),
        @ResourceDependency(library = "org.richfaces", name = "testComponent.css")})
public abstract class TestComponentRendererBase extends Renderer {
    public static final String RENDERER_TYPE = "org.richfaces.TestComponentRenderer";
}
