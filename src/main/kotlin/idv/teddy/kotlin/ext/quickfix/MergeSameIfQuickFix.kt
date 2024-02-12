package idv.teddy.kotlin.ext.quickfix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.SmartPsiElementPointer
import org.jetbrains.kotlin.psi.KtIfExpression

class MergeSameIfQuickFix(
    val first: SmartPsiElementPointer<KtIfExpression>,
    val second: SmartPsiElementPointer<KtIfExpression>,
) : LocalQuickFix {
    override fun getFamilyName(): String {
        return "Merge same if"
    }


    override fun applyFix(
        p0: Project,
        p1: ProblemDescriptor,
    ) {
        val firstElement = checkNotNull(first.element)
        val secondElement = checkNotNull(second.element)
        val newCondition =
            buildString {
                append(wrapIfNeeded(firstElement))
                append(" || ")
                append(wrapIfNeeded(secondElement))
            }
        firstElement.condition?.replace(
            PsiElementFactory.getInstance(p0).createExpressionFromText(newCondition, firstElement.condition),
        )
        secondElement.delete()
    }

    private fun wrapIfNeeded(expression: KtIfExpression): String {
        return expression.condition?.text?.also {
            if (it.contains("||") || it.contains("&&")) {
                return "($it)"
            }
        } ?: ""
    }
}
