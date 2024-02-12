package idv.teddy.kotlin.ext.intention

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.codeInsight.intention.preview.IntentionPreviewInfo
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.parentOfType
import org.jetbrains.kotlin.psi.KtValueArgumentList

class FlipArgumentsIntention : PsiElementBaseIntentionAction(), IntentionAction {
    override fun getFamilyName(): String {
        return "Flip ',' (may change semantics)"
    }

    override fun getText(): String {
        return familyName
    }

    override fun generatePreview(
        project: Project,
        editor: Editor,
        file: PsiFile,
    ): IntentionPreviewInfo {
        val originalText = file.text
        val element = checkNotNull(file.findElementAt(editor.caretModel.offset))
        invoke(project, editor, element)
        val newText = file.text

        return IntentionPreviewInfo.CustomDiff(
            file.fileType,
            originalText,
            newText,
        )
    }

    override fun isAvailable(
        project: Project,
        editor: Editor?,
        element: PsiElement,
    ): Boolean {
        return element.parentOfType<KtValueArgumentList>()?.children?.size == 2
    }

    override fun invoke(
        project: Project,
        editor: Editor?,
        element: PsiElement,
    ) {
        val argumentList = checkNotNull(element.parentOfType<KtValueArgumentList>())
        val first = argumentList.arguments[0]
        argumentList.addArgument(first)
        argumentList.removeArgument(0)
        CodeStyleManager.getInstance(project).reformat(argumentList)
    }
}
