package org.shadowmaster435.operators.mathmatical

import org.shadowmaster435.built_ins.numberType
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxUnaryOperator
import org.shadowmaster435.util.GenericHolder

abstract class OnyxNegate(type: OnyxType, retType: OnyxType) : OnyxUnaryOperator(type, retType) {
    override val precedence = 12
    override fun toString() = "(-)"
    class ByPrimitiveNumber : OnyxNegate(numberType, numberType) {
        @Suppress("DuplicatedCode")
        override fun evaluate(params: EvaluationParams): DataProvider {
            val a = params.getParam(0)
            fun double() = GenericHolder(-(a.held as Double))
            fun float() = GenericHolder(-(a.held as Float))
            fun long() = GenericHolder(-(a.held as Long))
            fun int() = GenericHolder(-(a.held as Int))
            fun short() = GenericHolder(-(a.held as Short))
            fun byte() = GenericHolder(-(a.held as Byte))
            return when(a.held) {
                is Double -> double()
                is Float -> float()
                is Long -> long()
                is Int -> int()
                is Short -> short()
                is Byte -> byte()
                else -> throw RuntimeException("Unknown number class")
            }
        }
    }
    companion object {
        fun create(provider: DataProvider): OnyxNegate {
            return if (provider.type.castableTo(numberType)) {
                ByPrimitiveNumber()
            } else throw RuntimeException("Unknown")
        }
    }
}