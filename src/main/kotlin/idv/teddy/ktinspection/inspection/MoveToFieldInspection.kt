package idv.teddy.ktinspection.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import idv.teddy.ktinspection.quickfix.MoveToFieldQuickFix
import org.jetbrains.kotlin.idea.codeinsight.api.classic.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.propertyVisitor

class MoveToFieldInspection : AbstractKotlinInspection() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
    ): PsiElementVisitor {
        var a = 0
        return propertyVisitor { block ->
            run {
                if (block.isLocal) {
                    holder.registerProblem(block, "Move to field", MoveToFieldQuickFix(block))
                }
            }
        }
    }
}
