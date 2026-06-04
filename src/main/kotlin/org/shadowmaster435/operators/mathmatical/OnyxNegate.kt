package org.shadowmaster435.operators.mathmatical

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.abstracts.OnyxOperator
import org.shadowmaster435.impl.abstracts.OnyxUnaryOperator
import org.shadowmaster435.util.GenericHolder

abstract class OnyxNegate<A,B> : OnyxUnaryOperator<A, B>() {
    override val precedence = 12
    override fun toString() = "(-)"
    class ByPrimitiveNumber : OnyxNegate<Number, Number>() {
        @Suppress("UNCHECKED_CAST", "DuplicatedCode")
        override fun evaluate(params: OnyxOperator<Number>.EvaluationParams): DataProvider<Number> {
            val a = params.getParam(0)
            fun double() = GenericHolder(-a.held.toDouble())
            fun float() = GenericHolder(-a.held.toFloat())
            fun long() = GenericHolder(-a.held.toLong())
            fun int() = GenericHolder(-a.held.toInt())
            fun short() = GenericHolder(-a.held.toShort())
            fun byte() = GenericHolder(-a.held.toByte())
            return when(a.held) {
                is Double -> double()
                is Float -> float()
                is Long -> long()
                is Int -> int()
                is Short -> short()
                is Byte -> byte()
                else -> throw RuntimeException("Unknown number class")
            } as DataProvider<Number>

        }
    }
    companion object {
        fun create(provider: DataProvider<*>): OnyxNegate<*, *> {
            return if (Number::class.java.isAssignableFrom(provider.typeClass)) {
                ByPrimitiveNumber()
            } else throw RuntimeException("Unknown")
        }
    }
}