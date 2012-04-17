package org.richfaces.cdk.generate.taglib;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.richfaces.cdk.model.EventModel;

public class TestListenerAttributes {

    @Test
    public void testAttribute() {

        // given
        EventModel model = new EventModel();
        ListenerAttribute attribute = ListenerAttribute.FOR;

        // when
        String description = attribute.derivateProperty(model).getDescription();

        // then
        assertTrue(description instanceof String);
    }
}
