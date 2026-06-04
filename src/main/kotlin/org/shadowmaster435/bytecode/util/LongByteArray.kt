package org.shadowmaster435.bytecode.util

import java.lang.foreign.AddressLayout
import java.lang.foreign.Arena
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout
import kotlin.math.max


class LongByteArray(val size: Long): Iterable<Byte> {
    private val arena = Arena.ofConfined()
    private val segment = arena.allocate(size)

    constructor(from: ByteArray) : this(max(from.size.toLong(), 8)) {
        set(0L, from)
    }

    override operator fun iterator(): ByteIterator {
        return object : ByteIterator() {
            var idx = 0L
            override fun nextByte(): Byte {
                if (!hasNext()) throw NoSuchElementException()
                return segment.getAtIndex(ValueLayout.JAVA_BYTE, idx++)
            }
            override fun hasNext() = idx < segment.byteSize()
        }
    }

    fun close() {
        arena.close()
    }


    operator fun set(index: Long, segment: MemorySegment) {
        segment.setAtIndex(AddressLayout.ADDRESS, index, segment)
    }

    operator fun set(index: Long, bytes: ByteArray) {
        segment.setAtIndex(AddressLayout.ADDRESS, index, arena.allocateFrom(ValueLayout.JAVA_BYTE, *bytes))
    }
    operator fun set(index: Long, byte: Byte) {
        segment.setAtIndex(ValueLayout.JAVA_BYTE, index, byte)
    }
    operator fun get(index: Long): Byte {
        return segment.getAtIndex(ValueLayout.JAVA_BYTE, index)
    }
    operator fun get(index: LongRange): MemorySegment {
        return segment.asSlice(index.first, index.last - index.first)
    }
    operator fun get(start: Long, size: Long): MemorySegment {
        return segment.asSlice(start, size)
    }
}