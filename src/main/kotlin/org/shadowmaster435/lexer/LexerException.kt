package org.shadowmaster435.lexer

import org.shadowmaster435.tokenizer.Token

class LexerException(line: Int, val msg: String) : RuntimeException("Error at line $line with message: $msg") {
}