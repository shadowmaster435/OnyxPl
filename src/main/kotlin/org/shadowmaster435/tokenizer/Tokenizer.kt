package org.shadowmaster435.tokenizer

import org.shadowmaster435.util.IntIterHolder

object Tokenizer {

    fun init() {}

    private val constantTokenizers = buildList {

        add(TokenizerScanner("+", TokenType.ADD))
        add(TokenizerScanner("-", TokenType.SUB))
        add(TokenizerScanner("*", TokenType.MUL))
        add(TokenizerScanner("/", TokenType.DIV))
        add(TokenizerScanner("%", TokenType.MOD))
        add(TokenizerScanner("!", TokenType.NOT))
        add(TokenizerScanner("^", TokenType.XOR))
        add(TokenizerScanner("@", TokenType.AT))
        add(TokenizerScanner("$", TokenType.DOLLAR_SIGN))
        add(TokenizerScanner("&", TokenType.BIT_AND))
        add(TokenizerScanner("|", TokenType.BIT_OR))
        add(TokenizerScanner("~", TokenType.BIT_NOT))
        add(TokenizerScanner("=", TokenType.ASSIGN))
        add(TokenizerScanner("\\", TokenType.REF_NOT_EQUAL))
        add(TokenizerScanner("'", TokenType.CHAR))
        add(TokenizerScanner("\"", TokenType.STRING))
        add(TokenizerScanner(";", TokenType.INLINE_BREAK))
        add(TokenizerScanner(":", TokenType.SEMICOLON))
        add(TokenizerScanner("?", TokenType.NULLABLE))
        add(TokenizerScanner(".", TokenType.ACCESS))
        add(TokenizerScanner("<", TokenType.LESS))
        add(TokenizerScanner(">", TokenType.GREATER))
        add(TokenizerScanner(",", TokenType.COMMA))
        add(TokenizerScanner("++", TokenType.INC))
        add(TokenizerScanner("--", TokenType.DEC))
        add(TokenizerScanner("==", TokenType.EQUAL))
        add(TokenizerScanner("::", TokenType.METHOD_CALL))
        add(TokenizerScanner(">>", TokenType.SHR))
        add(TokenizerScanner("<<", TokenType.SHL))
        add(TokenizerScanner("**", TokenType.POW))
        add(TokenizerScanner("&&", TokenType.AND))
        add(TokenizerScanner("||", TokenType.OR))
        add(TokenizerScanner("!!", TokenType.NOT_NULL))
        add(TokenizerScanner("//", TokenType.COMMENT))
        add(TokenizerScanner("?:", TokenType.ELVIS))
        add(TokenizerScanner("?.", TokenType.NULLABLE_ACCESS))
        add(TokenizerScanner("+=", TokenType.ADD_ASSIGN))
        add(TokenizerScanner("-=", TokenType.SUB_ASSIGN))
        add(TokenizerScanner("/=", TokenType.DIV_ASSIGN))
        add(TokenizerScanner("*=", TokenType.MUL_ASSIGN))
        add(TokenizerScanner("%=", TokenType.MOD_ASSIGN))
        add(TokenizerScanner(">=", TokenType.GREATER_EQ))
        add(TokenizerScanner("<=", TokenType.LESS_EQ))
        add(TokenizerScanner("!=", TokenType.NOT_EQUAL))
        add(TokenizerScanner("->", TokenType.LAMBDA_RIGHT))
        add(TokenizerScanner("<-", TokenType.LAMBDA_LEFT))
        add(TokenizerScanner("===", TokenType.REF_EQUAL))
        add(TokenizerScanner("\"\"\"", TokenType.STRING))
        add(TokenizerScanner(">>>", TokenType.USHR))
        add(TokenizerScanner("!==", TokenType.REF_NOT_EQUAL))
        add(TokenizerScanner("(", TokenType.OPEN_PARENTHESIS))
        add(TokenizerScanner(")", TokenType.CLOSE_PARENTHESIS))
        add(TokenizerScanner("{", TokenType.OPEN_BRACKET))
        add(TokenizerScanner("}", TokenType.CLOSE_BRACKET))
        add(TokenizerScanner("[", TokenType.OPEN_BRACE))
        add(TokenizerScanner("]", TokenType.CLOSE_BRACE))

        add(TokenizerScanner("define", TokenType.DEFINE))
        add(TokenizerScanner("!is", TokenType.IS_NOT))
        add(TokenizerScanner("!as", TokenType.SAFE_AS))
        add(TokenizerScanner("as", TokenType.AS))
        add(TokenizerScanner("is", TokenType.IS))
        add(TokenizerScanner("try", TokenType.TRY))
        add(TokenizerScanner("catch", TokenType.CATCH))
        add(TokenizerScanner("finally", TokenType.FINALLY))
        add(TokenizerScanner("final", TokenType.FINAL))
        add(TokenizerScanner("undefine", TokenType.UNDEFINE))
        add(TokenizerScanner("class", TokenType.CLASS))
        add(TokenizerScanner("file", TokenType.FILE))
        add(TokenizerScanner("static", TokenType.STATIC))
        add(TokenizerScanner("object", TokenType.OBJECT))
        add(TokenizerScanner("public", TokenType.PUBLIC))
        add(TokenizerScanner("protected", TokenType.PROTECTED))
        add(TokenizerScanner("private", TokenType.PRIVATE))
        add(TokenizerScanner("struct", TokenType.STRUCT))
        add(TokenizerScanner("val", TokenType.VAL))
        add(TokenizerScanner("var", TokenType.VAR))
        add(TokenizerScanner("vararg", TokenType.VARARG))
        add(TokenizerScanner("const", TokenType.CONST))
        add(TokenizerScanner("null", TokenType.NULL))
        add(TokenizerScanner("global", TokenType.GLOBAL))
        add(TokenizerScanner("fun", TokenType.FUNC))
        add(TokenizerScanner("this", TokenType.THIS))
        add(TokenizerScanner("enum", TokenType.ENUM))
        add(TokenizerScanner("interface", TokenType.INTERFACE))
        add(TokenizerScanner("abstract", TokenType.ABSTRACT))
        add(TokenizerScanner("package", TokenType.PACKAGE))
        add(TokenizerScanner("import", TokenType.IMPORT))
        add(TokenizerScanner("operators", TokenType.OP_WORD))
        add(TokenizerScanner("type", TokenType.TYPE))
        add(TokenizerScanner("keyword", TokenType.KEYWORD))
        add(TokenizerScanner("if", TokenType.IF))
        add(TokenizerScanner("else", TokenType.ELSE))
        add(TokenizerScanner("when", TokenType.WHEN))
        add(TokenizerScanner("where", TokenType.WHERE))
        add(TokenizerScanner("for", TokenType.FOR))
        add(TokenizerScanner("while", TokenType.WHILE))
        add(TokenizerScanner("do", TokenType.DO))
        add(TokenizerScanner("return", TokenType.RETURN))
        add(TokenizerScanner("break", TokenType.BREAK))
        add(TokenizerScanner("continue", TokenType.CONTINUE))
        add(TokenizerScanner("true", TokenType.TRUE))
        add(TokenizerScanner("false", TokenType.FALSE))
        sortWith(Comparator { tokenizer, tokenizer1 -> tokenizer1.len - tokenizer.len })
    }

    fun tokenize(string: String, preserveNonStringSpaces: Boolean = false) = buildList {
        val string = "$string " // make single character nonsense not happen
        val iter = IntIterHolder()
        var currentGeneric = ""
        var scopeLevel = 0
        var inString = false
        while (iter.i < string.length) {
            var shouldInc = true
            val stringEnd = iter.i + 1 >= string.length
            if (wordGap.matches(string[iter.i].toString()) || stringEnd) {
                if (currentGeneric.isNotEmpty()) {
                    var letterOp = false
                    val actualGeneric = currentGeneric + if (stringEnd) {
                        val char = string.last().toString()
                        if (whiteSpaceChar.matches(char)) ""
                        else char
                    } else ""
                    constantTokenizers.forEach { scanner ->
                        if (actualGeneric == scanner.tokenString) {
                            if (scanner.type == TokenType.STRING ) inString = !inString
                            add(Token(scanner.type, actualGeneric, scopeLevel, getFlags(scanner.type), iter.i))
                            letterOp = true
                        }
                    }
                    if (!letterOp) add(Token(TokenType.GENERIC, actualGeneric, scopeLevel, 0, iter.i))
                }

                if (string[iter.i].toString() == " " && if (!inString && preserveNonStringSpaces) true else inString) add(Token(TokenType.SPACE, " ", scopeLevel, 0, iter.i))
                if (string[iter.i].toString() == "\n") add(Token(TokenType.NEWLINE, "\n", scopeLevel, 0, iter.i))
                constantTokenizers.forEach { scanner ->
                    if (iter.i + scanner.len < string.length + 1) {
                        val substr = string.substring(iter.i..<(iter.i + scanner.len))
                        if (substr == scanner.tokenString) {
                            if (scanner.type.subtypes.contains(TokenType.TokenSubtype.GROUP_END)) scopeLevel -= 1
                            val token = Token(scanner.type, scanner.tokenString, scopeLevel, getFlags(scanner.type), iter.i)
                            add(token)
                            if (scanner.type.subtypes.contains(TokenType.TokenSubtype.GROUP_START)) scopeLevel += 1
                            iter.inc(scanner.len)
                            shouldInc = false
                            return@forEach
                        }
                    }
                }
                currentGeneric = ""
            } else currentGeneric += string[iter.i].toString()
            if (shouldInc) iter.inc()
        }
    }

    private fun getFlags(type: TokenType): Int {
        return if (unaryOps.contains(type)) 0b10
        else if (optionallyUnaryOps.contains(type)) 0b100
        else 0
    }

    private val unaryOps = mutableListOf(
        TokenType.NOT, TokenType.INC, TokenType.DEC, TokenType.AT, TokenType.DOLLAR_SIGN,
        TokenType.BIT_NOT, TokenType.ACCESS, TokenType.NULLABLE,
      //  "!", "++", "--", "@", "$", "~", "."
    )
    private val optionallyUnaryOps = mutableListOf(
        TokenType.METHOD_CALL, TokenType.NOT_NULL
    )

    //region Regexes
    private val wordGap = Regex("[+\\-.<>/?:,=!^|&*~%;$@\"'\\[\\](){}\\s]")
    private val whiteSpaceChar = Regex("\\s")
    private val wordFollowup = Regex("([\\w_])*")
    private val nonNumberTokens = Regex("[a-zA-Z_]")
    //endregion

}