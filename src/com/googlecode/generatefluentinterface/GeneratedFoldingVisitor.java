package com.googlecode.generatefluentinterface;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaRecursiveElementWalkingVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ma21633
 *         Date: 9/1/12
 */
public abstract class GeneratedFoldingVisitor extends JavaRecursiveElementWalkingVisitor {
    public static final Object lock = new Object();
    private PsiElement myLastElement;
    protected final List<FoldingDescriptor> myFoldingData = new ArrayList<FoldingDescriptor>();


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
