package org.shadowmaster435.operators.logical

import org.shadowmaster435.built_ins.OnyxBuiltinClasses
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.util.GenericHolder

open class OnyxIs(leftType: OnyxType, rightType: OnyxType): OnyxBinaryOperator(leftType, rightType, OnyxBuiltinClasses.OnyxBooleanClass.type) {
    override val precedence: Int = 4
    @Suppress("UNCHECKED_CAST")
    override fun evaluate(params: EvaluationParams): DataProvider {
        val a = params.getParam(0)
        val b = params.getParam(1)
        return GenericHolder(a.type === b.type)
    }
}