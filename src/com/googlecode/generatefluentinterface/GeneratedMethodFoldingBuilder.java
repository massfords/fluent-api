/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.generatefluentinterface;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * @author yole
 */
public class GeneratedMethodFoldingBuilder extends FoldingBuilderEx {
    @NotNull
    public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        GeneratedFoldingVisitor visitor = new GeneratedFoldingVisitor();
        root.accept(visitor);
        return visitor.myFoldingData.toArray(new FoldingDescriptor[visitor.myFoldingData.size()]);
    }

    public String getPlaceholderText(@NotNull ASTNode node) {
        return "{...}";
    }

    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return true;
    }
}
