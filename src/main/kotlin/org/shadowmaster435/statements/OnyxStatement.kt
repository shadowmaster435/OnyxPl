package org.shadowmaster435.statements

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.enums.CodeObjType

abstract class OnyxStatement<T>(override val typeClass : Class<T>) : CodeObject<T>, DataProvider<T> {
    override val objType = CodeObjType.STATEMENT
    abstract override var held: T
}