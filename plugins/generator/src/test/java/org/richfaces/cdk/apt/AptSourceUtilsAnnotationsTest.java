package org.richfaces.cdk.apt;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

import org.junit.Test;
import org.richfaces.cdk.apt.TestAnnotation.TestEnum;
import org.richfaces.cdk.model.ClassName;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class AptSourceUtilsAnnotationsTest extends SourceUtilsTestBase {

    private static final String ANNOTATIONS_TEST_SUB_CLASS = "AnnotationsTestSubClass";
    private static final String ANNOTATIONS_TEST_CLASS = "AnnotationsTestClass";
    private static final String PACKAGE_PATH = "org/richfaces/cdk/apt/";
    private static final String CLASS_JAVA = PACKAGE_PATH + ANNOTATIONS_TEST_CLASS + ".java";
    private static final String SUB_CLASS_JAVA = PACKAGE_PATH + ANNOTATIONS_TEST_SUB_CLASS + ".java";

    @Test
    public void testGetAnnotationMirror() {
        execute(new SourceUtilsCallback() {
            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                Element element = findElement(roundEnv, ANNOTATIONS_TEST_CLASS);
                AnnotationMirror annotationMirror = utils.getAnnotationMirror(element, TestAnnotation.class);
                assertNotNull(annotationMirror);
                assertEquals(TestAnnotation.class.getName(), annotationMirror.getAnnotationType().toString());
            }
        });
    }

    @Test
    public void testIsAnnotationPresent() {
        execute(new SourceUtilsCallback() {
            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                assertTrue(utils.isAnnotationPresent(findElement(roundEnv, ANNOTATIONS_TEST_CLASS), TestAnnotation.class));
            }
        });
    }

    @Test
    public void testIsDefaultValue() {
        execute(new SourceUtilsCallback() {
            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                Element element = findElement(roundEnv, ANNOTATIONS_TEST_CLASS);
                AnnotationMirror annotationMirror = utils.getAnnotationMirror(element, TestAnnotation.class);
                assertTrue(utils.isDefaultValue(annotationMirror, "withDefault"));
            }
        });
    }

    @Test
    public void testNotIsDefaultValue() {
        execute(new SourceUtilsCallback() {
            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                Element element = findElement(roundEnv, ANNOTATIONS_TEST_CLASS);
                AnnotationMirror annotationMirror = utils.getAnnotationMirror(element, TestAnnotation.class);
                assertFalse(utils.isDefaultValue(annotationMirror, "value"));
            }
        });
    }

    @Test
    public void testIsAnnotationPropertyPresent() {
        execute(new SourceUtilsCallback() {
            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                Element element = findElement(roundEnv, ANNOTATIONS_TEST_CLASS);
                AnnotationMirror annotationMirror = utils.getAnnotationMirror(element, TestAnnotation.class);
                assertTrue(utils.isAnnotationPropertyPresent(annotationMirror, "value"));
            }
        });
    }

    @Test
    public void testNotIsAnnotationPropertyPresent() {
        execute(new SourceUtilsCallback() {
            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                Element element = findElement(roundEnv, ANNOTATIONS_TEST_CLASS);
                AnnotationMirror annotationMirror = utils.getAnnotationMirror(element, TestAnnotation.class);
                assertFalse(utils.isAnnotationPropertyPresent(annotationMirror, "notExistedProperty"));
            }
        });
    }

    @Test
    public void testGetStringAnnotationValue() {
        getAndCompareAnnotationValue("value", String.class, "foo");
    }

    @Test
    public void testGetBooleanAnnotationValue() {
        getAndCompareAnnotationValue("booleanProperty", Boolean.class, Boolean.TRUE);
    }
    
    @Test
    public void testGetEnumAnnotationValue() {
        getAndCompareAnnotationValue("enumProperty", TestAnnotation.TestEnum.class, TestEnum.BAR);
    }

    @Test
    public void testGetClassAnnotationValue() {
        getAndCompareAnnotationValue("typeProperty", ClassName.class, ClassName.get(PACKAGE_PATH.replace('/', '.')+ANNOTATIONS_TEST_SUB_CLASS));
    }

    @Test
    public void testGetDefaultStringAnnotationValue() {
        getAndCompareAnnotationValue("withDefault", String.class, "FOO");
    }

    public <T> void getAndCompareAnnotationValue(final String propertyName,final Class<T> type, final Object expected) {
        execute(new SourceUtilsCallback() {
            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                Element element = findElement(roundEnv, ANNOTATIONS_TEST_CLASS);
                AnnotationMirror annotationMirror = utils.getAnnotationMirror(element, TestAnnotation.class);
                T annotationValue = utils.getAnnotationValue(annotationMirror, propertyName, type);
                assertEquals("Annotation value is different from expected",expected, annotationValue);
            }
        });
    }
    
    public <T> void getAndCompareAnnotationValues(final String propertyName,final Class<T> type, final Object ... expected) {
        execute(new SourceUtilsCallback() {
            @Override
            public void process(SourceUtils utils, RoundEnvironment roundEnv) {
                Element element = findElement(roundEnv, ANNOTATIONS_TEST_CLASS);
                AnnotationMirror annotationMirror = utils.getAnnotationMirror(element, TestAnnotation.class);
                List<T> annotationValues = utils.getAnnotationValues(annotationMirror, propertyName, type);                
                assertEquals("Annotation values size is different from expected",expected.length, annotationValues.size());
                for (int i = 0; i < expected.length; i++) {
                    Object expectedValue = expected[i];
                    assertEquals("Annotation value at position "+i+" is different from expected",expectedValue, annotationValues.get(i));                    
                }
            }
        });
    }

    @Test
    public void testGetAnnotationValues() {
        getAndCompareAnnotationValues("emptyStrings", String.class);
    }

    @Test
    public void testGetClassAnnotationValues() {
        getAndCompareAnnotationValues("types", ClassName.class,ClassName.get(String.class),ClassName.get(Object.class));
    }

    @Override
    protected Iterable<String> sources() {
        return ImmutableList.of(CLASS_JAVA, SUB_CLASS_JAVA);
    }

    private Element findElement(RoundEnvironment roundEnvironment, final String name){
        Set<? extends Element> elements = roundEnvironment.getRootElements();
        return Iterables.find(elements, new Predicate<Element>() {

            @Override
            public boolean apply(Element input) {
                return name.equals(input.getSimpleName().toString());
            }
        });
    }
}
