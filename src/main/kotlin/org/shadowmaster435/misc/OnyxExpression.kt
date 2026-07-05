package org.shadowmaster435.misc

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.impl.abstracts.OnyxOperator
import org.shadowmaster435.impl.abstracts.OnyxUnaryOperator
import org.shadowmaster435.impl.enums.CodeObjType
import org.shadowmaster435.util.GenericHolder
import java.util.*

class OnyxExpression(operations: List<Pair<OnyxOperator, List<DataProvider>>>, override val type: OnyxType) : CodeObject, DataProvider, OnyxMember {
    override val objType = CodeObjType.DATA
    override var initialized = false
    override var held
        get() = evaluate().held
        set(_) {}
    @Suppress("UNCHECKED_CAST")
    inner class OpInst(
        var first: DataProvider,
        val op: OnyxOperator,
        var second: DataProvider,
        val precedence: Int
    ) : DataProvider {
        var thisInstance: DataProvider? = null
        override var initialized: Boolean = false
        override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
        override val type; get() = throw RuntimeException("Should Never Be Called")
        override var held; get() = evaluate(thisInstance); set(_) {}
        val isBinary = op is OnyxBinaryOperator
        override fun toString(): String {
            return "($first $op $second)"
        }
        fun evaluate(thisInstance: DataProvider?): Any? {
            this.thisInstance = thisInstance
            val builder = op.begin()
            builder.accept(first)
            if (isBinary) {
                builder.accept(second)
            }
            return builder.evaluate().held
        }

        fun treeInit(currentScope: HashMap<String, OnyxMember>) {
            fun tree(provider: DataProvider) {
                when(provider) {
                    is OpInst -> treeInit(currentScope)
                    is OnyxMember -> provider.initialize(currentScope)
                }
            }
            tree(first)
            tree(second)
        }

    }

    val chain = run {
        class TempOp : DataProvider {
            override var initialized: Boolean = false
            override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider): OnyxMember? {
                throw RuntimeException("This shouldn't happen")
            }
            override var held: Any?; get() = throw RuntimeException("This shouldn't happen")
                set(_) {throw RuntimeException("This shouldn't happen")}
            override val type; get() = throw RuntimeException("This shouldn't happen")
        }
        val stack = Stack<OpInst>()
        var lastOp: OpInst? = null
        var rootOp: OpInst? = null
        repeat(operations.size) {
            val pair = operations[it]
            val op = pair.first
            val next = operations.getOrNull(it + 1)
            val vals = pair.second
            val isPrecedenceChainStart = if (lastOp == null) true else lastOp.op.precedence != op.precedence
            val isPrecedenceChainEnd = if (next == null) true else next.first.precedence != op.precedence
            var skip = false
            var actualLastOp: OpInst? = null
            when(op) {
                is OnyxUnaryOperator -> {}
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


    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        chain.treeInit(namedScopeMembers)
    }

    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this

    override fun toString(): String {
        return "($chain)"
    }

    @Suppress("UNCHECKED_CAST")
    fun evaluate(thisInstance: DataProvider? = null): DataProvider {

        return GenericHolder(chain.evaluate(thisInstance))
    }
}