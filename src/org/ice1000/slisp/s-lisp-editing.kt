package org.ice1000.slisp

import com.intellij.lang.*
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.ice1000.slisp.psi.SLispTypes

class SLispCommenter : Commenter {
	override fun getCommentedBlockCommentPrefix(): String? = null
	override fun getCommentedBlockCommentSuffix(): String? = null
	override fun getBlockCommentPrefix() = "`"
	override fun getBlockCommentSuffix() = "`"
	override fun getLineCommentPrefix(): String? = null
}

class SLispBraceMatcher : PairedBraceMatcher {
	override fun getPairs() = PAIRS
	override fun isPairedBracesAllowedBeforeType(p0: IElementType, p1: IElementType?) = true
	override fun getCodeConstructStart(p0: PsiFile?, p1: Int) = p1

	companion object {
		@JvmField
		val PAIRS = arrayOf(
				BracePair(SLispTypes.LEFT_BRACE, SLispTypes.RIGHT_BRACE, false),
				BracePair(SLispTypes.LEFT_BRACKET, SLispTypes.RIGHT_BRACKET, false),
				BracePair(SLispTypes.LEFT_PARENTHESE, SLispTypes.RIGHT_PARENTHESE, false))
	}
}
