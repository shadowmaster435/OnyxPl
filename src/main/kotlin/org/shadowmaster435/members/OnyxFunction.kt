package org.shadowmaster435.members

import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.misc.OnyxTuples

abstract class OnyxFunction<T>(override val name: String?, override val typeClass: Class<*>, val tuples: OnyxTuples, val inputs: List<DataProvider<*>>, val prevalidated: Boolean = false) : OnyxMember, DataProvider<T> {
    override var held: T = invoke()
    init {
        if (!prevalidated) tuples.validate(inputs)
    }
    abstract fun invoke(): T
}