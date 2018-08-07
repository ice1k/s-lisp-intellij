package org.ice1000.slisp

import com.intellij.lang.*
import com.intellij.lexer.FlexAdapter
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.*
import org.ice1000.slisp.psi.SLispTypes

class SLispLexerAdapter : FlexAdapter(SLispLexer())

class SLispParserDefinition : ParserDefinition {
	private companion object {
		private val FILE = IFileElementType(SLispLanguage.INSTANCE)
	}

	override fun createParser(project: Project?): PsiParser = SLispParser()
	override fun createFile(viewProvider: FileViewProvider) = SLispFile(viewProvider)
	override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?) =
			ParserDefinition.SpaceRequirements.MAY

	override fun getStringLiteralElements() = SLispTokenType.STRINGS
	override fun getFileNodeType() = FILE
	override fun createLexer(project: Project?) = SLispLexerAdapter()
	override fun createElement(node: ASTNode?): PsiElement = SLispTypes.Factory.createElement(node)
	override fun getCommentTokens() = SLispTokenType.COMMENTS
	override fun getWhitespaceTokens() = SLispTokenType.WHITE_SPACES
}

class SLispElementType(debugName: String) : IElementType(debugName, SLispLanguage.INSTANCE)

class SLispTokenType(debugName: String) : IElementType(debugName, SLispLanguage.INSTANCE) {
	companion object {
		@JvmField val STRINGS = TokenSet.create(SLispTypes.STRING)
		@JvmField val COMMENT = SLispTokenType("Comment")
		@JvmField val COMMENTS = TokenSet.create(SLispTokenType.COMMENT)
		@JvmField val WHITE_SPACES = TokenSet.WHITE_SPACE
	}
}
