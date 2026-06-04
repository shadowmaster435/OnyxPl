package org.shadowmaster435.misc

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.LLVMMutableValue
import org.shadowmaster435.impl.LLVMType
import org.shadowmaster435.impl.LLVMValue
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.impl.abstracts.OnyxOperator
import org.shadowmaster435.impl.abstracts.OnyxUnaryOperator
import org.shadowmaster435.impl.enums.CodeObjType
import org.shadowmaster435.types.LLVMDoubleType
import org.shadowmaster435.types.LLVMFloatType
import org.shadowmaster435.types.LLVMIntType
import org.shadowmaster435.types.LLVMLongType
import org.shadowmaster435.util.GenericHolder
import org.shadowmaster435.util.LLVMBuilder
import java.util.EmptyStackException
import java.util.Stack

class OnyxExpression<T>(operations: List<Pair<OnyxOperator<*>, List<DataProvider<*>>>>, override val typeClass: Class<T>) : CodeObject<T>, DataProvider<T> {
    override val objType = CodeObjType.DATA
    override var held: T
        get() = evaluate().held
        set(_) {}
    @Suppress("UNCHECKED_CAST")
    inner class OpInst<T>(
        var first: DataProvider<*>,
        val op: OnyxOperator<T>,
        var second: DataProvider<*>,
        val precedence: Int
        ) : DataProvider<T> {
        override val typeClass; get() = throw RuntimeException("Should Never Be Called")
        override var held; get() = evaluate(); set(_) {}
        val isBinary = op is OnyxBinaryOperator<*,*,*>
        override fun toString(): String {


            return "($first $op $second)"
        }
        fun evaluate(): T {
            val builder = op.begin()
            builder.accept(first)
            if (isBinary) {
                builder.accept(second)
            }
            return builder.evaluate().held
        }
    }

    val chain = run {
        class TempOp: DataProvider<Any> {
            override var held: Any; get() = throw RuntimeException("This shouldn't happen")
                set(_) {throw RuntimeException("This shouldn't happen")}
            override val typeClass: Class<*>; get() = throw RuntimeException("This shouldn't happen")
        }
        val stack = Stack<OpInst<*>>()
        var lastOp: OpInst<*>? = null
        var rootOp: OpInst<*>? = null
        repeat(operations.size) {
            val pair = operations[it]
            val op = pair.first
            val next = operations.getOrNull(it + 1)
            val vals = pair.second
            val isPrecedenceChainStart = if (lastOp == null) true else lastOp.op.precedence != op.precedence
            val isPrecedenceChainEnd = if (next == null) true else next.first.precedence != op.precedence
            var skip = false
            var actualLastOp: OpInst<*>? = null
            when(op) {
                is OnyxUnaryOperator<*,*> -> {}
                else -> {
                    val inst = OpInst(
                        if (isPrecedenceChainStart)
                            vals.first()
                        else
                            TempOp(), op, vals.last(), op.precedence
                    )

                    if (isPrecedenceChainStart) {

                        if (rootOp == null) rootOp = inst

                        if (lastOp != null && !stack.isEmpty()) {
                            if (lastOp.precedence < inst.precedence) {
                                if (rootOp !== lastOp) {
                                    lastOp.second = inst
                                    stack.push(lastOp)
                                } else {
                                    rootOp.second = inst
                                }


                            }

                        } else {
                            stack.push(inst)
                        }
                    }

                    if (lastOp != null) {
                        if (isPrecedenceChainStart)
                            if (inst.precedence > lastOp.precedence)
                                lastOp.second = inst
                            else {
                                skip = true
                                inst.first = lastOp
                            }
                        else
                            inst.first = lastOp

                        if (isPrecedenceChainEnd) {
                            if (lastOp.precedence > inst.precedence) {
                                inst.first = lastOp
                            } else if (lastOp.precedence == inst.precedence && lastOp !== stack.peek()) {

                                if (stack.peek().second === lastOp) {
                                    stack.peek().second = inst
                                    skip = true
                                }
                            }
                        }
                    }
                    actualLastOp = lastOp
                    lastOp = inst
                }
            }
            if (isPrecedenceChainEnd) {
                if (lastOp!!.precedence == rootOp!!.precedence ) {
                    if (stack.peek() !== rootOp)
                        rootOp.second = lastOp
                    else {

                        rootOp = lastOp
                    }
                } else {
                    if (lastOp.precedence < rootOp.precedence) {
                        lastOp.first = rootOp
                        rootOp = lastOp
                    }

                    if (lastOp.precedence > rootOp.precedence) {
                        if (!skip) rootOp.second = if (stack.peek() !== lastOp.first) stack.peek()
                        else lastOp
                    }

                    try {
                        // something funky going on here
                        // if you move the pop into the if statement the expression calculates incorrectly
                        // but if you don't pop even if the stack is empty(?) it also fails
                        // so the try block fixes this error
                        val popped = stack.pop()
                        if (stack.isNotEmpty()) lastOp = popped
                    } catch (_: EmptyStackException) {}
                }
            }
        }
        rootOp!!
    }


    @Suppress("UNCHECKED_CAST")
    fun evaluate(): DataProvider<T> {
        println(chain)
        return GenericHolder(chain.evaluate()) as DataProvider<T>
    }
}