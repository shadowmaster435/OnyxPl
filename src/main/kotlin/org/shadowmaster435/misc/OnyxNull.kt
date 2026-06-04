package org.shadowmaster435.misc

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.enums.CodeObjType

object OnyxNull : OnyxConstable<Any?>(Unit), CodeObject<Any?> {
    override val objType = CodeObjType.DATA
    override var held: Any
        get() = super.held
        set(_) {}
}