package org.richfaces.cdk.test.renderkit;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class RendererUtils {
    
    public static boolean shouldRenderAttribute(Object value) {
        // TODO Auto-generated method stub
        return false;
    }

    public static void renderPassThroughAttributes(FacesContext facesContext, UIComponent component,
        Attributes passThroughAttributes0) throws IOException {
        // TODO Auto-generated method stub
        
    }   
    public static Attributes attributes() {
        // TODO Auto-generated method stub
        return null;
    }
    /**
     * Wrapper class around object value used to transform values into particular JS objects
     * 
     * @author Nick Belaevski
     * @since 3.3.2
     */
    public enum ScriptHashVariableWrapper {

        /**
         * No-op default wrapper
         */
        noop, 
        

        /**
         * Convert parameter to array of srings.
         */
        asArray 
        , 
        
        /**
         * Event handler functions wrapper. Wraps <pre>functionCode</pre> object into:
         * <pre>function(event) {
         *   functionCode
         * }</pre>
         */
        eventHandler;        
        /**
         * Method that does the wrapping
         * 
         * @param o object to wrap
         * @return wrapped object
         */
        Object wrap(Object o){
            return o;
        }
    }
    

    public class Attributes {
        
        public Attributes generic(String string, String string2,String ...events) {
            return this;
        }

        public Attributes bool(String string, String string2) {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
