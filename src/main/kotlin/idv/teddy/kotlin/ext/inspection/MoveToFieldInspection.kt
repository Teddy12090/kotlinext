package idv.teddy.kotlin.ext.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import idv.teddy.kotlin.ext.quickfix.MoveToFieldQuickFix
import org.jetbrains.kotlin.idea.codeinsight.api.classic.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.psi.propertyVisitor
import org.jetbrains.kotlin.psi.psiUtil.createSmartPointer

class MoveToFieldInspection : AbstractKotlinInspection() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
    ): PsiElementVisitor {
        return propertyVisitor { block ->
            run {
                if (block.isLocal) {
                    holder.registerProblem(block, "Move to field", MoveToFieldQuickFix(block.createSmartPointer()))
                }
            }
        }
    }
}
