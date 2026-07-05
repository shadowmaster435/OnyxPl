package org.shadowmaster435.operators.mathmatical

import org.shadowmaster435.built_ins.numberType
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.util.GenericHolder

abstract class OnyxSub(leftType: OnyxType, rightType: OnyxType, retType: OnyxType) : OnyxBinaryOperator(leftType, rightType, retType) {
    override val precedence = 8
    override fun toString() = "-"

    class ByPrimitiveNumber : OnyxSub(numberType, numberType, numberType) {

        @Suppress("DuplicatedCode")
        override fun evaluate(params: EvaluationParams): DataProvider {
            val a = params.getParam(0)
            val b = params.getParam(1)
            fun double() = GenericHolder((a.held as Double) - (b.held as Double))
            fun float() = GenericHolder((a.held as Float) - (b.held as Float))
            fun long() = GenericHolder((a.held as Long) - (b.held as Long))
            fun int() = GenericHolder((a.held as Int) - (b.held as Int))
            fun short() = GenericHolder((a.held as Short) - (b.held as Short))
            fun byte() = GenericHolder((a.held as Byte) - (b.held as Byte))
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
            }
        }
    }
    companion object {
        fun create(left: DataProvider, right: DataProvider): OnyxSub {
            return if (left.type.castableTo(numberType) && right.type.castableTo(numberType)) {
                ByPrimitiveNumber()
            } else throw RuntimeException("Unknown")
        }
    }

}