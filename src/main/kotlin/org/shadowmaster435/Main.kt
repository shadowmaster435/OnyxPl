package org.shadowmaster435

import org.shadowmaster435.built_ins.OnyxIntClass
import org.shadowmaster435.built_ins.OnyxNumberClass
import org.shadowmaster435.built_ins.numberType
import org.shadowmaster435.lexer.Lexer
import org.shadowmaster435.lexer.LexerKeypointParser
import org.shadowmaster435.tokenizer.Tokenizer
import org.shadowmaster435.types.LLVMIntType
import org.shadowmaster435.util.LLVMModule

fun main() {
//    testExpressions()
    println(OnyxNumberClass("Number", listOf(), type = numberType).toString())
}

fun testModifiers() {
    val tokens = Tokenizer.tokenize(modifierTest)
    print(Lexer.tryLexModifiers(tokens))
}

val b = 1 +
        1
fun fieldTesting() {
    val a = "val a: Int = 2 + 5 + (4 * (1 - 4))"
    val tokens = Tokenizer.tokenize(a)
    val keypoints = LexerKeypointParser.parse(tokens)
    val v = Lexer.lexField(tokens, 0, keypoints)
    println(v)
    println(v.held)
}


fun funcTesting() {
    val str = """
        fun test(a: Int, b: Int): Int {
            return a + b
        }
    """.trimMargin()
}

fun testExpressions() {
    val c = "1 + 1 * 2 * 2"
    val a = "2 * 2 * 8 / 3 % 2 * 5 * 5 + 1 * 2 - 4 - 15 * 4 - 34 * 4"
    val b = "4 + 2 + 2 * 12 * 3 + 13 + 14 * 3 * 65"

    val d = "4 * -2 + 8 * 8 * 2 + 21 * 4 + 4 + -4 * 44 * 1 + 45 + 1"
    val e = "2 + ((1 - 4) * 4)" // make work
    val f = "2 + (4 * (1 - 4))"
    val tokens = Tokenizer.tokenize(a)
    val keypoints = LexerKeypointParser.parse(tokens)
    val adf = OnyxIntClass.type

    val expr = Lexer.lexExpression(tokens, keypoints, OnyxIntClass.type)
    println(expr)
    println(expr.held)
    println(2 * 2 * 8 / 3 % 2 * 5 * 5 + 1 * 2 - 4 - 15 * 4 - 34 * 4)

}

fun onyxTesting() {



//

    val tokens = Tokenizer.tokenize(sampleClass1)
    val keypoints = LexerKeypointParser.parse(tokens)
    keypoints.forEach {
        println(it)
    }
//    tokens.forEach {
//        println(it.type)
//    }

}
val modifierTest = """
   public static final
"""
val sampleClass1 = """
    private abstract class Print(val output: String) {
        fun print() {
            out(output)
        }
    }
    private abstract class Print2(val output: String) {
        fun print() {
            out(output)
        }
    }
"""




fun llvmTesting() {
    val module = LLVMModule(LLVMIntType, false) {
        val test by 1
        val test2 by 16
        test shl const(4)
        test + test2
        test
    }
    println(module.execute())
}
//val h = 'h'.code.toByte()
//val e = 'e'.code.toByte()
//val l = 'l'.code.toByte()
//val o = 'o'.code.toByte()
//val space = ' '.code.toByte()
//val w = 'w'.code.toByte()
//val r = 'r'.code.toByte()
//val d = 'd'.code.toByte()
//4 * 1 + 4 * 4 + 2 * 4 + 4 / 4 * 4
//    4 + 16    + 8     + 4
//    println(tokens)
//    val testProgram = byteArrayOf(
//        // a program that repeats hello world forever
//        0, 0, 0, 0, 17, 0,// skip out data
//        h, e, l, l, o, space, w, o, r, l, d, // out data
//        2, 0, 0, 0, 6, 1, 0, 0, 0, 11, 0, //invoke out stream
//        0, 0, 0, 0, 17, 0,// jump to invoke
//    )
//    //println(testProgram.contentToString())
//    val helloWorld = OnyxAsmParser.parse(
//        """
//        skip i17
//        mark "string"
//        '$h,'$e,'$l,'$l,'$o,'$space,'$w,'$o,'$r,'$l,'$d
//        sout 6, b, bytearray
//        jump "string"
//        """
//    )
//    val a = ByteBuffer.allocate(4)
//    val b = ByteBuffer.wrap(byteArrayOf())
//    println(8 % 8)
//  println(helloWorld.contentToString())
// program.run()
//    val s = "/home/shadowmaste435/Desktop/New Folder 1/helloworld.onx"
//    val f = File(s)
//    f.createNewFile()
//    f.writeBytes(testProgram2)
//
//    val byteBuf = ByteBuffer.allocate(8)
//    byteBuf.getInt()

val s3 = "fun test(a: Test) {" +
        "} "
val s2 = "    private fun test() {\n" +
        "        val stack = Stack<LexerScope>()\n" +
        "        stack.push(egg)\n" +
        "        stack\n" +
        "    }"
val s = "fun tokenize(string: String): List<Token> {\n" +
        "        return buildList {\n" +
        "            val iter = IntIterHolder()\n" +
        "            var currentGeneric = \"\"\n" +
        "            var scopeLevel = 0\n" +
        "            while (iter.i < string.length) {\n" +
        "                var shouldContinue = false\n" +
        "                constantTokenizers.forEach { scanner ->\n" +
        "                    val gap = wordHasGap(iter.i, string, scanner.tokenString)\n" +
        "                    if (gap) {\n" +
        "                        add(Token(scanner.type, scanner.tokenString, scopeLevel))\n" +
        "                        if (scanner.type == TokenType.GROUP_START) scopeLevel += 1\n" +
        "                        if (scanner.type == TokenType.GROUP_END) scopeLevel -= 1\n" +
        "                        shouldContinue = true\n" +
        "                    }\n" +
        "                }\n" +
        "                if (shouldContinue) {\n" +
        "                    currentGeneric = \"\"\n" +
        "                    continue\n" +
        "                }\n" +
        "                if (isGapChar(string[iter.i])) {\n" +
        "                    add(Token(TokenType.GENERIC, currentGeneric, scopeLevel))\n" +
        "                    currentGeneric = \"\"\n" +
        "                } else currentGeneric += string[iter.i].toString()\n" +
        "\n" +
        "                iter.inc()\n" +
        "            }\n" +
        "            if (currentGeneric.isNotEmpty()) {\n" +
        "                add(Token(TokenType.GENERIC, currentGeneric, scopeLevel))\n" +
        "            }\n" +
        "        }\n" +
        "    }"