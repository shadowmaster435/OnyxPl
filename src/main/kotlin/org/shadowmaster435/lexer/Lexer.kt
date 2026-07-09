package org.shadowmaster435.lexer

import org.shadowmaster435.built_ins.OnyxBuiltinClasses
import org.shadowmaster435.code.fields.OnyxVal
import org.shadowmaster435.code.fields.OnyxVar
import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.Modifier
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.impl.abstracts.OnyxOperator
import org.shadowmaster435.impl.abstracts.OnyxUnaryOperator
import org.shadowmaster435.misc.OnyxConstable
import org.shadowmaster435.misc.OnyxExpression
import org.shadowmaster435.built_ins.OnyxPrimitives.*
import org.shadowmaster435.built_ins.OnyxPackages
import org.shadowmaster435.built_ins.OnyxPrimitives
import org.shadowmaster435.misc.OnyxModifiers
import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.code.OnyxFunction
import org.shadowmaster435.code.fields.OnyxField
import org.shadowmaster435.impl.OnyxCodeFile
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.misc.OnyxPackage
import org.shadowmaster435.modifiers.AbstractModifier
import org.shadowmaster435.modifiers.FinalModifier
import org.shadowmaster435.modifiers.GlobalModifier
import org.shadowmaster435.modifiers.MetaModifier
import org.shadowmaster435.modifiers.OverrideModifier
import org.shadowmaster435.modifiers.PackageModifier
import org.shadowmaster435.modifiers.PrivateModifier
import org.shadowmaster435.modifiers.ProtectedModifier
import org.shadowmaster435.modifiers.PublicModifier
import org.shadowmaster435.modifiers.StaticModifier
import org.shadowmaster435.modifiers.VarArgModifier
import org.shadowmaster435.operators.logical.OnyxIs
import org.shadowmaster435.operators.logical.OnyxIsNot
import org.shadowmaster435.operators.mathmatical.OnyxAdd
import org.shadowmaster435.operators.mathmatical.OnyxDiv
import org.shadowmaster435.operators.mathmatical.OnyxMod
import org.shadowmaster435.operators.mathmatical.OnyxMul
import org.shadowmaster435.operators.mathmatical.OnyxNegate
import org.shadowmaster435.operators.mathmatical.OnyxSub
import org.shadowmaster435.tokenizer.Token
import org.shadowmaster435.tokenizer.TokenType
import org.shadowmaster435.tokenizer.Tokenizer


typealias T = TokenType
object Lexer {

    private var i = 0
    private var tokenCount = 0
    private var lastNonWhiteSpace = 0
    private var lastWhiteSpace = 0
    private var lastLinebreak = 0
    private var inString = false
    private var currentString = ""

    private var lastObject: CodeObject? = null
    private var lastProvider: DataProvider? = null
    private var currentError: Throwable? = null
    private var line = 0

    private var obj : CodeObject? = null
    private fun parse(string: String) {
        return lex(Tokenizer.tokenize(string))
    }

    private fun isLexingOperators() = i < tokenCount

    private fun skipSpaces(tokens: List<Token>) {
        while(tokens[i].type == T.SPACE) i++
    }

    fun lexProject(projectName: String, tokens: HashMap<String, List<Token>>) {
        val lexer = ProjectLexer(projectName)
        val tokenKeypoints = HashMap<String, Pair<List<Token>, List<LexerKeypointParser.LexerKeypoint>>>()
        tokens.forEach {
            val keypoints = LexerKeypointParser.parse(it.value)
            tokenKeypoints[it.key] = it.value to keypoints
        }
        lexer.lexProject(tokenKeypoints)
    }

    private fun lex(tokens: List<Token>) {
        tokenCount = tokens.size
        while (i < tokenCount) {
            val current = tokens[i]
            val type = current.type
            if (type == T.NEWLINE) line += 1
            if (obj == null) obj = tryLexModifiers(tokens)

            if (currentError != null) throw currentError!!

            if (!inString) {
                if (type == T.NEWLINE || type == T.INLINE_BREAK) lastLinebreak = i
                if (type.subtypes.contains(TokenType.TokenSubtype.WHITESPACE)) lastWhiteSpace = i
                else lastNonWhiteSpace = i
            }
            i++
        }
        if (inString) throw LexerException(line, "String is missing closing quote")
    }

    private var lastExpressionProvider: DataProvider? = null
    fun lexExpression(tokens: List<Token>, keypoints: List<LexerKeypointParser.LexerKeypoint>, type: OnyxType): DataProvider {
        val tokens = tokens.filter { it.type != TokenType.NEWLINE }
        fun next(): Token? {
            return tokens.getOrNull(i + 1)
        }
        fun last(): Token? {
            return tokens.getOrNull(i - 1)
        }
        var lastProvider: DataProvider? = null
        val resultList = mutableListOf<Pair<OnyxOperator, List<DataProvider>>>()
        keypoints.forEachIndexed { index, keypoint ->
            val token = keypoint.token
            if (keypoint.type == LexerKeypointParser.LexerKeypointType.OPERATOR ) {
                val last = last()
                i = keypoint.index - if (last == null || !last.type.isOperator) 1 else 0
                val leftProvider = lastProvider ?: lastExpressionProvider ?: ConstableLexer.tryLexConstable(tokens)

                i += 1
                var skipped = false
                val provider = if (tokens[i].type == TokenType.OPEN_PARENTHESIS) {
                    i += 1

                    val expr = lexExpression(tokens, keypoints.slice((index + 2)..<keypoints.size), type)
                    i -= 1
                    skipped = true
                    expr
                } else ConstableLexer.tryLexConstable(tokens)

                val binaryLeft = token.hasSubType(TokenType.TokenSubtype.BINARY_OR_LEFT_OPERATOR)
                val binary = token.hasSubType(TokenType.TokenSubtype.BINARY_OPERATOR)
                val left = token.hasSubType(TokenType.TokenSubtype.LEFT_OPERATOR)
                val right = token.hasSubType(TokenType.TokenSubtype.RIGHT_OPERATOR)
                val leftRight = token.hasSubType(TokenType.TokenSubtype.LEFT_OR_RIGHT_OPERATOR)
                if (binary || binaryLeft) {
                    val actuallyUnary = binaryLeft && leftProvider == null && provider != null

                    if (!actuallyUnary) {
                        resultList.add(
                            createBinaryOperator(
                                (leftProvider ?: run {
                                    throw LexerException(line, "Missing expression before binary operator")
                                }),
                                (provider ?: throw LexerException(line, "Missing expression after binary operator")),
                                token.type
                            ) to listOf(leftProvider, provider)
                        )
                    } else {
                        resultList.add(createUnaryOperator(provider, token.type) to listOf(provider))
                    }
                    if (skipped) {
                        val expr = OnyxExpression(resultList, type)
                        lastExpressionProvider = expr
                        return expr
                    }

                    return@forEachIndexed
                }

                lastProvider = provider
            }
        }
        if (resultList.isEmpty()) return ConstableLexer.tryLexConstable(tokens)!!
        return OnyxExpression(resultList, type)
    }

    private fun createBinaryOperator(left: DataProvider, right: DataProvider, type: TokenType): OnyxBinaryOperator {
        return when(type) {
            TokenType.ADD -> OnyxAdd.create(left, right)
            TokenType.SUB -> OnyxSub.create(left, right)
            TokenType.MUL -> OnyxMul.create(left, right)
            TokenType.DIV -> OnyxDiv.create(left, right)
            TokenType.MOD -> OnyxMod.create(left, right)

            TokenType.IS -> OnyxIs(left.type, right.type)
            TokenType.IS_NOT -> OnyxIsNot(left.type, right.type)

            TokenType.ELVIS -> OnyxIs(left.type, right.type)

            else -> throw LexerException(line, "this should never happen")
        }
    }

    private fun createUnaryOperator(provider: DataProvider, type: TokenType): OnyxUnaryOperator {
        return when(type) {
            TokenType.SUB -> OnyxNegate.create(provider)
            else -> throw LexerException(line, "this should never happen")
        }
    }




    fun tryLexModifiers(tokens: List<Token>): OnyxModifiers? {
        var current = tokens[i]
        var entry : Modifier? = null
        val currentModifiers = mutableListOf<Modifier>()
        for (token in tokens) {
            val modifier = modifierMap[token.type]
            if (modifier != null) {
                currentModifiers.add(modifier)
            }
        }

        var failed = false
        currentModifiers.forEach {top ->
            currentModifiers.forEach {
                if (top != it) if (!top.chainsWith(it)) {
                    failed = true
                    return@forEach
                }
            }
        }
        return if (failed || currentModifiers.isEmpty()) {
            if (failed) currentError = LexerException(line, "Invalid modifier combination")
            null
        } else OnyxModifiers(currentModifiers)
    }

    private val modifierMap = mapOf(
        Pair(T.ABSTRACT, AbstractModifier),
        Pair(T.PUBLIC, PublicModifier),
        Pair(T.PROTECTED, ProtectedModifier),
        Pair(T.PRIVATE, PrivateModifier),
        Pair(T.PACKAGE, PackageModifier),
        Pair(T.GLOBAL, GlobalModifier),
        Pair(T.STATIC, StaticModifier),
        Pair(T.VARARG, VarArgModifier),
        Pair(T.OVERRIDE, OverrideModifier),
        Pair(T.FINAL, FinalModifier),
        Pair(T.META, MetaModifier),
    )

    private class ProjectLexer(val projectName: String) {
        val projectRootPackage = OnyxPackage(projectName, OnyxPackages.globalRoot)
        private var packageLexed = false
        fun lexProject(tokenMap: HashMap<String, Pair<List<Token>, List<LexerKeypointParser.LexerKeypoint>>>) {
            tokenMap.forEach { 
                val file = lexFile(it.value.first, it.value.second)
            }
        }

        fun lexFile(tokens: List<Token>, keypoints: List<LexerKeypointParser.LexerKeypoint>): OnyxCodeFile {
            val lexer = FileLexer(this)
            val pkg = lexer.lexPackage(tokens, keypoints)
            val imports = lexer.lexImports(tokens, keypoints)
            imports.forEach {
                println(it)
            }
            return OnyxCodeFile(pkg, imports)
        }

    }

    private class FileLexer(val projectLexer: ProjectLexer) {
        private fun validatePackageName(token: Token, import: Boolean) {
            if (token.type != TokenType.GENERIC || token.tokenString.contains(if (import) Regex("[^a-zA-Z*]") else Regex("[^a-zA-Z]")))
                throw LexerException(line, "Package names can only contain alphabetical characters\n Problematic string ${token.tokenString}")
        }
  

        fun lexPackage(tokens: List<Token>, keypoints: List<LexerKeypointParser.LexerKeypoint>): OnyxPackage {
            keypoints.find { it.type == LexerKeypointParser.LexerKeypointType.PACKAGE } ?: return projectLexer.projectRootPackage
            val pkgIndex = tokens.indexOfFirst { it.type == TokenType.NEWLINE }
            var pkg = projectLexer.projectRootPackage
            var separator = false
            val sliced = tokens.slice(i..<if (pkgIndex < 0) tokens.size else pkgIndex + 1)
            sliced.forEach {
                if (it.type == TokenType.ACCESS) {
                    if (separator) {
                        throw LexerException(line, "Empty sub package name")
                    }
                    else separator = true
                } else {
                    if (it.type != TokenType.PACKAGE) {
                        if (it.type != TokenType.NEWLINE) {
                            validatePackageName(it, false)
                            pkg = pkg.add(it.tokenString)
                            separator = false
                        } else return@forEach
                    }
                }
            }
            return pkg
        }

        fun lexImports(tokens: List<Token>, keypoints: List<LexerKeypointParser.LexerKeypoint>): HashSet<OnyxMember> {
            OnyxPackages.init()
            OnyxBuiltinClasses.staticInit()
            val imports = hashSetOf<OnyxMember>()
            val classNames = hashSetOf<String>()
            val funcNames = hashMapOf<String, OnyxFunction>()
            val fieldNames = hashSetOf<String>()
            fun checkMember(it: OnyxMember) {
                when(it) {
                    is OnyxClass ->
                        if (classNames.contains(it.type.name))
                            throw LexerException(line, "Duplicate Class Name ${it.type.name}")
                        else classNames.add(it.type.name)
                    is OnyxField ->
                        if (fieldNames.contains(it.name))
                            throw LexerException(line, "Duplicate Field ${it.name}")
                        else fieldNames.add(it.name)
                    is OnyxFunction -> {
                        val name = it.name + it.tuples.toString()
                        val func = funcNames[name]
                        if (func != null && (func.name + func.tuples.tuples) == name)
                            throw LexerException(line, "Duplicate Function ${it.name}")
                        else fieldNames.add(it.name + it.tuples.toString())
                    }
                    else -> {}
                }
            }
            
            keypoints.forEach { kp ->
                if (kp.type == LexerKeypointParser.LexerKeypointType.IMPORT) {
                    var qName = ""
                    tokens.slice((kp.index + 1)..<tokens.size).forEach subtokens@{
                        if (it.type == TokenType.NEWLINE) return@subtokens
                        if (it.type != TokenType.ACCESS) validatePackageName(it, true)
                        qName += it.tokenString
                    }
                    val wildcard = qName.endsWith("*")
                    if (wildcard) {
                        qName = qName.substring(0..<qName.lastIndexOf('.'))
                        val pkg = projectLexer.projectRootPackage.byQualifiedNameFromCurrent(qName) ?:
                            throw LexerException(line, "Unknown package $qName")
                        pkg.forEachMember { 
                            checkMember(it)
                            imports.add(it)
                        }
                    } else {
                        val member = OnyxPackage.getMember(qName) ?: throw LexerException(line, "Unknown member name $qName")
                        checkMember(member)
                        imports.add(member)
                    }
                }
            }
            return imports
        }

        fun lexSingleType(token: Token, imports: HashSet<OnyxClass>): OnyxClass {
            validatePackageName(token, false)
            return OnyxPackages.onyxLang.findClassBySimpleName(token.tokenString) ?: 
            imports.find { it.type.name == token.tokenString} ?: 
            throw LexerException(line, "Unknown type ${token.tokenString}")
        }
        

        fun lexField(tokens: List<Token>, keypointIndex: Int, keypoints: List<LexerKeypointParser.LexerKeypoint>, modifiers: OnyxModifiers = OnyxModifiers(listOf())): DataProvider {
            val startPoint = keypoints[keypointIndex].index
            var exprKeypointStartIndex = 0
            val exprStart = run {
                var i = keypointIndex
                while (i < keypoints.size) {
                    val kp = keypoints[i]
                    if (kp.type == LexerKeypointParser.LexerKeypointType.EXPRESSION_START) {
                        exprKeypointStartIndex = i
                        return@run kp.index
                    }
                    i++
                }
                -1
            }

            val tokens = tokens.slice(keypointIndex..<tokens.size)
            val isVar = tokens.first().type == TokenType.VAR
            val name = tokens[1].tokenString
            val hasDefinedType = tokens[2].type == TokenType.COLON
            val expr = lexExpression(tokens, keypoints.slice((exprKeypointStartIndex)..<keypoints.size), OnyxBuiltinClasses.OnyxIntClass.type)
            return if (isVar) OnyxVar(name, expr, modifiers) else OnyxVal(name, expr, modifiers)
        }
    }


    object ConstableLexer {

        fun tryLexString(tokens: List<Token>) : OnyxString? {

            val current = tokens[i]
            val last = tokens.getOrNull(i - 1)
            if (current.type == T.STRING) {
                if (inString && last?.type != T.BACKSLASH) {
                    val str = currentString.substring(1)
                    currentString = ""
                    return OnyxString(str)
                }
                inString = !inString
            }
            if (inString) currentString += current.tokenString
            return null
        }

        fun tryLexConstable(tokens: List<Token>): OnyxConstable? {
            val current = tokens[i]
            val startChar = current.tokenString.first()
            return if (current.type == T.NULL) OnyxNull
            else if (current.type == T.TRUE || current.type == T.FALSE) {
                OnyxBoolean(current.type == T.TRUE)}
            else if (numberWithNegative.matches(startChar.toString())) try {tryLexNumber(tokens, current)} catch (_: Throwable) {
                currentError = LexerException(line, "Failed to parse number"); null}
            else {currentError = LexerException(line, "Failed to parse constable"); null}
        }


        fun tryLexNumber(tokens: List<Token>, current: Token) : OnyxConstable {
            var current = current
            if (current.tokenString == "-") current = tokens[i+++1]
            val beforeLast = tokens.getOrNull(i - 2)
            val last = tokens.getOrNull(i - 1)
            val curStr = current.tokenString
            val next = tokens.getOrNull(i + 1)
            val afterNext = tokens.getOrNull(i + 2)
            val hasIntFloatMod = (curStr.endsWith("f") || curStr.endsWith("F"))
            val hasIntDoubleMod = (curStr.endsWith("d") || curStr.endsWith("d"))
            val onlyDotDecimal = (current.tokenString == "." && if (next != null) number.matches(next.tokenString.first().toString()) else false)
            val fullDecimal = (number.matches(curStr.last().toString()) && next?.tokenString?.first() == '.' && if (afterNext != null) number.matches(afterNext.tokenString.first().toString()) else false)
            val isDecimal = if (hasIntFloatMod || hasIntDoubleMod) 1 else if (onlyDotDecimal) 2 else if (fullDecimal) 3 else 0
            val isNegative = last?.tokenString == "-" && if (beforeLast == null) true else beforeLast.type != TokenType.GENERIC
            val n = if (isNegative) -1 else 1
            if (isDecimal != 0) {
                if (hasIntFloatMod) return OnyxFloat(curStr.dropLast(1).toFloat() * n)
                else if (hasIntDoubleMod) return OnyxDouble(curStr.dropLast(1).toDouble() * n)
                else {
                    var hasEndMod: Boolean
                    val isDouble = if (isDecimal == 2) {
                        val endChar = next?.tokenString?.last() ?: ' '
                        hasEndMod = endChar != ' '
                        i += 2
                        endChar != 'f' && endChar != 'F'
                    } else {
                        val afterStr = afterNext!!.tokenString
                        val b = afterStr.endsWith("f") || afterStr.endsWith("F")
                        i += 3
                        hasEndMod = b
                        !b
                    }
                    val str = (curStr + (next?.tokenString ?: "") + (afterNext?.tokenString ?: "")).dropLast(if (hasEndMod) 1 else 0)
                    return if (isDouble) OnyxDouble(str.toDouble() * n) else OnyxFloat(str.toFloat() * n)
                }
            } else {
                val isLong = curStr.endsWith("l") || curStr.endsWith("L")
                val isByte = curStr.endsWith("b") || curStr.endsWith("B")
                val isShort = curStr.endsWith("s") || curStr.endsWith("S")
                val str = if (isLong || isShort || isByte) curStr.dropLast(1) else curStr
                i += 1
                return if (isLong) OnyxLong(str.toLong() * n)
                else if (isShort) OnyxShort((str.toInt() * n).toShort())
                else if (isByte) OnyxByte((str.toInt() * n).toByte())
                else OnyxInt(str.toInt() * n)
            }
        }


        private val number = Regex("[0-9.]")
        private val numberWithNegative = Regex("[0-9.\\-]")
    }


}