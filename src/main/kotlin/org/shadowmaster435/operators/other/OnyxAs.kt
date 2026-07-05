package org.shadowmaster435.operators.other

import org.shadowmaster435.built_ins.*
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.operators.mathmatical.OnyxNegate
import org.shadowmaster435.operators.mathmatical.OnyxNegate.ByPrimitiveNumber
import org.shadowmaster435.util.GenericHolder

abstract class OnyxAs(fromType: OnyxType, toType: OnyxType): OnyxBinaryOperator(fromType, OnyxTypeClass.type, toType) {
    override val precedence: Int = 11
    
    class NumberCast(val type: OnyxType) : OnyxAs(numberType, type) {
        override fun evaluate(params: EvaluationParams): DataProvider {
            val v = params.getParam(0)
            return GenericHolder(when(type) {
                OnyxIntClass.type -> v.held as Int
                OnyxLongClass.type -> v.held as Long
                OnyxFloatClass.type -> v.held as Float
                OnyxDoubleClass.type -> v.held as Double
                OnyxShortClass.type -> v.held as Short
                OnyxByteClass.type -> v.held as Byte
                else -> throw RuntimeException("Unknown number type")
            })
        }
    }

    companion object {
        fun create(from: DataProvider, toType: DataProvider): OnyxAs {
            return if (from.type.castableTo(numberType) && (toType.held == OnyxTypeClass.type)) {
                NumberCast(toType.held as OnyxType)
            } else throw RuntimeException("Unknown")
        }
    }
}