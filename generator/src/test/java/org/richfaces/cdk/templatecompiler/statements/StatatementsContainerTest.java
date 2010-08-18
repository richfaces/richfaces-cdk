package org.richfaces.cdk.templatecompiler.statements;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.richfaces.cdk.CdkTestBase;
import org.richfaces.cdk.CdkTestRunner;
import org.richfaces.cdk.Mock;
import org.richfaces.cdk.MockController;
import org.richfaces.cdk.templatecompiler.builder.model.JavaImport;
import org.richfaces.cdk.templatecompiler.el.types.TypesFactory;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.internal.ImmutableSet;
import com.google.inject.name.Named;

@RunWith(CdkTestRunner.class)
public class StatatementsContainerTest extends CdkTestBase {

    @Mock
    private TemplateStatement statement;

    @Mock
    @Named("second")
    private TemplateStatement statement1;

    @Mock
    private JavaImport import1;
    @Mock
    @Named("second")
    private JavaImport import2;
    @Mock
    @Named("third")
    private JavaImport import3;

    @Inject
    private MockController controller;

    @Inject
    private StatementsContainer container;

    @Test
    public void addStatement() throws Exception {
        statement.setParent(container);
        expectLastCall();
        controller.replay();
        container.addStatement(statement);
        controller.verify();
        assertSame(statement, container.getStatements().get(0));
    }

    @Test
    public void addStatement1() throws Exception {
        statement.setParent(container);
        expectLastCall();
        controller.replay();
        container.getStatements().add(statement);
        controller.verify();
        assertSame(statement, container.getStatements().get(0));
    }

    @Test
    public void addStatement2() throws Exception {
        statement.setParent(container);
        expectLastCall();
        statement1.setParent(container);
        expectLastCall();
        controller.replay();
        container.addStatement(statement);
        container.addStatement(statement1);
        controller.verify();
        assertEquals(2, container.getStatements().size());
        assertSame(statement, container.getStatements().get(0));
    }

    @Test
    public void getImports() throws Exception {
        statement.setParent(container);
        expectLastCall();
        expect(statement.getRequiredImports()).andReturn(Collections.singleton(import1));
        statement1.setParent(container);
        expectLastCall();
        expect(statement1.getRequiredImports()).andReturn(ImmutableSet.of(import2, import3));
        controller.replay();
        container.addStatement(statement);
        container.addStatement(statement1);
        JavaImport[] javaImports = Iterables.toArray(container.getRequiredImports(), JavaImport.class);
        controller.verify();
        assertEquals(3, javaImports.length);
        assertSame(import1, javaImports[0]);
    }

    @Test
    public void getCode() throws Exception {
        statement.setParent(container);
        expectLastCall();
        expect(statement.getCode()).andReturn("first ");
        statement1.setParent(container);
        expectLastCall();
        expect(statement1.getCode()).andReturn("second");
        controller.replay();
        container.addStatement(statement);
        container.addStatement(statement1);
        String code = container.getCode();
        controller.verify();
        assertTrue(code.contains("first"));
        assertTrue(code.contains("second"));
    }

    @Test
    public void testVariables() throws Exception {
        StatementsContainer parent = new StatementsContainer();
        parent.addStatement(container);
        parent.setVariable("foo", TypesFactory.OBJECT_TYPE);
        assertTrue(container.isDefined("foo"));
    }

    @Test
    public void testVariables1() throws Exception {
        StatementsContainer parent = new StatementsContainer();
        parent.addStatement(container);
        container.setVariable("foo", TypesFactory.OBJECT_TYPE);
        assertFalse(parent.isDefined("foo"));
    }

    @Test
    public void testVariables2() throws Exception {
        StatementsContainer parent = new StatementsContainer();
        parent.addStatement(container);
        parent.setVariable("foo", TypesFactory.OBJECT_TYPE);
        container.setVariable("foo", TypesFactory.INTEGER_TYPE);
        assertTrue(container.isDefined("foo"));
        assertEquals(TypesFactory.INTEGER_TYPE, container.getVariable("foo"));
    }
}
