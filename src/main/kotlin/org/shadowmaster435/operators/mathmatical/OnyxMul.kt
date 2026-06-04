package org.shadowmaster435.operators.mathmatical

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.impl.abstracts.OnyxOperator
import org.shadowmaster435.util.GenericHolder

abstract class OnyxMul<A,B,C> : OnyxBinaryOperator<A, B, C>() {
    override val precedence = 9
    override fun toString() = "*"

    class ByPrimitiveNumber : OnyxMul<Number, Number, Number>() {

        @Suppress("UNCHECKED_CAST", "DuplicatedCode")
        override fun evaluate(params: OnyxOperator<Number>.EvaluationParams): DataProvider<Number> {
            val a = params.getParam(0)
            val b = params.getParam(1)
            fun double() = GenericHolder(a.held.toDouble() * b.held.toDouble())
            fun float() = GenericHolder(a.held.toFloat() * b.held.toFloat())
            fun long() = GenericHolder(a.held.toLong() * b.held.toLong())
            fun int() = GenericHolder(a.held.toInt() * b.held.toInt())
            fun short() = GenericHolder(a.held.toShort() * b.held.toShort())
            fun byte() = GenericHolder(a.held.toByte() * b.held.toByte())
            return when(a.held) {
                is Double -> double()
                is Float -> when(b.held) {
                    is Double -> double()
                    else -> float()
                }
                is Long -> when(b.held) {
                    is Double -> double()
                    is Float -> float()
                    else -> long()
                }
                is Int -> when(b.held) {
                    is Double -> double()
                    is Float -> float()
                    is Long -> long()
                    else -> int()
                }
                is Short -> when(b.held) {
                    is Double -> double()
                    is Float -> float()
                    is Long -> long()
                    is Int -> int()
                    else -> short()
                }
                is Byte -> when(b.held) {
                    is Double -> double()
                    is Float -> float()
                    is Long -> long()
                    is Int -> int()
                    is Short -> short()
                    else -> byte()
                }
                else -> throw RuntimeException("Unknown number class")
            } as DataProvider<Number>

        }

    }
    companion object {
        fun create(left: DataProvider<*>, right: DataProvider<*>): OnyxMul<*, *, *> {
            return if (Number::class.java.isAssignableFrom(left.typeClass) && Number::class.java.isAssignableFrom(right.typeClass)) {
                ByPrimitiveNumber()
            } else throw RuntimeException("Unknown")
        }
    }

}