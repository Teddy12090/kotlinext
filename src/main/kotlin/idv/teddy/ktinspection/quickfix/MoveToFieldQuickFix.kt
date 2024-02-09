package idv.teddy.ktinspection.quickfix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtProperty


class MoveToFieldQuickFix(private val block: KtProperty) : LocalQuickFix {
    override fun getFamilyName(): String {
        return "Move to field"
    }

    override fun applyFix(
        p0: Project,
        p1: ProblemDescriptor,
    ) {
        val targetPlace = getTargetPlace(block)
        block.delete()

        targetPlace.parent.addBefore(
            PsiElementFactory.getInstance(p0).createStatementFromText(block.text, null),
            targetPlace,
        )
    }

    private fun getTargetPlace(block: PsiElement): PsiElement {
        return if (block is KtFunction) block else getTargetPlace(block.parent)
    }
}
