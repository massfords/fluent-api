package com.googlecode.generatefluentinterface;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

/**
 * <p>
 * <p> Date: 6/2/11 Time: 10:28 AM </p>
 *
 * @author Felix.ZHU
 * @since v
 */
class GenerateFluentInterfaceWorker {
// ------------------------------ FIELDS ------------------------------

    private final Project project;
    private PsiElementFactory elementFactory;
    private PsiClass psiClass;
    private CodeStyleManager codeStyleManager;
    private String getterPrefix;
    private String setterPrefix;
    private boolean generateGetter;


// --------------------------- CONSTRUCTORS ---------------------------

    public GenerateFluentInterfaceWorker(final Project project,
                                         final PsiClass clazz, final String setterPrefix,
                                         final boolean generateGetter, String getterPrefix) {
        this.project = project;
        this.elementFactory = JavaPsiFacade.getElementFactory(project);
        this.psiClass = clazz;
        this.codeStyleManager = CodeStyleManager.getInstance(project);
        this.setterPrefix = setterPrefix;
        this.getterPrefix = getterPrefix;
        this.generateGetter = generateGetter;
    }

// -------------------------- OTHER METHODS --------------------------

    public void execute(final PsiField[] candidateFields) {
        if (generateGetter) {
            generateGetterMethods(candidateFields);
        }

        generateSetterMethods(candidateFields);
    }

    private void generateSetterMethods(final PsiField[] candidateFields) {
        for (PsiField candidateField : candidateFields) {
            createMethodFromText(buildWriteMethodText(candidateField));
        }
    }

    private void generateGetterMethods(final PsiField[] candidateFields) {
        for (PsiField candidateField : candidateFields) {
            createMethodFromText(buildReadMethodText(candidateField));
        }
    }

    private String buildReadMethodText(final PsiField candidateField) {
        return "public " + candidateField.getType().getCanonicalText() + " " + this.getterPrefix
                + upperFirstLetter(candidateField.getName()) + "() " +
                "{ " +
                "return this." + candidateField.getName() + ";" +
                "}";
    }

    private void createMethodFromText(String methodText) {
        PsiMethod psiMethod = elementFactory.createMethodFromText(methodText, null);
        generatedAnnotation(psiMethod);
        JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiMethod);
        codeStyleManager.reformat(psiMethod);
        psiClass.add(psiMethod);
    }

    private void generatedAnnotation(PsiMethod psiMethod) {
        PsiAnnotation annotation = elementFactory.createAnnotationFromText(
                "@javax.annotation.Generated(value=\"com.googlecode.generate.fluent.interface\", " +
                        "\ncomments=\"consider regenerating as opposed to modifying by hand\")", psiMethod);
        psiMethod.getModifierList().addAfter(annotation, null);
    }

    private String buildWriteMethodText(final PsiField candidateField) {
        return "public " + psiClass.getName() + " "
                + constructSetterName(candidateField) + "(" + "final " + candidateField.getType().getCanonicalText() + " " + candidateField.getName() + "){"
                + "this." + candidateField.getName() + " = " + candidateField.getName() + "; return this;" +
                "}";
    }

    private String constructSetterName(final PsiField candidateField) {
        final String fieldName = candidateField.getName();
        if (setterPrefix.equals("")) {
            return fieldName;
        } else {
            return setterPrefix + upperFirstLetter(fieldName);
        }
    }

    private String upperFirstLetter(final String name) {
        char[] firstLetter = new char[]{name.charAt(0)};
        return new String(firstLetter).toUpperCase() + name.substring(1);
    }
}
