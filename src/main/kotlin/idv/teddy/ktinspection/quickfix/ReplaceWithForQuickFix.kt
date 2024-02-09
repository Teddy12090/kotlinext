package idv.teddy.ktinspection.quickfix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.SmartPsiElementPointer
import org.jetbrains.eval4j.checkNull
import org.jetbrains.kotlin.idea.intentions.loopToCallChain.nextStatement
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.uast.kotlin.unwrapBlockOrParenthesis

class ReplaceWithForQuickFix(val expression: SmartPsiElementPointer<KtExpression>) : LocalQuickFix {
    override fun getFamilyName(): String {
        return "Replace with loop"
    }

    override fun applyFix(
        p0: Project,
        p1: ProblemDescriptor,
    ) {
        val element = checkNotNull(expression.element)
        val sameStatements = collectFollowingSameStatements()
        val new = KtPsiFactory.contextual(element).createExpression("for (i in 1 .. ${sameStatements.size}) { ${element.text} }")
        element.replace(new)
        sameStatements.forEach { it.delete() }
    }

    private fun collectFollowingSameStatements(): List<KtExpression> {
        val element = checkNotNull(expression.element)
        val text = element.unwrapBlockOrParenthesis().unwrapBlockOrParenthesis().text
        var next = element.nextStatement()

        return buildList {
            add(element)
            while (next?.unwrapBlockOrParenthesis()?.text == text) {
                add(next.checkNull())
                next = next?.nextStatement()
            }
        }
    }
}
