package org.ice1000.slisp

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.*
import com.intellij.psi.FileViewProvider
import javax.swing.Icon

const val S_LISP_NAME = "S-Lisp"
const val S_LISP_EXTENSION = "lisp"
const val S_LISP_DESCRIPTION = "S-Lisp Programming Language"

object SLispFileType : LanguageFileType(SLispLanguage.INSTANCE) {
	override fun getIcon(): Icon? = null
	override fun getName() = S_LISP_NAME
	override fun getDefaultExtension() = S_LISP_EXTENSION
	override fun getDescription() = S_LISP_DESCRIPTION
}

class SLispFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, SLispLanguage.INSTANCE) {
	override fun getFileType() = SLispFileType
}

class SLispFileTypeFactory : FileTypeFactory() {
	override fun createFileTypes(consumer: FileTypeConsumer) {
		consumer.consume(SLispFileType, S_LISP_EXTENSION)
	}
}
