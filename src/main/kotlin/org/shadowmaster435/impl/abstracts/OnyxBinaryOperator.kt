package org.shadowmaster435.impl.abstracts

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.LLVMConstableValue
import org.shadowmaster435.impl.LLVMType
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.types.LLVMTypes
import org.shadowmaster435.util.LLVMBuilder

abstract class OnyxBinaryOperator(val leftType: OnyxType, val rightType: OnyxType, retType: OnyxType) : OnyxOperator(retType) {

    
    companion object {
        fun getLLVMNumberType(evaluationParams: EvaluationParams): LLVMType<*> {
            return LLVMTypes.typeOf(getNumberType(evaluationParams))
        }

        fun getNumberType(evaluationParams: EvaluationParams): Class<*> {
            val a = evaluationParams.getParam(0)
            val b = evaluationParams.getParam(1)
            return when(a.held) {
                is Double -> Double::class.java
                is Float -> when(b.held) {
                    is Double -> Double::class.java
                    else -> Float::class.java
                }

                is Long -> when(b.held) {
                    is Double -> Double::class.java
                    is Float -> Float::class.java
                    else -> Long::class.java
                }

                is Int -> when(b.held) {
                    is Double -> Double::class.java
                    is Float -> Float::class.java
                    is Long -> Long::class.java
                    else -> Int::class.java
                }

                is Short -> when(b.held) {
                    is Double -> Double::class.java
                    is Float -> Float::class.java
                    is Long -> Long::class.java
                    is Int -> Int::class.java
                    else -> Short::class.java
                }

                is Byte -> when(b.held) {
                    is Double -> Double::class.java
                    is Float -> Float::class.java
                    is Long -> Long::class.java
                    is Int -> Int::class.java
                    is Short -> Short::class.java
                    else -> Byte::class.java
                }

                else -> throw RuntimeException("Unknown number class")
            }
        }
    }
    
}