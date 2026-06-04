package org.shadowmaster435.lexer

import org.shadowmaster435.tokenizer.Token

class LexerScope(val scopeBounds: IntRange, private val tokens: List<Token>, val parentScope: LexerScope?, val scopeLevel: Int, val scopeType: LexerScopeType) {
    val isGlobal = parentScope == null

    fun forEachInScopeLevel(consumer: (token: Token) -> Unit) {
        var i = scopeBounds.first
        while (i in scopeBounds) {
            val token = tokens[i]
            if (token.scopeLevel == scopeLevel) consumer.invoke(token)
            i++
        }
    }


    fun forEachInScope(startIndex: Int, consumer: (token: Token) -> Unit) {
        var i = startIndex
        var currentScopeLevel = scopeLevel
        while(i > 0 && currentScopeLevel > -1) {
            val token = tokens[i]
            if (token.isInScope(scopeLevel)) {
                consumer.invoke(token)
                if (currentScopeLevel > token.scopeLevel) currentScopeLevel--
            }
            i--
        }
        if (currentScopeLevel < 0) throw RuntimeException("Illegal Scope Level")
    }


}