package org.shadowmaster435.operators.other

import org.shadowmaster435.built_ins.OnyxPrimitives.*
import org.shadowmaster435.built_ins.pointerType
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.abstracts.OnyxBinaryOperator
import org.shadowmaster435.impl.abstracts.OnyxUnaryOperator
import org.shadowmaster435.util.GenericHolder

open class OnyxPointerize(type: OnyxType): OnyxUnaryOperator(type, pointerType.of("T" to type)) {
    override val precedence: Int = 12
    override fun evaluate(params: EvaluationParams): DataProvider {
        val a = params.getParam(0)
        return OnyxPointer(a, type)
    }
}