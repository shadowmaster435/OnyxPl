package org.shadowmaster435.bytecode

import java.lang.foreign.MemorySegment

abstract class OnyxBytecodeFunctionality {
    protected val outStreamBindings: Array<(Any) -> Unit> = Array(15) {{}}

    protected abstract fun jump(address: Long)
    protected abstract fun skip(amount: Long)
    protected abstract fun read(address: Long, size: Long = 1): MemorySegment
    protected abstract fun read(address: Long): Byte

    protected abstract fun plus(a: ByteArray, b: ByteArray, size: Long)

    protected abstract fun exec(address: Long)
    protected abstract fun branch(input: MemorySegment, trueAddress: MemorySegment, falseAddress: MemorySegment)
    protected abstract fun outStream(stream: MemorySegment, size: Long, type: OutStreamType)
    open fun bindOutStream(type: OutStreamType, out: (Any) -> Unit) {
        outStreamBindings[type.arrayIndex] = out
    }

    enum class OutStreamType(internal val arrayIndex: Int) {
        BYTE(0),
        BYTE_ARRAY(1),
        SHORT(2),
        SHORT_ARRAY(3),
        INT(4),
        INT_ARRAY(5),
        LONG(6),
        LONG_ARRAY(7),
        FLOAT(8),
        FLOAT_ARRAY(9),
        DOUBLE(10),
        DOUBLE_ARRAY(11),
        BOOL(12),
        BOOL_ARRAY(13),
        STRING(14),
        MEMORY_SEGMENT(15),
    }

}