package org.shadowmaster435.misc

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.enums.CodeObjType
import org.shadowmaster435.memory.OnyxClassInstance

open class OnyxConstable(open val v: Any?, override val type: OnyxType) : DataProvider, CodeObject {
    override var initialized: Boolean = true
    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider) = this
    override val objType = CodeObjType.DATA
    override var held: Any? = v
    override fun toString() = v.toString()
}