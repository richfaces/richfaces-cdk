package org.richfaces.cdk.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestAttributeMergability {

    private AttributeModel visibleAttribute;
    private AttributeModel hiddenAttribute;

    @Before
    public void before() {
        visibleAttribute = new AttributeModel();
        hiddenAttribute = new AttributeModel();
        hiddenAttribute.setHidden(true);
    }

    @Test
    public void when_both_are_visible_then_result_is_visible() {
        // given
        AttributeModel attribute = new AttributeModel();
        assertFalse(attribute.isHidden());

        // when
        attribute.merge(visibleAttribute);

        // then
        assertFalse(attribute.isHidden());
    }

    @Test
    public void when_both_are_hidden_then_result_is_hidden() {
        // given
        AttributeModel attribute = new AttributeModel();
        attribute.setHidden(true);
        assertTrue(attribute.isHidden());

        // when
        attribute.merge(hiddenAttribute);

        // then
        assertTrue(attribute.isHidden());
    }

    @Test
    public void when_source_is_hidden_and_target_visible_then_result_is_hidden() {
        // given
        AttributeModel attribute = new AttributeModel();
        attribute.setHidden(true);
        assertTrue(attribute.isHidden());

        // when
        attribute.merge(visibleAttribute);

        // then
        assertTrue(attribute.isHidden());
    }

    @Test
    public void when_target_is_hidden_and_source_visible_then_result_is_hidden() {
        // given
        AttributeModel attribute = new AttributeModel();
        assertFalse(attribute.isHidden());

        // when
        attribute.merge(hiddenAttribute);

        // then
        assertTrue(attribute.isHidden());
    }
}
