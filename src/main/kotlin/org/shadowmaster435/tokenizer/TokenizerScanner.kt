package org.shadowmaster435.tokenizer

class TokenizerScanner(val tokenString: String, val type: TokenType) {
    val len = tokenString.length
    override fun toString() = "String: $tokenString, Type: $type"
}