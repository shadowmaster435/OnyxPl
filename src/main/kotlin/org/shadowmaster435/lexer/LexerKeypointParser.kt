package org.shadowmaster435.lexer

import org.shadowmaster435.lexer.LexerScopeType
import org.shadowmaster435.tokenizer.Token
import org.shadowmaster435.tokenizer.TokenType
import java.util.Stack

object LexerKeypointParser {
    private var lastScopeType: LexerScopeType = LexerScopeType.FILE
    private var lastTokenType: TokenType = TokenType.NONE
    private var currentHeaderType = LexerHeaderType.NONE
    private var headerTypeStack = Stack<LexerHeaderType>()
    private var currentScopeType: LexerScopeType = LexerScopeType.FILE
    private var scopeTypeStack = Stack<LexerScopeType>()
    private var scopeUnchanged = false

    fun parse(tokens: List<Token>): List<LexerKeypoint> {
        lastTokenType = TokenType.NONE
        lastScopeType = LexerScopeType.FILE
        currentHeaderType = LexerHeaderType.NONE
        currentScopeType = LexerScopeType.FILE
        headerTypeStack.clear()
        scopeTypeStack.clear()
        val keypoints = mutableListOf<LexerKeypoint>()
        headerTypeStack.push(LexerHeaderType.NONE)
        scopeTypeStack.push(LexerScopeType.FILE)

        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            val headerAction = updateHeaderType(token)
            when(headerAction) {
                StackAction.PUSH -> headerTypeStack.push(LexerHeaderType.NONE) // at this point it is no longer in a header so we push NONE
                StackAction.POP -> { // return to previous header type when a valid group closer is encountered
                    headerTypeStack.pop()
                    currentHeaderType = headerTypeStack.peek()
                }
                else -> {}
            }
            updateScopeType(token)

            if (scopeUnchanged) {
                if (token.type.isOperator) {
//                    val next = tokens.getOrNull(i + 1)
//                    val afterNext = tokens.getOrNull(i + 2)
//                    val isNumber = next != null && (
//                                (number.matches(next.tokenString)) ||
//                                (next.tokenString == "." && afterNext != null && number.matches(afterNext.tokenString))
//                    )
//                    if (!isNumber)
                    keypoints.add(LexerKeypoint(currentScopeType, i, token, LexerKeypointType.OPERATOR))
                }

                else if (token.type == TokenType.STRING)
                    keypoints.add(LexerKeypoint(currentScopeType,i, token, LexerKeypointType.STRING))
            }

            if (currentScopeType != lastScopeType) {
                keypoints.add(
                    LexerKeypoint(currentScopeType,i, token, LexerKeypointType.SCOPE)
                )
            }
            lastScopeType = currentScopeType
            lastTokenType = token.type
            i++
        }
        return keypoints
    }

    private fun updateHeaderType(token: Token): StackAction {
        when(token.type) {
            TokenType.FUNC -> currentHeaderType = headerTypeStack.push(LexerHeaderType.FUNCTION)
            TokenType.OBJECT, TokenType.INTERFACE, TokenType.CLASS, TokenType.ENUM -> currentHeaderType = headerTypeStack.push(LexerHeaderType.CLASS)
            TokenType.STRUCT -> currentHeaderType = headerTypeStack.push(LexerHeaderType.STRUCT)
            else -> {}
        }

        return when(token.type) {
            TokenType.OPEN_PARENTHESIS, TokenType.OPEN_BRACE, TokenType.OPEN_BRACKET -> StackAction.PUSH
            TokenType.CLOSE_BRACE, TokenType.CLOSE_PARENTHESIS, TokenType.CLOSE_BRACKET -> StackAction.POP
            else -> StackAction.PASS
        }
    }



    private fun updateScopeType(token: Token) {
        scopeUnchanged = false
        val type = token.type
        val sTypes = type.subtypes
        var isPushScope = false
        var isPopScope = false
        fun has(subtype: TokenType.TokenSubtype) = sTypes.contains(subtype)
        fun checkPushPop(): Boolean {
            if (isPushScope) scopeTypeStack.push(currentScopeType)
            if (isPopScope) {
                scopeTypeStack.pop()
                currentScopeType = scopeTypeStack.peek()
            }
            return isPopScope || isPushScope
        }
        //region Group Closers
        when (type) {
            TokenType.CLOSE_BRACKET, TokenType.CLOSE_PARENTHESIS, TokenType.CLOSE_BRACE -> isPopScope = true
            TokenType.GREATER -> isPopScope =
                currentScopeType.polymorphic && (lastTokenType == TokenType.GREATER || lastTokenType == TokenType.GENERIC)

            else -> {}
        }
        //endregion
        if (checkPushPop()) return
        //region Modifiers
        if (currentScopeType != LexerScopeType.MODIFIERS) {
            if (has(TokenType.TokenSubtype.MODIFIER)) {
                currentScopeType = LexerScopeType.MODIFIERS
                isPushScope = true
            }
        } else if (!has(TokenType.TokenSubtype.MODIFIER) && currentScopeType == LexerScopeType.MODIFIERS) {
            if (lastTokenType.subtypes.contains(TokenType.TokenSubtype.MODIFIER)) {
                scopeTypeStack.pop()
                currentScopeType = scopeTypeStack.peek()
            } else {


                isPopScope = true
            }
        }
        //endregion
        if (checkPushPop()) return
        //region Open Bracket
        if (type == TokenType.OPEN_BRACKET) {
            isPushScope = true

            when (currentHeaderType) {
                LexerHeaderType.FUNCTION -> {
                    currentScopeType = LexerScopeType.FUNCTION_BODY
                }

                LexerHeaderType.STRUCT -> currentScopeType = LexerScopeType.STRUCT_BODY
                LexerHeaderType.CLASS -> when (currentScopeType) {
                    LexerScopeType.INTERFACE_HEADER -> currentScopeType = LexerScopeType.INTERFACE_BODY
                    LexerScopeType.ENUM_HEADER -> currentScopeType = LexerScopeType.ENUM_BODY
                    LexerScopeType.STRUCT_HEADER -> currentScopeType = LexerScopeType.STRUCT_BODY
                    LexerScopeType.OBJECT_HEADER, LexerScopeType.CLASS_HEADER -> currentScopeType =
                        LexerScopeType.CLASS_BODY

                    else -> isPushScope = false
                }

                else -> isPushScope = false
            }
        }
        //endregion
        if (checkPushPop()) return
        //region Polymorphic Group Closer
        if (type == TokenType.LESS) {
            isPushScope = true
            if (lastTokenType == TokenType.GENERIC) { // messy
                if (currentScopeType.supportsFunctionCalls) currentScopeType =
                    LexerScopeType.FUNCTION_CALL_POLYMORPHIC_INPUTS
                else if (currentHeaderType == LexerHeaderType.CLASS) currentScopeType =
                    LexerScopeType.CLASS_POLYMORPHIC_TYPEDEF
                else if (currentHeaderType == LexerHeaderType.FUNCTION) currentScopeType =
                    LexerScopeType.FUNCTION_POLYMORPHIC_TYPEDEF
                else isPushScope = false
            } else isPushScope = false
        }
        //endregion
        if (checkPushPop()) return
        //region Function Params
        if (type == TokenType.OPEN_PARENTHESIS) {
            isPushScope = true

            if (lastTokenType == TokenType.GENERIC && currentScopeType != LexerScopeType.FUNCTION_HEADER || lastTokenType == TokenType.GREATER && currentScopeType.supportsFunctionCalls)
                currentScopeType = LexerScopeType.FUNCTION_CALL_INPUTS
            else if (currentScopeType == LexerScopeType.CLASS_HEADER) currentScopeType =
                LexerScopeType.CONSTRUCTOR_PARAMS
            else if (currentScopeType == LexerScopeType.FUNCTION_HEADER) currentScopeType =
                LexerScopeType.FUNCTION_PARAMS
            else if (currentScopeType == LexerScopeType.STRUCT_HEADER) currentScopeType = LexerScopeType.STRUCT_PARAMS
            else if (currentScopeType == LexerScopeType.CONSTRUCTOR_EXTENSION) currentScopeType =
                LexerScopeType.CONSTRUCTOR_EXTENSION_PARAMS
            else isPushScope = false
        }
        //endregion
        if (checkPushPop()) return
        //region Bracket Bodies
        if (currentScopeType.supportsBodyTypeNesting) {
            isPushScope = true
            when (type) {
                TokenType.CLASS -> {
                    currentScopeType = LexerScopeType.CLASS_HEADER
                }
                TokenType.STRUCT -> currentScopeType = LexerScopeType.STRUCT_HEADER
                TokenType.INTERFACE -> currentScopeType = LexerScopeType.INTERFACE_HEADER
                TokenType.ENUM -> currentScopeType = LexerScopeType.ENUM_HEADER
                TokenType.OBJECT -> currentScopeType = LexerScopeType.OBJECT_HEADER
                else -> isPushScope = false
            }
        }
        //endregion
        if (checkPushPop()) return
        //region Header Scopes
        when(type) {
            TokenType.DEFINE -> currentScopeType = LexerScopeType.DEFINE
            TokenType.UNDEFINE -> currentScopeType = LexerScopeType.UNDEFINE
            TokenType.FUNC -> currentScopeType = LexerScopeType.FUNCTION_HEADER
            else -> isPushScope = false
        }
        //endregion
        if (checkPushPop()) return
        //region Default params
        isPushScope = true
        if (type.subtypes.contains(TokenType.TokenSubtype.ASSIGNMENT) && currentScopeType.supportsFunctionCalls) {
            when (currentScopeType) {
                LexerScopeType.CONSTRUCTOR_PARAMS -> currentScopeType = LexerScopeType.CONSTRUCTOR_DEFAULT_PARAM
                LexerScopeType.FUNCTION_PARAMS -> currentScopeType = LexerScopeType.FUNCTION_DEFAULT_PARAM
                LexerScopeType.STRUCT_PARAMS -> currentScopeType = LexerScopeType.STRUCT_DEFAULT_PARAM
                else -> isPushScope = false
            }
        } else isPushScope = false
        //endregion
        if (checkPushPop()) return
        //region Group Openers
        when(type) {
            TokenType.OPEN_BRACKET, TokenType.OPEN_PARENTHESIS, TokenType.OPEN_BRACE -> isPushScope = true
            TokenType.LESS -> isPushScope = !currentScopeType.polymorphic && (lastTokenType == TokenType.LESS || lastTokenType == TokenType.GENERIC)
            else -> {}
        }
        //endregion
        checkPushPop()
        scopeUnchanged = true
    }




    class LexerKeypoint(val scopeType: LexerScopeType, val index: Int, val token: Token, val type: LexerKeypointType) {
        override fun toString(): String {
            return "LexerKeypoint(scopeType=$scopeType, index=$index, token=${token.tokenString}, type=${type})"
        }
    }
    private enum class StackAction {
        PUSH,
        POP,
        PASS
    }

    private enum class LexerHeaderType {
        NONE,
        FUNCTION,
        CLASS,
        STRUCT
    }

    enum class LexerKeypointType {
        SCOPE,
        OPERATOR,
        STRING,
    }


}