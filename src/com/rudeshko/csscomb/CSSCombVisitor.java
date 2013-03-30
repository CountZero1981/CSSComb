package com.rudeshko.csscomb;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.css.CssBlock;
import com.intellij.psi.css.CssDeclaration;
import com.intellij.psi.css.CssRuleset;
import com.intellij.psi.css.CssStylesheet;

import java.util.Arrays;
import java.util.Comparator;

public class CSSCombVisitor extends PsiElementVisitor {

    private Comparator<? super CssDeclaration> comparator;

    @Override
    public void visitElement(PsiElement element) {
        if (element instanceof CssStylesheet) {
            final CssStylesheet cssStylesheet = (CssStylesheet) element;
            Project project = cssStylesheet.getProject();
            comparator = CssOrder.makeComparator(project);

            new WriteCommandAction.Simple(project, cssStylesheet.getContainingFile()) {
                @Override
                protected void run() throws Throwable {
                    processStylesheet(cssStylesheet);
                }
            }.execute();
        }
    }

    private void processStylesheet(CssStylesheet cssStylesheet) {
        for (CssRuleset ruleSet : cssStylesheet.getRulesets()) {
            processRuleSet(ruleSet);
        }
    }

    private void processRuleSet(CssRuleset ruleSet) {
        processBlock(ruleSet.getBlock());
    }

    private void processBlock(CssBlock block) {
        if (block != null) {
            sortBlock(block);
        }
    }

    private void sortBlock(CssBlock block) {
        CssDeclaration[] sortedDeclarations = block.getDeclarations();
        Arrays.sort(sortedDeclarations, comparator);

        CssDeclaration[] declarations = block.getDeclarations();
        for (int i = 0; i < declarations.length; i++) {
            block.addAfter(sortedDeclarations[i], declarations[i]);
        }

        for (CssDeclaration declaration : declarations) {
            declaration.delete();
        }
    }

}
