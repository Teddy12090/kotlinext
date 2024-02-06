package idv.teddy.ktinspection.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import idv.teddy.ktinspection.quickfix.MergeSameIfQuickFix
import org.jetbrains.kotlin.idea.codeinsight.api.classic.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.idea.intentions.loopToCallChain.nextStatement
import org.jetbrains.kotlin.psi.KtIfExpression
import org.jetbrains.kotlin.psi.ifExpressionVisitor
import org.jetbrains.uast.kotlin.unwrapBlockOrParenthesis

class MergeSameIfInspection : AbstractKotlinInspection() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
    ): PsiElementVisitor {
        return ifExpressionVisitor {
                block ->
            run {
                val nextStatement = block.nextStatement()
                if (nextStatement is KtIfExpression) {
                    val nextIfBody = nextStatement.then?.unwrapBlockOrParenthesis()?.text
                    val currentIfBody = block.then?.unwrapBlockOrParenthesis()?.text
                    if (nextIfBody == currentIfBody) {
                        holder.registerProblem(block, "Merge same if", MergeSameIfQuickFix(block, nextStatement))
                    }
                }
            }
        }
    }
}
