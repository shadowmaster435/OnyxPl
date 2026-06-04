package org.shadowmaster435.misc

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.enums.CodeObjType

open class OnyxConstable<T>(open val v: T & Any) : DataProvider<T & Any>, CodeObject<T> {
    override val objType = CodeObjType.DATA
    override var held: T & Any = v
    override val typeClass: Class<*> = v::class.java
    override fun toString() = v.toString()

}