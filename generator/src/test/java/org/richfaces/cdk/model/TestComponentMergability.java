package org.richfaces.cdk.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestComponentMergability {

    @Test
    public void testComponentTagMerging() {
        // given
        ComponentModel component1 = new ComponentModel();
        ComponentModel component2 = new ComponentModel();

        TagModel tag1 = new TagModel();
        tag1.setName("tag1");
        component1.getTags().add(tag1);

        TagModel tag2 = new TagModel();
        tag2.setName("tag2");
        component1.getTags().add(tag2);

        // when
        component1.merge(component2);

        // then
        assertTrue(component1.getTags().contains(tag1));
        assertTrue(component1.getTags().contains(tag2));
    }

}
