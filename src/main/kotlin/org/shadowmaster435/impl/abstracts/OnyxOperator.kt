package org.shadowmaster435.impl.abstracts

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.enums.CodeObjType

abstract class OnyxOperator(type: OnyxType) : CodeObject {

    override val objType = CodeObjType.OPERATOR
    abstract val precedence : Int
    fun begin(): EvaluationParams {
        return EvaluationParams()
    }
    abstract fun evaluate(params: EvaluationParams): DataProvider
   // abstract fun LLVMBuilder.Builder.toLLVM(params: EvaluationParams): LLVMConstableValue<Number>

    inner class EvaluationParams {
        private val params = mutableListOf<DataProvider>()
        fun accept(dataProvider: DataProvider): EvaluationParams {
            params.add(dataProvider)
            return this
        }
        fun getParam(index: Int) = params[index]
        fun evaluate(): DataProvider {
            return this@OnyxOperator.evaluate(this)
        }
    }
}