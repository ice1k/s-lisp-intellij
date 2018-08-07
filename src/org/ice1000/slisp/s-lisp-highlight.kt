package org.ice1000.slisp

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import org.ice1000.slisp.psi.SLispTypes

object SLispSyntaxHighlighter : SyntaxHighlighter {
	@JvmField val COMMENT = TextAttributesKey.createTextAttributesKey("S_LISP_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
	@JvmField val COMMENT_KEY = arrayOf(COMMENT)
	@JvmField val STRING = TextAttributesKey.createTextAttributesKey("S_LISP_STRING", DefaultLanguageHighlighterColors.STRING)
	@JvmField val STRING_KEY = arrayOf(STRING)

	override fun getHighlightingLexer() = SLispLexerAdapter()
	override fun getTokenHighlights(type: IElementType?): Array<TextAttributesKey> = when (type) {
		SLispTokenType.COMMENT -> COMMENT_KEY
		SLispTypes.STR -> STRING_KEY
		else -> emptyArray()
	}
}

class SLispSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
	override fun getSyntaxHighlighter(p0: Project?, p1: VirtualFile?) =
			SLispSyntaxHighlighter
}

class SLispAnnotator : Annotator {
	override fun annotate(element: PsiElement, holder: AnnotationHolder) {
	}
}
