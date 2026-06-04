package org.shadowmaster435.bytecode

import org.shadowmaster435.bytecode.util.LongByteArray
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout
import kotlin.text.get

class OnyxProgramMemory(byteAllocSize: Long) {
    private val mem = LongByteArray(byteAllocSize)
    private var nextFree = 0L



    fun read(address: Long, size: Long = 1) = mem[address, size]
    fun read(address: Long): Byte = mem[address, 1].get(ValueLayout.JAVA_BYTE, address)
    fun write(bytes: MemorySegment, address: Long) {
        mem[address] = bytes
    }
    fun write(bytes: ByteArray, address: Long) {
        mem[address] = bytes
    }

    fun malloc(size: Long): Long {
        val address = nextFree
        nextFree += size
        return nextFree
    }
}