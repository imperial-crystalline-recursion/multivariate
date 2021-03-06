package com.afewroosloose.abtesting;

import com.afewroosloose.abtesting.compiler.ABTestProcessor;
import com.google.testing.compile.JavaFileObjects;

import javax.tools.JavaFileObject;

import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by matt on 31/08/2016.
 */
public class MixedTest {

    String sourceClass = //
            "package test;" //
                    + "\n" //
                    + "\nimport com.afewroosloose.abtesting.api.annotations.ResourceTest;" //
                    + "\nimport com.afewroosloose.abtesting.api.annotations.TextTest;" //
                    + "\n" //
                    + "\npublic class Test {" //
                    + "\n  @TextTest(testName = \"Test1\", values = {\"A\", \"Z\"})" //
                    + "\n  Dummy hello;" //
                    + "\n" //
                    + "\n  @ResourceTest(method = \"setInt\", testName = \"Test1\", values = {0, 1})" //
                    + "\n  Dummy bye;" //
                    + "\n" //
                    + "\n  public static class Dummy {" //
                    + "\n    void setText(String value) {" //
                    + "\n" //
                    + "\n    }" //
                    + "\n" //
                    + "\n    void setInt(int value) {" //
                    + "\n" //
                    + "\n    }" //
                    + "\n  }" //
                    + "\n}"; //

    String generatedClass = //
            "package test;" //
                    + "\n" //
                    + "\nimport com.afewroosloose.abtesting.api.AbstractTest;" //
                    + "\nimport java.lang.Override;" //
                    + "\nimport java.lang.SuppressWarnings;" //
                    + "\nimport javax.annotation.Generated;" //
                    + "\n" //
                    + "\n@Generated(\"com.afewroosloose.abtesting.compiler.ABTestProcessor\")"
                    + "\n@SuppressWarnings(\"ResourceType, unused\")" //
                    + "\npublic class Test$$Test1 extends AbstractTest {" //
                    + "\n  private int numberOfTests;" //
                    + "\n" //
                    + "\n  Test.Dummy hello;" //
                    + "\n" //
                    + "\n  Test.Dummy bye;" //
                    + "\n" //
                    + "\n  public Test$$Test1() {" //
                    + "\n    numberOfTests = 2;" //
                    + "\n  }" //
                    + "\n" //
                    + "\n  @Override" //
                    + "\n  public int getNumberOfTests() {" //
                    + "\n    return numberOfTests;" //
                    + "\n  }" //
                    + "\n" + "\n  @Override" //
                    + "\n  public void run(int testToChoose) {" //
                    + "\n    if (testToChoose == 0) {" //
                    + "\n      hello.setText(\"A\");"//
                    + "\n      bye.setInt(0);" //
                    + "\n      return;" //
                    + "\n    }" //
                    + "\n    if (testToChoose == 1) {" //
                    + "\n      hello.setText(\"Z\");" //
                    + "\n      bye.setInt(1);" //
                    + "\n      return;" //
                    + "\n    }" //
                    + "\n  }" //
                    + "\n}"; //

    @Test
    public void testResourceTest() throws Exception {

        JavaFileObject sourceObj =
                JavaFileObjects.forSourceString("test.Test", sourceClass);

        JavaFileObject genObj =
                JavaFileObjects.forSourceString("test/Test$$Test1", generatedClass);

        assertAbout(javaSource()).that(sourceObj)
                .processedWith(new ABTestProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(genObj);
    }
}

