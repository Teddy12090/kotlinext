package idv.teddy.kotlin.ext.quickfix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.SmartPsiElementPointer
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtProperty

class MoveToFieldQuickFix(val block: SmartPsiElementPointer<KtProperty>) : LocalQuickFix {
    override fun getFamilyName(): String {
        return "Move to field"
    }

    override fun applyFix(
        p0: Project,
        p1: ProblemDescriptor,
    ) {
        val element = checkNotNull(block.element)
        val targetPlace = getTargetPlace(element)
        element.delete()

        targetPlace.parent.addBefore(
            PsiElementFactory.getInstance(p0).createStatementFromText("private ${element.text}", null),
            targetPlace,
        )
    }

    private fun getTargetPlace(block: PsiElement): PsiElement {
        return if (block is KtFunction) block else getTargetPlace(block.parent)
    }
}
