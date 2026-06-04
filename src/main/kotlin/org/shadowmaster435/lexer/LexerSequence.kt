package org.shadowmaster435.lexer

import org.shadowmaster435.impl.DataHolder
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.tokenizer.Token
import org.shadowmaster435.tokenizer.TokenType
import java.util.Stack


class LexerSequence<T: DataProvider<*>> private constructor(private val list: List<SequenceEntry>) {
    private var ended = false
    private var lastCompiledEntry: CompiledEntry? = null
    private var i = 0
    private val current get() = list[i]

    private fun matchToken(inType: TokenType, matchType: TokenType): Boolean {
        return if (matchType == TokenType.ANY) true
        else inType == matchType
    }

    private fun checkEnd() {
        if (current.sequenceType == SequenceType.END_IF) {
            
        }
    }




    class LexerSequenceBuilder {
        private var building = true
        private var list = mutableListOf<SequenceEntry>()
        fun compiled(listener: () -> DataProvider<*>) {
            if (building) list.add(CompiledEntry(listener))
            else throw RuntimeException("Cannot call builder functions outside a builder")
        }
        fun token(string: String, type: TokenType) {
            if (building) list.add(SequenceEntry(SequenceType.TOKEN, string, type))
            else throw RuntimeException("Cannot call builder functions outside a builder")
        }
        fun token(type: TokenType) {
            if (building) list.add(SequenceEntry(SequenceType.TOKEN, "", type))
            else throw RuntimeException("Cannot call builder functions outside a builder")
        }
        /**
         * Ends the current (sub)sequence if the type and string of a token match the provided values
         */
        fun endIf(string: String, type: TokenType) {
            if (building) list.add(SequenceEntry(SequenceType.END_IF, string, type))
            else throw RuntimeException("Cannot call builder functions outside a builder")
        }
        /**
         * Ends the current (sub)sequence
         */
        fun end(string: String, type: TokenType) {
            if (building) list.add(SequenceEntry(SequenceType.END_IF, string, type))
            else throw RuntimeException("Cannot call builder functions outside a builder")
        }
        fun or(sequence: LexerSequence<*>) {
            if (building) list.add(OrSubSequenceEntry(sequence))
            else throw RuntimeException("Cannot call builder functions outside a builder")
        }
        fun or(string: String, type: TokenType) {
            if (building) list.add(OrSequenceEntry(SequenceType.OR, string, type))
            else throw RuntimeException("Cannot call builder functions outside a builder")
        }
        fun or(type: TokenType) {
            if (building) list.add(OrSequenceEntry(SequenceType.OR, "", type))
            else throw RuntimeException("Cannot call builder functions outside a builder")
        }
        fun subSequence(sequence: LexerSequence<*>) {
            if (building) list.add(SubSequenceEntry(sequence))
            else throw RuntimeException("Cannot call builder functions outside a builder")
        }
        fun subSequence(init: LexerSequenceBuilder.() -> LexerSequence<*>) {
            if (building) list.add(SubSequenceEntry(init.invoke(LexerSequenceBuilder())))
            else throw RuntimeException("Cannot call builder functions outside a builder")
        }

        fun build(objBuilder: (List<SequenceEntry>)) : LexerSequence<*> {
            building = false
            val copy = buildList { addAll(list) }
            list.clear()
            return LexerSequence<DataProvider<*>>(copy)
        }

    }

    companion object {
        operator fun invoke(init: LexerSequenceBuilder.() -> LexerSequence<*>): LexerSequence<*> {
            return init.invoke(LexerSequenceBuilder())
        }
    }
    open class SequenceEntry(val sequenceType: SequenceType, val string: String, val tokenType: TokenType)
    open class OrSequenceEntry(sequenceType: SequenceType, string: String, tokenType: TokenType): SequenceEntry(sequenceType, string, tokenType)
    class SubSequenceEntry(val subSequence: LexerSequence<*>): SequenceEntry(SequenceType.SUBSEQUENCE, "", TokenType.ANY)
    class OrSubSequenceEntry(val subSequence: LexerSequence<*>): SequenceEntry(SequenceType.OR, "", TokenType.ANY)
    class CompiledEntry(val compileListener: () -> DataProvider<*>): SequenceEntry(SequenceType.COMPILED, "", TokenType.ANY) {
        lateinit var compiled: DataProvider<*>
    }


    enum class SequenceType {
        DATA_PROVIDER,
        TOKEN,
        SUBSEQUENCE,
        OR,
        END_IF,
        COMPILED
    }
}