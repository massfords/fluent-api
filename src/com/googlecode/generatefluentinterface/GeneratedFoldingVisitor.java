package com.googlecode.generatefluentinterface;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ma21633
 *         Date: 9/1/12
 */
public class GeneratedFoldingVisitor extends JavaRecursiveElementWalkingVisitor {
    private PsiElement myLastElement;
    protected final List<FoldingDescriptor> myFoldingData = new ArrayList<FoldingDescriptor>();

    @Override
    public void visitMethod(PsiMethod method) {
        PsiModifierList modifierList = method.getModifierList();
        if (modifierList != null) {
            PsiAnnotation annotation = modifierList.findAnnotation(Generated.class.getName());
            if (annotation != null) {
                addFoldingData(method.getBody());
            }
        }
        super.visitMethod(method);
    }
    @Override
    public void visitAnnotation(PsiAnnotation annotation) {
        String name = annotation.getQualifiedName();
        if (name != null && name.equals(Generated.class.getName())) {
            addFoldingData(annotation.getParameterList());
        }
        super.visitAnnotation(annotation);
    }

    protected void addFoldingData(final PsiElement element) {
        PsiElement prevSibling = PsiTreeUtil.skipSiblingsBackward(element, PsiWhiteSpace.class);
        synchronized (myFoldingData) {
            if (myLastElement == null || prevSibling != myLastElement) {
                myFoldingData.add(new FoldingDescriptor(element, element.getTextRange()));
            } else {
                FoldingDescriptor lastDescriptor = myFoldingData.get(myFoldingData.size() - 1);
                final TextRange range = new TextRange(lastDescriptor.getRange().getStartOffset(), element.getTextRange().getEndOffset());
                myFoldingData.set(myFoldingData.size() - 1, new FoldingDescriptor(lastDescriptor.getElement(), range));
            }
        }
        myLastElement = element;
    }
}
