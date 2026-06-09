package org.shadowmaster435.operators.other

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.util.GenericHolder

open class OnyxElvis(type: OnyxType): OnyxBinaryOperator(type, type, type) {
    override val precedence: Int = 4
    @Suppress("UNCHECKED_CAST")
    override fun evaluate(params: EvaluationParams): DataProvider {
        val a = params.getParam(0)
        val b = params.getParam(1)
        return GenericHolder(a.held ?: b.held)
    }
}