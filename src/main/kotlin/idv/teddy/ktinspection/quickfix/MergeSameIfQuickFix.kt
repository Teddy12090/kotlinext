package idv.teddy.ktinspection.quickfix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementFactory
import org.jetbrains.kotlin.psi.KtIfExpression

class MergeSameIfQuickFix(
    val first: KtIfExpression,
    val second: KtIfExpression,
) : LocalQuickFix {
    override fun getFamilyName(): String {
        return "Merge same if"
    }

    override fun applyFix(
        p0: Project,
        p1: ProblemDescriptor,
    ) {
        val newCondition =
            buildString {
                append(wrapIfNeeded(first))
                append(" || ")
                append(wrapIfNeeded(second))
            }
        first.condition?.replace(
            PsiElementFactory.getInstance(p0).createExpressionFromText(newCondition, first.condition),
        )
        second.delete()
    }

    private fun wrapIfNeeded(expression: KtIfExpression): String {
        return expression.condition?.text?.also {
            if (it.contains("||") || it.contains("&&")) {
                return "($it)"
            }
        } ?: ""
    }
}
