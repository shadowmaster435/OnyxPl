package org.shadowmaster435.util

import org.shadowmaster435.classes.anyType
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember

class GenericHolder(override var held: Any?) : DataProvider, OnyxMember {
    override fun toString() = "$held"
    override val type = anyType
    override var initialized = true
    override fun instantiate(vararg params: DataProvider) = this
}