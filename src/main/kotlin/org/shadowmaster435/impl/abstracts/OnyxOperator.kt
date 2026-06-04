package org.shadowmaster435.impl.abstracts

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.enums.CodeObjType

abstract class OnyxOperator<T> : CodeObject<T> {

    override val objType = CodeObjType.OPERATOR
    abstract val precedence : Int
    fun begin(): EvaluationParams {
        return EvaluationParams()
    }
    abstract fun evaluate(params: EvaluationParams): DataProvider<T>
   // abstract fun LLVMBuilder.Builder.toLLVM(params: EvaluationParams): LLVMConstableValue<Number>

    @Suppress("UNCHECKED_CAST")
    inner class EvaluationParams {
        private val params = mutableListOf<DataProvider<T>>()
        fun accept(dataProvider: DataProvider<*>): OnyxOperator<T>.EvaluationParams {
            params.add(dataProvider as DataProvider<T>)
            return this
        }
        fun getParam(index: Int) = params[index]
        fun evaluate(): DataProvider<T> {
            return this@OnyxOperator.evaluate(this)
        }
    }

    class OnyxOperatorValue<T>(private val default: DataProvider<T>): DataProvider<T> {
        override var held = default.held
        override val typeClass = default.typeClass
        @Suppress("UNCHECKED_CAST")
        fun changeHeld(any: Any?) {
            held = any as T
        }
        fun reset() {
            held = default.held
        }

        override fun toString(): String {
            return held.toString()
        }
    }
    companion object {

    }
}