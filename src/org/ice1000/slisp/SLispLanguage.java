package org.ice1000.slisp;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

import static org.ice1000.slisp.S_lisp_infoKt.S_LISP_NAME;

public class SLispLanguage extends Language {
	public static final @NotNull
	SLispLanguage INSTANCE = new SLispLanguage();

	private SLispLanguage() {
		super(S_LISP_NAME, "text/" + S_LISP_NAME);
	}
}
