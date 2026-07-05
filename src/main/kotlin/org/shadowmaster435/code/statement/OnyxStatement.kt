package org.shadowmaster435.code.statement

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.enums.CodeObjType

abstract class OnyxStatement : CodeObject, DataProvider {
    override val objType = CodeObjType.STATEMENT
    abstract override var held: Any?
}