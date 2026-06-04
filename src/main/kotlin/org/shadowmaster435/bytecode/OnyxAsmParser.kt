package org.shadowmaster435.bytecode

import org.shadowmaster435.arena
import org.shadowmaster435.gen.LLVM
import org.shadowmaster435.gen.LLVM.LLVMAppendBasicBlock
import org.shadowmaster435.types.LLVMIntType
import org.shadowmaster435.types.LLVMVoidType
import org.shadowmaster435.util.LLVMModule
import java.lang.foreign.MemorySegment
import kotlin.collections.plus
import kotlin.math.min
import kotlin.reflect.KClass

object OnyxAsmParser {

    private fun Boolean.int() = if (this) 1 else 0

    private val flagChars = charArrayOf('*', ';', ':', 'i', 'l', '?', 's', '\'')
    private class Value<T: Number>(val bytes: ByteArray, val size: Int, val valBytes: ByteArray, val typeClass: KClass<*>) {
        val intSafeBytes = byteArrayOf(((bytes[0].toInt() and 0b111) or 0b001000).toByte()) + valBytes.int().bytes()
        val longSafeBytes = byteArrayOf(((bytes[0].toInt() and 0b111) or 0b001000).toByte()) + valBytes.int().bytes()
        constructor(value: T, isPointer: Boolean, isAddress: Boolean) : this(
            fun(): ByteArray {
                var isFloatingPoint = false
                val bytes = when(value) {
                    is Int -> value.bytes()
                    is Long -> value.bytes()
                    is Short -> value.bytes()
                    is Byte -> byteArrayOf(value)
                    is Float -> {
                        isFloatingPoint = true
                        value.toRawBits().bytes()
                    }
                    is Double -> {
                        isFloatingPoint = true
                        value.toRawBits().bytes()
                    }
                    else -> throw RuntimeException("Unknown Number Type")
                }
                val fl = ((((bytes.size + 1 shr 3) and isAddress.int()) shr 1) and isFloatingPoint.int()) and isPointer.int()
                return byteArrayOf(fl.toByte()) + bytes
            }.invoke(),
            when(value) {
                is Int, Float -> 4
                is Long, Double -> 8
                is Short -> 2
                is Byte, Boolean -> 1
                else -> throw RuntimeException("Unknown Number Type")
            },
            when(value) {
                is Int -> value.bytes()
                is Long -> value.bytes()
                is Short -> value.bytes()
                is Byte -> byteArrayOf(value)
                is Float -> value.toRawBits().bytes()
                is Double -> value.toRawBits().bytes()
                else -> throw RuntimeException("Unknown Number Type")
            },
            value::class
        )
    }

    @Suppress("SpellCheckingInspection")
    private val outStreamValues = arrayOf(
        "byte",
        "bytearray",
        "short",
        "shortarray",
        "int",
        "intarray",
        "long",
        "longarray",
        "float",
        "floatarray",
        "double",
        "doublearray",
        "bool",
        "boolarray",
        "string",
    )

    private fun tryParseValue(string: String, start: Int, isOutStream: Boolean = false, markMap: Map<String, Pair<Int, Int>> = mapOf()): Value<*> {
        if (markMap.isNotEmpty() && string.substring(start)[0] == '"') {
            val str = tryParseString(string, start)
            val markLen = markMap[str] ?: throw RuntimeException("Unknown mark $str")
            return Value<Int>(byteArrayOf(), markLen.second, byteArrayOf(), String::class)
        }
        if (isOutStream) {
            val str = string.substring(start)
            val index = outStreamValues.indexOf(str)
            if (index == -1) throw RuntimeException("Unknown out stream type $str")
            return Value(index.toByte(), false, isAddress = false)
        }
        var isMemoryPointer = false
        var type = 0
        var i1 = start
        while (i1 < string.length && flagChars.contains(string[i1])) {
            val ch = string[i1]
            when(ch) {
                '*' -> isMemoryPointer = true
                'i' -> type = 1
                'l' -> type = 2
                ';' -> type = 3
                ':' -> type = 4
                's' -> type = 5
                '?' -> type = 6
                '\'' -> type = 7
                else -> {}
            }
            i1++
        }
        var str = ""
        var i = i1
        var v: Number
        while (i < string.length && string[i] != '\n') {
            if (string[i] == ',' || string[i] == '\n') break
            str += string[i]
            i += 1
        }
        when(type) {
            0 -> {
                if (str.length >= 8) throw RuntimeException("Address too long: $str")
                v = if (str.length < 4) str.uppercase().hexToInt() else str.uppercase().hexToLong()
            }
            1 -> v = str.toInt()
            2 -> v = str.toLong()
            3 -> v = str.toFloat()
            4 -> v = str.toDouble()
            5 -> v = str.toShort()
            6, 7 -> v = str.toByte()
            else -> throw RuntimeException("Unknown type")
        }
        return Value(v, isMemoryPointer, type == 0)
    }


    private fun tryParseString(string: String, start: Int): String {
        var str = ""
        var i = start + 1
        var literalEscape = false
        while (string[i] != '\n' && if (literalEscape) true else string[i] != '\"') {
            if (string[i] == '\\' && !literalEscape) {
                literalEscape = true
                continue
            }
            val ch = string[i]
            str += if (literalEscape) {
                when (ch) {
                    'n' -> "\n"
                    't' -> "\t"
                    else -> ch.toString()
                }
            } else ch.toString()
            if (literalEscape) {
                literalEscape = false
            }
            i++
            if (i >= string.length) break
        }
        return str
    }

    @Suppress("SpellCheckingInspection")
    private fun parseLineValues(string: String, markMap: Map<String, Pair<Int, Int>> = mapOf()): List<Value<*>> {
        val indexes = mutableListOf<Int>()
        var isOutStream = false
        if (instructionMap.containsKey(string.substring(0..<min(string.length, 4)))) {
            indexes.add(4)
            isOutStream = string.substring(0..<min(string.length, 4)) == "sout"
        } else indexes.add(0)
        if (string.last() == ',') throw RuntimeException("Trailing comma found in $string")
        string.forEachIndexed { index, ch ->
            if (ch == ',') {
                indexes.add(index + 1)
            }
        }
        return buildList {
            for (i in indexes) {
                add(tryParseValue(string, i, i == indexes.last() && isOutStream, markMap))
            }
        }
    }

    @Suppress("SpellCheckingInspection")
    private fun parseProgramMarks(programString: String): String {
        var offset = 0
        val markMap = hashMapOf<String, Pair<Int, Int>>()
        var finalStr = ""
        var lastWasMark = false
        var lastMark: String? = null

        for (it in programString.lines().filter { it.isNotEmpty() }) {
            if (it.isNotEmpty() && it[0] != '\n') {
                val str = it.substring(0..<min(it.length, 4))
                val isInstr = instructionMap.containsKey(str)

                val byteCount = if (str == "mark" || str == "dmrk") run { lastWasMark = true; 0 } else run {
                    val values = parseLineValues(it, markMap)
                    var curSize = if (isInstr) 2 else 0
                    values.forEach {curSize += it.size }
                    curSize
                }
                if (lastWasMark && lastMark != null){
                    markMap[lastMark] = Pair(markMap[lastMark]!!.first, byteCount)
                    lastMark = null
                    lastWasMark = false
                }
                if (str != "mark" && str != "dmrk") finalStr += run {
                    var finalStr = it
                    markMap.forEach { (string, i) -> finalStr = finalStr.replace("\"$string\"", "i${i.second}")}
                    finalStr + "\n"
                }
                if (!isInstr) continue
                if (str.isEmpty()) continue
                val markString = tryParseString(it, min(str.length, 4))
                if (str == "mark") {
                    if (markMap.containsKey(markString)) throw RuntimeException("Mark $markString already exists")
                    else {
                        markMap[markString] = Pair(offset, byteCount)
                        lastMark = markString
                    }
                }
                if (str == "dmrk") markMap.remove(markString)
                offset += byteCount
            }
        }
        return finalStr
    }

    fun parseScript(programString: String): ByteArray {
        val programString = programString.replace(" ", "").lowercase()


        return scriptBytecodeParse(parseProgramMarks(programString))
    }
    @Suppress("SpellCheckingInspection")
    private val instructionMap = mapOf(
        Pair("jump", 0.toByte()),
        Pair("brch", 1.toByte()),
        Pair("sout", 2.toByte()),
        Pair("skip", 3.toByte()),
        Pair("aloc", 4.toByte()),
        Pair("rdin", 5.toByte()),
        Pair("invk", 6.toByte()),
        Pair("incr", 7.toByte()),
        Pair("decr", 8.toByte()),
        Pair("mult", 9.toByte()),
        Pair("divd", 10.toByte()),
        Pair("modu", 11.toByte()),
        Pair("mark", (-1).toByte()),
    )


    private fun llvmParse(programString: String): LLVMModule<*> {
        val module = LLVMModule(LLVMIntType, false) {
            val currentAddress by 0L
            val constDefs = hashMapOf<LongRange, ByteArray>()
            val addresses = hashMapOf<Int, ByteArray>()
            val jumps = hashMapOf<Int, MemorySegment>()
            var currentAddrOfs = 0
            programString.lines().filter { it.isNotEmpty() }.forEach {
                if (it.isNotEmpty() && it[0] != '\n') {
                    val values = parseLineValues(it)
                    if (it.length > 4 && instructionMap.containsKey(it.substring(0..3))) {
                        val instruction = instructionMap[it.substring(0..3)]!!
                        val type: MemorySegment = LLVM.LLVMFunctionType(LLVMVoidType.llvmType, MemorySegment.NULL, 0, 0)
                        val func: MemorySegment = LLVM.LLVMAddFunction(module, arena.allocateFrom("main"), type)
                        val entry = LLVMAppendBasicBlock(func, arena.allocateFrom("entry"))
                        when(instruction.toInt()) {
//                            0 -> {
//                                LLVM.LLVMBuildBr(4)
//                            }
//                            1 -> {
//                            }
//                            2 -> {
//                            }
//                            3 -> {
//                            }
//                            4 -> {
//                            }
//                            0, 3 -> {
//                                by
//                            }
//                            4 -> {
//                                program += values[0].intSafeBytes
//                                program += values[1].intSafeBytes
//                            }
//                            2 -> {
//                                program += values[0].intSafeBytes
//                                program += values[1].intSafeBytes
//                                program += values[2].bytes
//                            }
//                            1 -> {
//                                program += values[0].intSafeBytes
//                                program += values[1].intSafeBytes
//                                program += values[2].intSafeBytes
//                            }
                        }
                    } else {
                        var bytes = byteArrayOf()
                        values.forEach { v ->
                            bytes += v.valBytes
                        }
                        addresses[currentAddrOfs] = bytes
                        currentAddrOfs += bytes.size
                    }
                }
            }
            const(0)
        }
        return module
    }


    private fun scriptBytecodeParse(programString: String): ByteArray {
        var program = byteArrayOf()

        programString.lines().filter { it.isNotEmpty() }.forEach {
            if (it.isNotEmpty() && it[0] != '\n') {
                val values = parseLineValues(it)
                if (it.length > 4 && instructionMap.containsKey(it.substring(0..3))) {
                    val instruction = instructionMap[it.substring(0..3)]!!
                    program += byteArrayOf(instruction)
                    when(instruction.toInt()) {
                        0, 3 -> {
                            program += values[0].intSafeBytes
                        }
                        4 -> {
                            program += values[0].intSafeBytes
                            program += values[1].intSafeBytes
                        }
                        2 -> {
                            program += values[0].intSafeBytes
                            program += values[1].intSafeBytes
                            program += values[2].bytes
                        }
                        1 -> {
                            program += values[0].intSafeBytes
                            program += values[1].intSafeBytes
                            program += values[2].intSafeBytes
                        }
                    }
                } else {
                    values.forEach { v ->
                        program += v.valBytes
                    }
                }
            }
        }
        return program
    }

    fun ByteArray.int(index: Int = 0): Int {
        var i = 0
        var result = 0
        while (i < min(size, 4) ) {
            if (i <  min(size, 4)) result = result or this[index + i].toInt()
            if (i < min(size, 3)) result = result shl 8
            i++
        }
        return result
    }


    fun Int.bytes(): ByteArray {
        var v = this
        var result = byteArrayOf()
        while (result.size < 4) {
            result += (v and 0xff).toByte()
            v = v shr 8
        }
        return result.reversedArray()
    }
    fun Short.bytes(): ByteArray {
        var v = this.toInt()
        var result = byteArrayOf()
        while (result.size < 2) {
            result += (v and 0xff).toByte()
            v = v shr 8
        }
        return result.reversedArray()
    }
    fun Long.bytes(): ByteArray {
        var v = this
        var result = byteArrayOf()
        while (result.size < 8) {
            result += (v and 0xff).toByte()
            v = v shr 8
        }
        return result.reversedArray()
    }
}