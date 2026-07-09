package org.shadowmaster435.operators.logical

import org.shadowmaster435.built_ins.OnyxPrimitives
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxUnaryOperator
import org.shadowmaster435.util.GenericHolder

open class OnyxNotNull(type: OnyxType, retType: OnyxType) : OnyxUnaryOperator(type, retType) {
    override val precedence = 12
    override fun evaluate(params: EvaluationParams): DataProvider {
        val a = params.getParam(0)
        return OnyxPrimitives.OnyxBoolean(a.held != null)
    }

    override fun toString() = "!!"
}