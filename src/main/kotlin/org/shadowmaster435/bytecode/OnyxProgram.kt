package org.shadowmaster435.bytecode

import org.shadowmaster435.bytecode.util.LongByteArray
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout
import kotlin.experimental.and
import kotlin.math.min

class OnyxProgram private constructor(val program: LongByteArray, byteAllocSize: Long) : OnyxBytecodeFunctionality() {
    private var currentAddress = 0L
    private var instructionByteCount = 1
    private val mem = OnyxProgramMemory(byteAllocSize)

    constructor(program: ByteArray, byteAllocSize: Long): this(LongByteArray(program), byteAllocSize)

    fun run() {
        while (currentAddress < program.size) {
            exec(currentAddress)
            if (currentAddress > program.size) break
        }
    }

    override fun jump(address: Long) {

        currentAddress = address
    }

    override fun skip(amount: Long) {
        currentAddress += amount
    }



    fun readProgramWithTypeSize(address: Long, sizeFromMem: Boolean): Pair<MemorySegment, Int> {
        val type = ((if (sizeFromMem) read(address) else readProgram(address)).toInt() shr 3) and 0b111
        val size = when(type) {
            6, 7 -> 1
            5 -> 2
            0, 1, 3 -> 4
            2, 4 -> 8
            else -> throw RuntimeException("Unknown type, this should never happen")
        }
        return readProgram(address, size.toLong()) to size
    }
    fun readWithTypeSize(address: Long, sizeFromMem: Boolean): Pair<MemorySegment, Int> {
        val type = ((if (sizeFromMem) read(address) else readProgram(address)).toInt() shr 3) and 0b111
        val size = when(type) {
            6, 7 -> 1
            5 -> 2
            0, 1, 3 -> 4
            2, 4 -> 8
            else -> throw RuntimeException("Unknown type, this should never happen")
        }
        return read(address, size.toLong()) to size
    }
    override fun read(address: Long, size: Long): MemorySegment = mem.read(address, size)
    override fun read(address: Long) = mem.read(address)
    override fun plus(a: ByteArray, b: ByteArray, size: Long) {
        TODO("Not yet implemented")
    }

    fun readProgram(address: Long) = program[address]
    fun readProgram(address: Long, size: Long) = program[address, size]


    private fun getActualAddress(addressPlusFlags: MemorySegment): MemorySegment {
        val isProgramPointer = (addressPlusFlags.getAtIndex(ValueLayout.JAVA_BYTE, 0) and 1) == 0.toByte()
        return if (isProgramPointer) addressPlusFlags else read(addressPlusFlags.getAtIndex(ValueLayout.JAVA_LONG_UNALIGNED, 1), 9)
    }

    private fun getActualRead(addressPlusFlags: MemorySegment, size: Long): MemorySegment {

        val isProgramPointer = (addressPlusFlags.getAtIndex(ValueLayout.JAVA_BYTE, 0) and 1) == 0.toByte()
        val actualAddress = getActualAddress(addressPlusFlags)
        return if (isProgramPointer) readProgram(actualAddress.getAtIndex(ValueLayout.JAVA_LONG, 1), size) else read(actualAddress.getAtIndex(ValueLayout.JAVA_LONG_UNALIGNED, 1), size)
    }
    override fun exec(address: Long) {
        val instructionAddress = program[address].toInt()

        when(instructionAddress) {
            0 -> jump(getActualAddress(readProgram(address + instructionByteCount, 9)).getAtIndex(ValueLayout.JAVA_LONG_UNALIGNED, 1))
            1 -> branch(
                getActualAddress(readProgram(address + instructionByteCount, 5)),
                getActualAddress(readProgram(address + instructionByteCount + 5, 5)),
                getActualAddress(readProgram(address + instructionByteCount + 10, 5))
            )
            2 -> {
                val readAddress = getActualAddress(readProgram(address + instructionByteCount, 5))
                val size = getActualAddress(readProgram(address + instructionByteCount + 5, 5)).getAtIndex(ValueLayout.JAVA_LONG_UNALIGNED, 1)
                val type = getActualAddress(readProgram(address + instructionByteCount + 5, 5))
                val bytes = getActualRead(readAddress, size)
                currentAddress += instructionByteCount + 10
            }
            3 -> skip(getActualAddress(readProgram(address + instructionByteCount, 5)).getAtIndex(ValueLayout.JAVA_LONG_UNALIGNED, 1) + 7)
            else -> currentAddress++
        }
    }

    override fun branch(input: MemorySegment, trueAddress: MemorySegment, falseAddress: MemorySegment) {
        currentAddress = if (input.getAtIndex(ValueLayout.JAVA_BYTE, 1) and 1 == 1.toByte()) trueAddress.getAtIndex(
            ValueLayout.JAVA_LONG, 0) else falseAddress.getAtIndex(
            ValueLayout.JAVA_LONG, 0)
    }


    override fun outStream(stream: MemorySegment, size: Long, type: OutStreamType) {

        val out = when(type) {
            OutStreamType.BYTE -> stream.getAtIndex(ValueLayout.JAVA_BYTE, 0)
            OutStreamType.BYTE_ARRAY -> (if (size == stream.byteSize()) stream
            else stream.asSlice(0, size)).toArray(ValueLayout.JAVA_BYTE)
            OutStreamType.SHORT -> stream.short()
            OutStreamType.SHORT_ARRAY -> stream.shortArray(size.toInt())
            OutStreamType.INT -> stream.int()
            OutStreamType.INT_ARRAY -> stream.intArray(size.toInt())
            OutStreamType.LONG -> stream.long()
            OutStreamType.LONG_ARRAY -> stream.longArray(size.toInt())
            OutStreamType.FLOAT -> stream.float()
            OutStreamType.FLOAT_ARRAY -> stream.floatArray(size.toInt())
            OutStreamType.DOUBLE -> stream.double()
            OutStreamType.DOUBLE_ARRAY -> stream.doubleArray(size.toInt())
            OutStreamType.BOOL -> stream.boolean()
            OutStreamType.BOOL_ARRAY -> stream.booleanArray(size.toInt())
            OutStreamType.STRING -> String(stream.toArray(ValueLayout.JAVA_BYTE))
            OutStreamType.MEMORY_SEGMENT -> if (size == stream.byteSize()) stream else stream.asSlice(0, size)
        }
        outStreamBindings[type.arrayIndex].invoke(out)
    }


    companion object {
        fun ByteArray.int(index: Int = 0): Int {
            var i = min(size, index + 2)
            var result = 0
            while (i > index) {
                result = result or this[i].toInt()
                i--
                if (i > index) result = result shl 8
            }
            return result
        }
        fun ByteArray.intArray(size: Int = this.size): IntArray {
            val array = IntArray(size / 4) {0}
            repeat(size / 4) {
                array[it] = int(it * 4)
            }
            return array
        }
        fun MemorySegment.short(index: Long = 0): Short {

            return this.getAtIndex(ValueLayout.JAVA_SHORT, index)
        }
        fun MemorySegment.int(index: Long = 0): Int {

            return this.getAtIndex(ValueLayout.JAVA_INT, index)
        }
        fun MemorySegment.long(index: Long = 0): Long {

            return this.getAtIndex(ValueLayout.JAVA_LONG, index)
        }
        fun MemorySegment.float(index: Long = 0) = Float.fromBits(int(index))
        fun MemorySegment.double(index: Long = 0) = Double.fromBits(long(index))
        fun MemorySegment.boolean(bitIndex: Long = 0) = (getAtIndex(ValueLayout.JAVA_BYTE, bitIndex / 8L).toInt() shr (bitIndex % 8).toInt()) == 1


        fun MemorySegment.shortArray(size: Int = byteSize().toInt()): ShortArray {
            return asSlice(size.toLong()).toArray(ValueLayout.JAVA_SHORT)
        }
        fun MemorySegment.intArray(size: Int = byteSize().toInt()): IntArray {
            return asSlice(size.toLong()).toArray(ValueLayout.JAVA_INT)
        }
        fun MemorySegment.longArray(size: Int = byteSize().toInt()): LongArray {
            return asSlice(size.toLong()).toArray(ValueLayout.JAVA_LONG)
        }
        fun MemorySegment.floatArray(size: Int = byteSize().toInt()): FloatArray {
            return asSlice(size.toLong()).toArray(ValueLayout.JAVA_FLOAT)

        }
        fun MemorySegment.doubleArray(size: Int = byteSize().toInt()): DoubleArray {
            return asSlice(size.toLong()).toArray(ValueLayout.JAVA_DOUBLE)

        }
        fun MemorySegment.booleanArray(bitCount: Int = byteSize().toInt() * 8): BooleanArray {
            val array = BooleanArray(bitCount) {false}
            repeat(bitCount) {
                array[it] = boolean(it.toLong())
            }
            return array
        }

        fun Int.bytes() = byteArrayOf((this shr 24).toByte(), ((this shr 16) and 0b11111111).toByte(), ((this shr 8) and 0b11111111).toByte(), (this and 0b11111111).toByte())
        fun ByteArray.byte() = this[0]

    }

}