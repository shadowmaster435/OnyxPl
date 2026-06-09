package org.shadowmaster435.operators.other

import org.shadowmaster435.built_ins.nullType
import org.shadowmaster435.code.fields.OnyxVar
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.impl.abstracts.OnyxOperator
import org.shadowmaster435.util.GenericHolder

open class OnyxAssignment(leftType: OnyxType, rightType: OnyxType): OnyxBinaryOperator(leftType, rightType, nullType) {
    override val precedence: Int = 0
    @Suppress("UNCHECKED_CAST")
    override fun evaluate(params: EvaluationParams): DataProvider {
        val a = params.getParam(0)
        val b = params.getParam(1)
        a.held = b.held
        return GenericHolder(Unit)
    }
}