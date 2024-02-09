package idv.teddy.ktinspection.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import idv.teddy.ktinspection.quickfix.ReplaceWithForQuickFix
import org.jetbrains.kotlin.idea.codeinsight.api.classic.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.idea.intentions.loopToCallChain.nextStatement
import org.jetbrains.kotlin.idea.intentions.loopToCallChain.previousStatement
import org.jetbrains.kotlin.psi.expressionVisitor
import org.jetbrains.kotlin.psi.psiUtil.createSmartPointer
import org.jetbrains.uast.kotlin.unwrapBlockOrParenthesis

class ReplaceWithLoopInspection : AbstractKotlinInspection() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
    ): PsiElementVisitor {
        return expressionVisitor { block ->
            run {
                val previousStatement = block.previousStatement()?.unwrapBlockOrParenthesis()?.text
                val nextStatement = block.nextStatement()?.unwrapBlockOrParenthesis()?.text
                val currentStatement = block.unwrapBlockOrParenthesis().text

                if (previousStatement == currentStatement) {
                    return@run
                }

                if (currentStatement == nextStatement) {
                    holder.registerProblem(block, "Replace with loop", ReplaceWithForQuickFix(block.createSmartPointer()))
                }
            }
        }
    }
}
