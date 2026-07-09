package org.shadowmaster435.operators.other

import org.shadowmaster435.built_ins.*
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxPolymorphicType
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.operators.mathmatical.OnyxNegate
import org.shadowmaster435.operators.mathmatical.OnyxNegate.ByPrimitiveNumber
import org.shadowmaster435.util.GenericHolder

abstract class OnyxAs(fromType: OnyxType, toType: OnyxType): OnyxBinaryOperator(fromType, toType, toType) {
    override val precedence: Int = 11
    
    class NumberCast(fromType: OnyxType, val toType: OnyxType) : OnyxAs(fromType, toType) {
        override fun evaluate(params: EvaluationParams): DataProvider {
            val v = params.getParam(0)
            return GenericHolder(when(toType) {
                OnyxBuiltinClasses.OnyxIntClass.type -> v.held as Int
                OnyxBuiltinClasses.OnyxLongClass.type -> v.held as Long
                OnyxBuiltinClasses.OnyxFloatClass.type -> v.held as Float
                OnyxBuiltinClasses.OnyxDoubleClass.type -> v.held as Double
                OnyxBuiltinClasses.OnyxShortClass.type -> v.held as Short
                OnyxBuiltinClasses.OnyxByteClass.type -> v.held as Byte
                else -> throw RuntimeException("Unknown number type")
            })
        }
    }

    override fun toString() = "as ${rightType.name}"
    companion object {
        fun create(from: DataProvider, toType: DataProvider): OnyxAs {
            return if (from.type.castableTo(numberType) && (toType.held == OnyxBuiltinClasses.OnyxTypeClass.type)) {
                NumberCast(from.type, toType.held as OnyxType)
            } else throw RuntimeException("Unknown")
        }
    }
}