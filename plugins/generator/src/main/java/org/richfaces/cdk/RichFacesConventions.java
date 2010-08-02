package org.richfaces.cdk;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.richfaces.cdk.model.ClassName;
import org.richfaces.cdk.model.ComponentLibrary;
import org.richfaces.cdk.model.FacesId;
import org.richfaces.cdk.model.InvalidNameException;
import org.richfaces.cdk.model.Name;
import org.richfaces.cdk.model.Name.Classifier;
import org.richfaces.cdk.util.Strings;

/**
 * <p class="changed_added_4_0">
 * </p>
 * 
 * @author asmirnov@exadel.com
 * 
 */
public final class RichFacesConventions implements NamingConventions {
    private static final String ABSTRACT = "Abstract";
    private static final String BASE = "Base";
    private static final String UI = "UI";
    private static final String[] COMPONENT_SUFFIXES = { BASE };
    private static final String[] COMPONENT_PREFIXES = { UI, ABSTRACT };

    // TODO - inject base name.
    private String baseName ;//= "org.richfaces";

    public RichFacesConventions() {
    }

    @Override
    public FacesId inferComponentType(ClassName componentClass) throws InvalidNameException {

        // check parameters.
        if (null == componentClass) {
            throw new IllegalArgumentException();
        }

        Name name = Name.create(componentClass.toString());

        // Use base library prefix.
        String baseName = this.getBaseName();

        if (null != baseName) {
            name.setPrefix(baseName);
        }

        // JsfComponent type does not contain class or markup parts.
        name.setClassifier(null);
        name.setMarkup(null);

        String simpleName = name.getSimpleName();

        // Remove common prefixes.
        for (int i = 0; i < COMPONENT_PREFIXES.length; i++) {
            if (simpleName.startsWith(COMPONENT_PREFIXES[i])) {
                simpleName = simpleName.substring(COMPONENT_PREFIXES[i].length());

                break;
            }
        }

        // Remove common suffixes.
        for (int i = 0; i < COMPONENT_SUFFIXES.length; i++) {
            if (simpleName.endsWith(COMPONENT_SUFFIXES[i])) {
                simpleName = simpleName.substring(0, simpleName.length() - COMPONENT_SUFFIXES[i].length());

                break;
            }
        }

        name.setSimpleName(simpleName);

        return new FacesId(name.toString());
    }

    @Override
    public ClassName inferUIComponentClass(FacesId componentType) throws InvalidNameException {
        if (null == componentType) {
            throw new IllegalArgumentException();
        }

        // Infer UI class name from component type.
        Name name = Name.create(componentType.toString());

        name.setClassifier(Classifier.component);
        name.setMarkup(null);
        name.setSimpleName(UI + name.getSimpleName());

        return new ClassName(name.toString());
    }

    @Override
    public FacesId inferUIComponentFamily(FacesId componentType) {
        if (null == componentType) {
            throw new IllegalArgumentException();
        }
        return componentType;
    }

    @Override
    public ClassName inferTagHandlerClass(FacesId componentType, String markup) {
        if (null == componentType) {
            throw new IllegalArgumentException();
        }
        Name name = Name.create(componentType.toString());
        name.setClassifier(Classifier.taglib);
        name.setMarkup(markup);
        name.setSimpleName(name.getSimpleName() + "Handler");
        return new ClassName(name.toString());
    }

    @Override
    public String inferTagName(FacesId componentType) {
        if (null == componentType) {
            throw new IllegalArgumentException();
        }
        Name name = Name.create(componentType.toString());
        return Strings.firstToLowerCase(name.getSimpleName());
    }

    /**
     * <p class="changed_added_4_0">
     * </p>
     * 
     * @return the baseName
     */
    protected String getBaseName() {
        return baseName;
    }

    public String inferRendererTypeByRendererClass(ClassName rendererClass) {
        Pattern pattern = Pattern.compile("^(.*).renderkit.(.*\\.)?(.*)RendererBase$");
        Matcher matcher = pattern.matcher(rendererClass.getName());
        if (matcher.matches()) {
            return matcher.group(1) + "." + matcher.group(3) + "Renderer";
        }
        return "";
    }

    @Override
    public String inferRendererTypeByComponentType(FacesId componentType) {
        return componentType + "Renderer";
    }

    public String inferComponentFamily(FacesId type) {
        return null;
    }

    public String inferTemplate(FacesId type) {
        return null;
    }

    public String inferRendererTypeByTemplatePath(String templateName) {
        return null;
    }

    public String inferRendererName(FacesId type) {
        return null;
    }

    public String inferRendererBaseName(FacesId type) {
        return null;
    }

    public String inferComponentTypeByRendererClass(String s) {
        Pattern pattern = Pattern.compile("^(.*).renderkit.(.*\\.)?(.*)RendererBase$");
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches()) {
            return matcher.group(1) + "." + matcher.group(3);
        }
        return "";
    }

    public String inferComponentFamilyByRendererClass(String s) {
        return inferComponentTypeByRendererClass(s);
    }

    @Override
    public ClassName inferBehaviorClass(FacesId id) {
        if (null == id) {
            throw new IllegalArgumentException();
        }

        // Infer UI class name from component type.
        Name name = Name.create(id.toString());

        name.setClassifier(Classifier.component);
        // All Behavior classes belongs to "component.behavior" package. 
        name.setMarkup("behavior");

        return new ClassName(name.toString());
    }

    @Override
    public FacesId inferBehaviorType(ClassName targetClass) {
        // check parameters.
        if (null == targetClass) {
            throw new IllegalArgumentException();
        }

        Name name = Name.create(targetClass.toString());

        // Use base library prefix.
        String baseName = this.getBaseName();

        if (null != baseName) {
            name.setPrefix(baseName);
        }

        // Jsf Behavior type does not contain class or markup parts.
        name.setClassifier(null);
        name.setMarkup(null);

        String simpleName = name.getSimpleName();

        // Remove common prefixes.
        for (int i = 0; i < COMPONENT_PREFIXES.length; i++) {
            if (simpleName.startsWith(COMPONENT_PREFIXES[i])) {
                simpleName = simpleName.substring(COMPONENT_PREFIXES[i].length());

                break;
            }
        }

        // Remove common suffixes.
        for (int i = 0; i < COMPONENT_SUFFIXES.length; i++) {
            if (simpleName.endsWith(COMPONENT_SUFFIXES[i])) {
                simpleName = simpleName.substring(0, simpleName.length() - COMPONENT_SUFFIXES[i].length());

                break;
            }
        }

        name.setSimpleName(simpleName);

        return new FacesId(name.toString());
    }

    @Override
    public String inferTaglibName(String uri) throws InvalidNameException {
        try {
            URI taglibUri = URI.create(uri);
            String path = taglibUri.getPath();
            if(null != path){
                int lastIndexOfPathSeparator = path.lastIndexOf('/');
                if(lastIndexOfPathSeparator>=0){
                    path = path.substring(lastIndexOfPathSeparator+1);
                }
                int indexOfDot = path.indexOf('.');
                if(indexOfDot>0){
                    path = path.substring(0, indexOfDot);
                }
                return path;
            } else {
                throw new InvalidNameException("Invalid taglib uri, no path defined " + uri );
            }
        } catch (IllegalArgumentException e) {
            throw new InvalidNameException("Invalid taglib uri " + uri + " , " + e.getMessage());
        }
    }

    @Override
    public String inferTaglibUri(ComponentLibrary library) {
        // TODO infer name from library base names.
        return "http://richfaces.org/a4j";
    }
}
