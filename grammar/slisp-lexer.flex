package org.ice1000.slisp;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import org.ice1000.slisp.psi.SLispTypes;

%%

%{
	public SLispLexer() { this((java.io.Reader) null); }
%}

%class SLispLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{ return;
%eof}

INCOMPLETE_COMMENT=`[^`]*
COMMENT={INCOMPLETE_COMMENT}`

WHITE_SPACE=[ \n\r\t]

INCOMPLETE_STRING=\"([^\"\\]|\\[^])*
STRING_LITERAL={INCOMPLETE_STRING}\"

DIGIT=[0-9]

HEX_NUM=0[xX][0-9a-fA-F]+
OCT_NUM=0[oO][0-7]+
BIN_NUM=0[bB][01]+
DEC_NUM={DIGIT}+[dDfFbBsSlLnNmM]?
FLOAT={DIGIT}+\.{DIGIT}+[dDfFmM]?
NUMBER=-?({BIN_NUM}|{OCT_NUM}|{DEC_NUM}|{HEX_NUM}|{FLOAT})

SYMBOL_CHAR=[a-zA-Z!@$\^&_:=<|>?.\\+\-~*/%#]
SYMBOL={SYMBOL_CHAR}({SYMBOL_CHAR}|{DIGIT})*

UNKNOWN_CHARACTER=[^a-zA-Z!@$\^&_:=<|>?.\\+\-~*/%#0-9\";]

%state AFTER_NUM

%%

<AFTER_NUM> {SYMBOL}
	{ yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }

{NUMBER}
	{ yybegin(AFTER_NUM); return SLispTypes.NUM; }

\( { yybegin(YYINITIAL); return SLispTypes.LEFT_PARENTHESE; }
\) { yybegin(YYINITIAL); return SLispTypes.RIGHT_PARENTHESE; }
\[ { yybegin(YYINITIAL); return SLispTypes.LEFT_BRACKET; }
\] { yybegin(YYINITIAL); return SLispTypes.RIGHT_BRACKET; }
\{ { yybegin(YYINITIAL); return SLispTypes.LEFT_BRACE; }
\} { yybegin(YYINITIAL); return SLispTypes.RIGHT_BRACE; }
\' { yybegin(YYINITIAL); return SLispTypes.QUOTE; }

{INCOMPLETE_STRING}
	{ yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }
{STRING_LITERAL}
	{ yybegin(YYINITIAL); return SLispTypes.STR; }

{INCOMPLETE_COMMENT}
	{ yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }
{COMMENT}
	{ yybegin(YYINITIAL); return SLispTokenType.COMMENT; }

{SYMBOL}
	{ yybegin(YYINITIAL); return SLispTypes.SYM; }

{WHITE_SPACE}+
	{ yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

{UNKNOWN_CHARACTER}
	{ yybegin(YYINITIAL); return TokenType.BAD_CHARACTER; }

