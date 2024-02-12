package idv.teddy.kotlin.ext.intention

import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementFactory
import com.intellij.psi.util.parentOfType
import com.intellij.psi.util.prevLeaf
import org.jetbrains.kotlin.psi.KtFunction
import org.jetbrains.kotlin.psi.KtProperty

class MoveToPropertyIntention : PsiElementBaseIntentionAction(), IntentionAction {
    override fun getFamilyName(): String {
        return "Move to property"
    }

    override fun getText(): String {
        return familyName
    }

    override fun isAvailable(
        project: Project,
        editor: Editor?,
        element: PsiElement,
    ): Boolean {
        return element.getLocalVariable() != null
    }

    override fun invoke(
        project: Project,
        editor: Editor?,
        element: PsiElement,
    ) {
        val propertyElement = checkNotNull(element.getLocalVariable())
        val targetPlace = getTargetPlace(propertyElement)
        propertyElement.delete()

        targetPlace.parent.addBefore(
            PsiElementFactory.getInstance(project).createStatementFromText("private ${propertyElement.text}", null),
            targetPlace,
        )
        ReformatCodeProcessor(targetPlace.containingFile, true).run()
    }

    private fun PsiElement.getLocalVariable(): PsiElement? {
        return (parentOfType<KtProperty>() ?: prevLeaf(true)?.parentOfType<KtProperty>()).let {
            if (it?.isLocal == true) it else null
        }
    }

    private fun getTargetPlace(block: PsiElement): PsiElement {
        return if (block is KtFunction) block else getTargetPlace(block.parent)
    }
}
